package upgrade.netty.tcp.recv;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


@Component("receiveInitializer")
public class ReceiveInitializer extends ChannelInitializer<SocketChannel> {
	
	@Resource(name="receiveHandler")
	private ReceiveHandler receiveHandler;
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline cp=ch.pipeline();
		
		//此处的日志Handler用来显示业务操作（可以删除）
		cp.addLast("logger",new LoggingHandler(LogLevel.INFO));
		
		/**
		 * 由于使用字节的方式进行文件的接受，因此不�?要其他的解码器（ChannelHandler�?
		 */
		
		cp.addLast("handler",receiveHandler);
	}

}
