package upgrade.job;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppJob {
	
	public static void main(String[] args) throws InterruptedException {
		PropertyConfigurator.configure("E:/MyProject/Eclipse/upgrade/src/log4j.xml");
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
		Thread.sleep(300000);
		context.close();

	}
}
