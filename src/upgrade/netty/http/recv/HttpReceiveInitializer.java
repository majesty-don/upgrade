package upgrade.netty.http.recv;

import javax.annotation.Resource;
import javax.net.ssl.SSLEngine;

import org.springframework.stereotype.Component;

import upgrade.netty.ssl.SecureSSLContextFactory;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

@Component("receiveHttpInitializer")
public class HttpReceiveInitializer extends ChannelInitializer<SocketChannel> {
	
	@Resource(name="receiveHttpHandler")
	private HttpReceiveHandler receiveHandler;
	
	private boolean isSSL;
	
	public boolean isSSL() {
		return isSSL;
	}

	public void setSSL(boolean isSSL) {
		this.isSSL = isSSL;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// Create a default pipeline implementation.
        ChannelPipeline pipeline = ch.pipeline();

        if (isSSL) {
            SSLEngine engine = SecureSSLContextFactory.getServerContext().createSSLEngine();
            engine.setUseClientMode(false);
            pipeline.addLast("ssl", new SslHandler(engine));
        }

        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("encoder", new HttpResponseEncoder());

        // Remove the following line if you don't want automatic content
        // compression.
        pipeline.addLast("deflater", new HttpContentCompressor());
        
        pipeline.addLast( "http-aggregator", new HttpObjectAggregator( Integer.MAX_VALUE ) );
		
        pipeline.addLast("chunkedWriter",new ChunkedWriteHandler());

        pipeline.addLast("handler", receiveHandler);
		
	}

}
