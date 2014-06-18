package upgrade.netty.region.send;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import upgrade.netty.IServer;

@Service
@Component("regionsend")
public class RegionSend implements IServer {

	/**用于分配处理业务线程的线程组个数 */
	protected static final int GROUPSIZE = Runtime.getRuntime().availableProcessors()*2;//默认
	/** 业务线程大小*/
	protected static final int THREADSIZE = 4;
	
	
	@Resource(name="sendRegionInitializer")
	private RegionSendInitializer sendInitializer;
	
	private String host="127.0.0.1";
	private int port=9017;
	
	public RegionSend() {
		
	}

	public RegionSend(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		EventLoopGroup boss=new NioEventLoopGroup(GROUPSIZE);
		try {
			Bootstrap boot=new Bootstrap();
			
			boot.group(boss).channel(NioSocketChannel.class);
			
			boot.handler(new LoggingHandler(LogLevel.INFO));
			
			boot.handler(sendInitializer);
			
			boot.option(ChannelOption.SO_KEEPALIVE, true);
			
			ChannelFuture cf=boot.connect(host, port).sync();
			
			cf.channel().closeFuture().sync();
					
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			boss.shutdownGracefully();
		}	
	}

}
