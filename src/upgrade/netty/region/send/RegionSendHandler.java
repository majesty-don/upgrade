package upgrade.netty.region.send;

import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import upgrade.netty.util.FileTool;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

@Component("sendRegionHandler")
public class RegionSendHandler extends SimpleChannelInboundHandler<String> {

	private static final Logger logger = LogManager.getLogger(RegionSendHandler.class);
	
	private final String filename = "c3p0-service.xml";
	private long length=0L;
	private File file=null;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
		if( ctx.channel().isRegistered() && ctx.channel().isWritable()){
			file = new File(FileTool.getFilePath("send", filename));
			if(!file.exists()){
				ctx.close();
				return ;
			}
			length=file.length();
			ctx.writeAndFlush(filename + "=" + length);
		}
	}

	private FileInputStream fileInputStream;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		logger.info(msg);		
        if (file.exists()) {
            if (!file.isFile()) {
                ctx.writeAndFlush("Not a file: " + file + '\n');
                return;
            }
            
            fileInputStream = new FileInputStream(file);
            FileChannel channel=fileInputStream.getChannel();
			FileRegion region = new DefaultFileRegion(channel, 0, length);
            ctx.write(region);
            ctx.flush();
            //ctx.writeAndFlush("\n");
        } else {
            ctx.writeAndFlush("File not found: " + file + '\n');
        }
		
	}
	
}
