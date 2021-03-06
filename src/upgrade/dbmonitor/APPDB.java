package upgrade.dbmonitor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class APPDB {
	private static final Logger logger = LogManager.getLogger(APPDB.class);
	public static void main(String[] args) throws InterruptedException { 
		PropertyConfigurator.configure("E:/MyProject/Eclipse/upgrade/src/log4j.xml");
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
		logger.debug("程序启动时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		Thread.sleep(900000);
		context.close();   
    } 
}
