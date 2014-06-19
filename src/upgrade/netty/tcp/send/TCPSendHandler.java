package upgrade.netty.tcp.send;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


import upgrade.netty.util.FileTool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.CharsetUtil;

@Component("sendTcpHandler")
public class TCPSendHandler extends ByteToMessageCodec<String> {

	private static final Logger logger = LogManager.getLogger(TCPSendHandler.class);
	
	private final String filename = "worldcup.jpg";
	private File file=null;
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
		if( ctx.channel().isRegistered() && ctx.channel().isWritable()){
			file = new File(FileTool.getFilePath("send", filename));
			
			String info=filename + " " + file.length();
			
			logger.info("file info:"+info);
            
            ctx.writeAndFlush(Unpooled.copiedBuffer(info.getBytes(CharsetUtil.UTF_8)));
		}
	}
	
	protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out)
			throws Exception {
	}

	private static final int SIZE=1024;
	private InputStream is;
	
	private static enum Block {
		START, END;
	};

	private Block current = Block.START;

	private void nextMode(Block nextMode) {
		current = nextMode;
	}
	
	protected void decode(final ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		switch(current){
		
		case START:
			logger.debug("msg:"+in.toString(CharsetUtil.UTF_8));
			
			in.clear();
			
			is = new BufferedInputStream( new FileInputStream(file));
	        
	        int filelength=(int)file.length();
	        
	        logger.debug("file length:"+filelength);
	       
			int pos=filelength;//剩余文件的位�?
			
			byte[] buffer=new byte[SIZE];//文件发�?�块的大�?
			int length=SIZE;
				
			while(length>0 && pos>0){
				
				if(pos<length){
					length=pos;
				}
				
				if(length<SIZE){
					buffer=new byte[length];
				}
				
				length=is.read(buffer, 0, length);
				
				pos-=length;
				
				logger.info("pos:"+pos+",send file length:"+length);
				
				ctx.writeAndFlush(Unpooled.copiedBuffer(buffer));

			}

			if(pos==0){
				is.close();
				logger.info("Transfer complete.");
			}
			nextMode(Block.END);
			break;
		case END:
			String msg=in.toString(CharsetUtil.UTF_8);
			logger.info("The Message that Server Responses:"+msg);
			if("Finish".equals(msg)){
				logger.info("服务器接收文件结束，客户端即将关闭...");
				ctx.close();
				logger.info("客户端关闭...");
			}
			in.clear();
			nextMode(Block.START);
		default:
			in.clear();
			nextMode(Block.START);
			break;
		
		}
	}
	
}
