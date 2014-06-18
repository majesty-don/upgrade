package upgrade.netty.http.send;

import java.nio.charset.Charset;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Client {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		PropertyConfigurator.configure("E:/MyProject/Eclipse/upgrade/src/log4j.xml");
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
		
		Thread thread=new Thread(new Send());
		thread.start();
//		IServer server=(IServer) context.getBean("httpsend");
//		
//		try {
//			server.run();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Thread.sleep(300000);
		context.close();
		
		String encoding = System.getProperty("file.encoding");
		System.out.println("你的操作系统使用的编码为："+encoding);
		
		System.out.println(Charset.defaultCharset()); 

	}

}
