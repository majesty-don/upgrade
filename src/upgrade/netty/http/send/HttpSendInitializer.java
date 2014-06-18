package upgrade.netty.http.send;

import javax.net.ssl.SSLEngine;

import org.springframework.stereotype.Component;

import upgrade.netty.ssl.SecureSSLContextFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

@Component("sendHttpInitializer")
public class HttpSendInitializer extends ChannelInitializer<Channel> {
	
	private  boolean ssl;
	
	public HttpSendInitializer() {
		// TODO Auto-generated constructor stub
	}
	
	public HttpSendInitializer(boolean ssl) {
		this.ssl = ssl;
	}

	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline cp=ch.pipeline();
		
		cp.addLast("logger", new LoggingHandler());
		
		if (ssl) {
            SSLEngine engine = SecureSSLContextFactory.getClientContext().createSSLEngine();
            engine.setUseClientMode(true);
            cp.addLast("ssl", new SslHandler(engine));
        }
		
		cp.addLast("codec", new HttpClientCodec());
		
		cp.addLast("inflater", new HttpContentDecompressor());
		
		cp.addLast( "http-aggregator", new HttpObjectAggregator( Integer.MAX_VALUE ) );
		
		cp.addLast("chunkedWriter",new ChunkedWriteHandler());
		
		cp.addLast("handler", new HttpSendHandler());
		
	}

}
