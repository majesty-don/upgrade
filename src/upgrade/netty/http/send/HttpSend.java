package upgrade.netty.http.send;


import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import upgrade.netty.util.FileTool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.ClientCookieEncoder;
import io.netty.handler.codec.http.DefaultCookie;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder.ErrorDataEncoderException;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Component("httpsend")
@Service
public class HttpSend implements Runnable {

	private static final Logger logger=LogManager.getLogger(HttpSend.class);
	
	/**用于分配处理业务线程的线程组个数 */
	protected static final int GROUPSIZE = Runtime.getRuntime().availableProcessors()*2;//默认
	/** 业务线程大小*/
	protected static final int THREADSIZE = 4;
	
	
//	@Resource(name="sendHttpInitializer")
//	private SendInitializer sendInitializer ;
	
	private String uri="http://localhost:9017";
    private String filePath=FileTool.getFilePath("send", "worldcup.jpg");
    
    public HttpSend() {
		// TODO Auto-generated constructor stub
	}
    
    public HttpSend(String uri, String filePath) {
        this.uri = uri;
        this.filePath = filePath;
    }
    
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public void run() {
		
		String postSimple;
        if (uri.endsWith("/")) {
            postSimple = uri + "formpost";
        } else {
            postSimple = uri + "/formpost";
        }
        
        logger.info("postSimple:"+postSimple);
        
        URI uriSimple;
        try {
            uriSimple = new URI(postSimple);
        } catch (URISyntaxException e) {
            logger.warn("Invalid URI syntax", e);
            return;
        }
        String scheme = uriSimple.getScheme() == null ? "http" : uriSimple.getScheme();
        String host = uriSimple.getHost() == null ? "localhost" : uriSimple.getHost();
        int port = uriSimple.getPort();
        if (port == -1) {
            if ("http".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("https".equalsIgnoreCase(scheme)) {
                port = 443;
            }
        }

        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            logger.warn("Only HTTP(S) is supported.");
            return;
        }

        boolean ssl = "https".equalsIgnoreCase(scheme);
        
        File file = new File(filePath);
        if (!file.canRead()) {
            logger.warn("A correct path is needed");
            return;
        }
        
        // setup the factory: here using a mixed memory/disk based on size threshold
        HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); // Disk if MINSIZE exceed

        DiskFileUpload.deleteOnExitTemporaryFile = true; // should delete file on exit (in normal exit)
        DiskFileUpload.baseDirectory = null; // system temp directory
        DiskAttribute.deleteOnExitTemporaryFile = true; // should delete file on exit (in normal exit)
        DiskAttribute.baseDirectory = null; // system temp directory
		
		EventLoopGroup boss=new NioEventLoopGroup(GROUPSIZE);
		try {
			Bootstrap boot=new Bootstrap();
			
			HttpSendInitializer sendInitializer=new HttpSendInitializer(ssl);
			//sendInitializer.setSsl(ssl);
			
			boot.group(boss).channel(NioSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO))
				.handler(sendInitializer);
				
			// Simple Post form: factory used for big attributes
            List<InterfaceHttpData> bodylist = formPost(boot, host, port, uriSimple, file, factory);
            if (bodylist == null) {
                factory.cleanAllHttpDatas();
                return;
            }
			
			//ChannelFuture cf=boot.connect(host, port).sync();
			
			//cf.channel().closeFuture().sync();
					
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//formPost抛出的异常
			e.printStackTrace();
		}finally{
			// Shut down executor threads to exit.
			boss.shutdownGracefully();

            // Really clean all temporary files if they still exist
            factory.cleanAllHttpDatas();
		}	
	}
    
    /**
     * Standard post without multipart but already support on Factory (memory management)
     *
     * @return the list of HttpData object (attribute and file) to be reused on next post
     */
    private static List<InterfaceHttpData> formPost(Bootstrap bootstrap, String host, int port, URI uriSimple,
            File file, HttpDataFactory factory) throws Exception {

        // Start the connection attempt
        Channel channel = bootstrap.connect(host, port).sync().channel();

        // Prepare the HTTP request.
        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uriSimple.toASCIIString());

        // Use the PostBody encoder
        HttpPostRequestEncoder bodyRequestEncoder = null;
        try {
            bodyRequestEncoder = new HttpPostRequestEncoder(factory, request, true); // false not multipart
        } catch (NullPointerException e) {
            // should not be since args are not null
            e.printStackTrace();
        } catch (ErrorDataEncoderException e) {
            // test if getMethod is a POST getMethod
            e.printStackTrace();
        }

        // it is legal to add directly header or cookie into the request until finalize
        HttpHeaders headers = request.headers();
        headers.set(HttpHeaders.Names.HOST, host);
        headers.set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        headers.set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP + ','+ HttpHeaders.Values.DEFLATE);

        headers.set(HttpHeaders.Names.ACCEPT_CHARSET, "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        headers.set(HttpHeaders.Names.ACCEPT_LANGUAGE, "zh-CN");
        headers.set(HttpHeaders.Names.REFERER, uriSimple.toString());
        headers.set(HttpHeaders.Names.USER_AGENT, "Netty Simple Http Client side");
        headers.set(HttpHeaders.Names.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        headers.set(HttpHeaders.Names.COOKIE, ClientCookieEncoder.encode(new DefaultCookie("my-cookie", "foo"),
                new DefaultCookie("another-cookie", "bar")));
        
        headers.set("Content-Name", file.getName());

        // add Form attribute
        try {
            bodyRequestEncoder.addBodyAttribute("method", "POST");
            bodyRequestEncoder.addBodyFileUpload("myfile", file, "application/x-zip-compressed", false);
            bodyRequestEncoder.addBodyAttribute("submit", "SUBMIT");
        } catch (NullPointerException e) {
            // should not be since not null args
            e.printStackTrace();
        } catch (ErrorDataEncoderException e) {
            // if an encoding error occurs
            e.printStackTrace();
        }

        // finalize request
        try {
            request = bodyRequestEncoder.finalizeRequest();
        } catch (ErrorDataEncoderException e) {
            // if an encoding error occurs
            e.printStackTrace();
        }

        // Create the bodylist to be reused on the last version with Multipart support
        List<InterfaceHttpData> bodylist = bodyRequestEncoder.getBodyListAttributes();

        // send request
        channel.write(request);

        // test if request was chunked and if so, finish the write
        if (bodyRequestEncoder.isChunked()) {
            // could do either request.isChunked()
            // either do it through ChunkedWriteHandler
            channel.writeAndFlush(bodyRequestEncoder).awaitUninterruptibly();
        }  else {
            channel.flush();
        }

        /* Do not clear here since we will reuse the InterfaceHttpData on the
         next request
         for the example (limit action on client side). Take this as a
         broadcast of the same
         request on both Post actions.
        
         On standard program, it is clearly recommended to clean all files
         after each request
         bodyRequestEncoder.cleanFiles();*/

        // Wait for the server to close the connection.
        channel.closeFuture().sync();

        return bodylist;
    }
    
}
