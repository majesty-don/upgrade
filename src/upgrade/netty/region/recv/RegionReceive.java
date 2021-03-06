package upgrade.netty.region.recv;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import upgrade.netty.IServer;

@Component("regionreceive")
@Service
public class RegionReceive implements IServer {

	/**用于分配处理业务线程的线程组个数 */
	private static final int GROUPSIZE = Runtime.getRuntime().availableProcessors()*2;//默认
	/** 业务线程大小*/
	private static final int THREADSIZE = 4;
	
	@Resource(name="receiveRegionInitializer")
	private RegionReceiveInitializer receiveInitializer;
	
	private int port=9017;
	
	public RegionReceive() {
		
	}
	
	public RegionReceive(int port) {
		this.port = port;
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
		EventLoopGroup worker=new NioEventLoopGroup(THREADSIZE);
		try {
			ServerBootstrap boot=new ServerBootstrap();
			
			boot.group(boss, worker)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(receiveInitializer);
		
			ChannelFuture cf=boot.bind(port).sync();
			
			cf.channel().closeFuture().sync();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}	
	}

}
