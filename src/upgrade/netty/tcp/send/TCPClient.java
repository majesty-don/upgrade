package upgrade.netty.tcp.send;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import upgrade.netty.IServer;

public class TCPClient {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		PropertyConfigurator.configure("E:/MyProject/Eclipse/upgrade/src/log4j.xml");
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
		IServer server=(IServer) context.getBean("tcpsend");
		try {
			server.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread.sleep(300000);
		context.close();
		

	}

}
