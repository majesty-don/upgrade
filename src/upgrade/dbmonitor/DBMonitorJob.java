package upgrade.dbmonitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import upgrade.pojo.DBMonitor;
import upgrade.service.DBMonitorService;
import upgrade.util.FileTool;

@Service
public class DBMonitorJob extends QuartzJobBean {

	private static final Logger logger = LogManager.getLogger(DBMonitorJob.class);
	
	@Autowired
	private DBMonitorService dbMonitorService;
	
	private static final String pathInit = FileTool.getDir("png");
	private static final int dateCntInit = 1;
	private static final int firstDayInit = 0;
	
	//TreeSet不是同步的
	//SortedSet s = Collections.synchronizedSortedSet(new TreeSet(...)); 可以保证同步
	private final static Set<DBMonitor> cityEnMap = new TreeSet<>();
	
	@Scheduled(cron = "0 0 0-23/2 * * ?")
	public void start() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 加载驱动
		
		List<DBMonitor> ldbm=dbMonitorService.findAll();
		cityEnMap.addAll(ldbm); 		
		logger.debug("全部数据库信息："+JSON.toJSONString(cityEnMap));
		
		ExecutorService es = new ThreadPoolExecutor(10, Integer.MAX_VALUE, 60L,
				TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		
		final Collection<Callable<DBMonitor>> tasks = new ArrayList<>();
		
		logger.debug("文件路径："+pathInit);
		
		for (DBMonitor city : cityEnMap) {
			tasks.add(new GraphTask(city, firstDayInit, dateCntInit, pathInit));
		}
		
		try {
			es.invokeAll(tasks, 15L, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.exit(0);
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {		
		start();	
	}
	
	/*public static void main(String[] args){
		String relativePath="/tcpsgps/tomcat6/bin"; 
		System.out.println(relativePath.replaceAll("bin", "upgrade/png"));
	}*/
}
