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

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import upgrade.pojo.City;
import upgrade.service.CityService;
import upgrade.util.FileTool;

@Service
public class DBMonitorJob extends QuartzJobBean {

	private static final Logger logger = LogManager.getLogger(DBMonitorJob.class);
	
	@Resource(name="cityService")
	private CityService cityService;
	
	private static final String pathInit = FileTool.getDir("png");
	private static final int dateCntInit = 1;
	private static final int firstDayInit = 0;
	
	//TreeSet不是同步的
	//SortedSet s = Collections.synchronizedSortedSet(new TreeSet(...)); 可以保证同步
	private final static Set<City> cityEnMap = new TreeSet<>();
	
	@Scheduled(cron = "0 0 0-23/2 * * ?") //*/15 * * * * ? 测试使用
	public void start() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 加载驱动
		
		List<City> ldbm=cityService.getDBs();
		cityEnMap.addAll(ldbm); 		
		logger.debug("全部数据库信息："+JSON.toJSONString(cityEnMap));
		
		ExecutorService es = new ThreadPoolExecutor(10, Integer.MAX_VALUE, 60L,
				TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		
		final Collection<Callable<City>> tasks = new ArrayList<>();
		
		logger.debug("文件路径："+pathInit);
		
		for (City city : cityEnMap) {
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
}
