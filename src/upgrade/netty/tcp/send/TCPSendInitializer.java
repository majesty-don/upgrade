package upgrade.netty.tcp.send;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LoggingHandler;

@Component("sendTcpInitializer")
public class TCPSendInitializer extends ChannelInitializer<Channel> {

	@Resource(name="sendTcpHandler")
	private TCPSendHandler sendHandler;
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline cp=ch.pipeline();
		
		//此处的日志Handler用来显示业务操作（可以删除）
		cp.addLast("logger", new LoggingHandler());
		
		/**
		 * 由于使用字节的方式进行文件的接受，因此不需要其他的解码器（ChannelHandler�?
		 */
		
		cp.addLast("handler", sendHandler);
		
	}

}
