package upgrade.netty.region.recv;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import upgrade.util.FileTool;


import com.alibaba.fastjson.JSON;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Component("receiveRegionHandler")
public class RegionReceiveHandler extends SimpleChannelInboundHandler<String> {

	private static final Logger logger = LogManager.getLogger(RegionReceiveHandler.class);
	
	private static final Charset charset=Charset.forName("GBK");
	
	private static enum Block {
		INFO, DATA;
	};

	private Block current = Block.INFO;

	private void nextMode(Block nextMode) {
		current = nextMode;
	}
	
	private long filelength=0L;
	
	private File file=null;
	private FileOutputStream fos=null;
	
	private BufferedOutputStream bos=null;
	
	private long size=0;
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		switch(current){
		case INFO:
			String[] info=msg.split("=");
			logger.info("info:"+JSON.toJSONString(info));
			String filename=info[0];
			filelength=Long.parseLong(info[1]);
			file=new File(FileTool.getFilePath("recv", filename));
			if(file.exists()){
				file.delete();
				file.createNewFile();
			}
			fos=new FileOutputStream(file);
			bos=new BufferedOutputStream(fos);
			ctx.writeAndFlush("OK");
			nextMode(Block.DATA);
			break;
		case DATA:
			byte[] bytes=msg.getBytes(charset);
			int length=bytes.length;
			size+=length;
			ByteBuf buffer=Unpooled.copiedBuffer(bytes);
			buffer.readBytes(bos, length);
			bos.flush();
			fos.flush();
			if(size==filelength){
				bos.close();
				fos.close();
				logger.info("Finished");
				nextMode(Block.INFO);
				ctx.close();
			}else{
				nextMode(Block.DATA);
			}
			
			break;
		default:
			nextMode(Block.INFO);
			break;
		
		}
		
	}

}
