package upgrade.netty.region.send;

import java.nio.charset.Charset;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Component("sendRegionInitializer")
public class RegionSendInitializer extends ChannelInitializer<Channel> {
	
	@Resource(name="sendRegionHandler")
	private RegionSendHandler sendHander;
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline cp=ch.pipeline();
		
		//此处的日志Handler用来显示业务操作（可以删除）
		cp.addLast("logger",new LoggingHandler(LogLevel.INFO));
		
		cp.addLast("decoder", new StringDecoder(Charset.forName("GBK")));
        cp.addLast("frame", new LineBasedFrameDecoder(8192));
        cp.addLast("encoder", new StringEncoder(Charset.forName("GBK")));
		
		cp.addLast("handler",sendHander);
		
	}

}
