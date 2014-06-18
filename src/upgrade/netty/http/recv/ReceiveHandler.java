package upgrade.netty.http.recv;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpMessage;

public class ReceiveHandler extends SimpleChannelInboundHandler<HttpMessage> {

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, HttpMessage arg1)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
