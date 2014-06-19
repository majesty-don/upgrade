package upgrade.netty.tcp.recv;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import upgrade.util.FileTool;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.CharsetUtil;

/**
 * 
 * @Sharable 解决多个客户端连接服务器，服务器能够向客户端回馈讯息
 * Be aware that sub-classes of ByteToMessageCodec MUST NOT annotated with @Sharable.
 */

@Component("receiveTcpHandler")
public class TCPReceiveHandler extends ByteToMessageCodec<String> {

	private static final Logger logger = LogManager.getLogger(TCPReceiveHandler.class);
	
	private static enum Block {
		INFO, DATA;
	};

	private Block current = Block.INFO;

	private void nextMode(Block nextMode) {
		current = nextMode;
	}
	
	private String pathname=null;
	private long filelength=0L;
	
	private FileOutputStream fos=null;
	
	private BufferedOutputStream bos=null;
	
	private long size=0;

	@Override
	protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		switch(current){
		case INFO:
			
			String msg=in.toString(CharsetUtil.UTF_8);
			
			logger.info("msg:"+msg);
			
			String info[]=msg.split(" ");
			
			logger.info("info:"+JSON.toJSONString(info));
			
			pathname=FileTool.getFilePath("recv", info[0]);
			logger.info("file path:"+pathname);
			
			filelength=Long.parseLong(info[1]);
			logger.info("file length:"+filelength);
			
			File recvfile=new File(pathname);
			
			if(recvfile.isFile()&& recvfile.exists()){
				recvfile.delete();
				recvfile.createNewFile();
			}
			
			fos = new FileOutputStream(recvfile);
			bos = new BufferedOutputStream(fos);
			
			ctx.writeAndFlush(Unpooled.copiedBuffer("OK".getBytes()));
			
			in.clear();
			nextMode(Block.DATA);
			break;
			
		case DATA:
			
			int length = in.readableBytes();
			
			logger.info("length:"+length);
			
			in.readBytes(bos, length);
			fos.flush();
			bos.flush();
			size+=length;
			logger.info("已接收文件字节数�?"+size);
			if(size==filelength){
				
				fos.close();
				bos.close();
				logger.info("Accept Finish");
				ctx.writeAndFlush(Unpooled.copiedBuffer("Finish".getBytes(CharsetUtil.UTF_8)));
				in.clear();
				nextMode(Block.INFO);			
			}else{
				in.clear();
				nextMode(Block.DATA);
			}
			
			break;
		default:
			nextMode(Block.INFO);
			break;
		}	
	}

}
