package upgrade.netty.http.send;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

@Component("sendHttpHandler")
public class HttpSendHandler extends SimpleChannelInboundHandler<HttpMessage> {

	private static final Logger logger = LogManager.getLogger(HttpSendHandler.class);

	private boolean readingChunks;
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpMessage msg)
			throws Exception {

		if(msg instanceof HttpResponse){
			logger.info("服务器回应（receiver response�?");
			HttpResponse response = (HttpResponse) msg;

            logger.info("STATUS: " + response.getStatus());
            logger.info("VERSION: " + response.getProtocolVersion());

            if (!response.headers().isEmpty()) {
                for (String name : response.headers().names()) {
                    for (String value : response.headers().getAll(name)) {
                        logger.info("HEADER: " + name + " = " + value);
                    }
                }
            }

            if (response.getStatus().code() == 200 && HttpHeaders.isTransferEncodingChunked(response)) {
                readingChunks = true;
                logger.info("CHUNKED CONTENT {");
            } else {
                logger.info("CONTENT {");
            }
        }
        if (msg instanceof HttpContent) {
            HttpContent chunk = (HttpContent) msg;
            logger.info(chunk.content().toString(CharsetUtil.UTF_8));

            if (chunk instanceof LastHttpContent) {
                if (readingChunks) {
                    logger.info("} END OF CHUNKED CONTENT");
                } else {
                    logger.info("} END OF CONTENT");
                }
                readingChunks = false;
            } else {
                logger.info(chunk.content().toString(CharsetUtil.UTF_8));
            }
        }
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
		ctx.close();
	}

}
