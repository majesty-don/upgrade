package upgrade.job;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import upgrade.pojo.City;
import upgrade.pojo.FileCount;
import upgrade.service.CityService;
import upgrade.service.FileCountService;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

@Service
@Component("quartzJob")
public class QuartzJob extends QuartzJobBean {
	
	private static final Logger logger=LogManager.getLogger(QuartzJob.class);
	
	@Resource(name="fileCountService")
	private FileCountService fileCountService;
	
	@Resource(name="cityService")
	private CityService cityService;
	
	private static InputStream stdout;
	private static InputStream stderr;
	private static Session sess;
	private static Connection conn;

	private String doit(int i, InputStream stdout, InputStream stderr, Session sess) {

		String ss = null;
		Map<String, String> map = new HashMap<String, String>();
		byte[] buffer = new byte[8192];
		int readCount = 0;
		while (true) {
			try {
				if ((stdout.available() == 0) && (stderr.available() == 0)) {
					/*
					 * Even though currently there is no data available, it may
					 * be that new data arrives and the session's underlying
					 * channel is closed before we call waitForCondition(). This
					 * means that EOF and STDOUT_DATA (or STDERR_DATA, or both)
					 * may be set together.
					 */

					int conditions = sess.waitForCondition(
							ChannelCondition.STDOUT_DATA
									| ChannelCondition.STDERR_DATA
									| ChannelCondition.EOF, 4000);

					/* Wait no longer than 2 seconds (= 2000 milliseconds) */

					if ((conditions & ChannelCondition.TIMEOUT) != 0) {
						/* A timeout occured. */
						throw new IOException(
								"Timeout while waiting for data from peer.");
					}

					/*
					 * Here we do not need to check separately for CLOSED, since
					 * CLOSED implies EOF
					 */

					if ((conditions & ChannelCondition.EOF) != 0) {
						/* The remote side won't send us further data... */

						if ((conditions & (ChannelCondition.STDOUT_DATA | ChannelCondition.STDERR_DATA)) == 0) {
							/*
							 * ... and we have consumed all data in the local
							 * arrival window.
							 */
							break;
						}
					}

					/* OK, either STDOUT_DATA or STDERR_DATA (or both) is set. */

					// You can be paranoid and check that the library is not
					// going nuts:
					// if ((conditions & (ChannelCondition.STDOUT_DATA |
					// ChannelCondition.STDERR_DATA)) == 0)
					// throw new
					// IllegalStateException("Unexpected condition result (" +
					// conditions + ")");
				}

				/*
				 * If you below replace "while" with "if", then the way the
				 * output appears on the local stdout and stder streams is more
				 * "balanced". Addtionally reducing the buffer size will also
				 * improve the interleaving, but performance will slightly
				 * suffer. OKOK, that all matters only if you get HUGE amounts
				 * of stdout and stderr data =)
				 */

				if (i == 1) {

					while (stdout.available() > 0) {
						readCount += stdout.read(buffer, readCount,8192 - readCount);
						String s = new String(buffer, "utf-8");
						String[] temp = s.split("\n");
						if (temp.length > 1) {
							for (String string : temp) {
								if (string.trim().equals(""))
									continue;
								String[] temp1 = string.split(" ");
								map.put(temp1[1], temp1[0].split("\n")[0]);
							}
						}
					}
					
					ss = "BusServerApp: " + map.get("BusServerApp")
							+ ",GpsReceiverApp: " + map.get("GpsReceiverApp");
					if(map.get("DownloadServerApp")!=null){
						ss+=",DownloadServerApp: "+map.get("DownloadServerApp");
					}
					if(map.get("Main")!=null){
						ss+=",Main: " + map.get("Main");
					}
							

				} else if (i == 2) {
					while (stdout.available() > 0) {
						int len = stdout.read(buffer);
						if (len > 0){ // this check is somewhat paranoid
							String info=new String(buffer, 0, len);
							logger.info("文件数:"+info);
						}

					}
					ss = new String(buffer, "utf-8");
				}

				while (stderr.available() > 0) {
					int len = stderr.read(buffer);
					if (len > 0){ // this check is somewhat paranoid
						String err=new String(buffer, 0, len);
						logger.info("error:"+err);
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logger.info("抓取的文件数信息:"+JSON.toJSONString(ss));
		return ss.split("\n")[0];
	}

	String GetEachFileCount(Connection conn, String command,String appname) {
		String count = null;
		try {
			sess = conn.openSession();
			sess.execCommand(command);
			stdout = sess.getStdout();
			stderr = sess.getStderr();
			count = doit(2, stdout, stderr, sess);
			logger.info(appname+":"+count);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sess != null) {
				sess.close();
			}
		}

		return count;
	}

	public void GetFileCount(City city) {
		try {

			String filecount = null;
			String allfilecount;
			String command = null;
			logger.info("城市信息："+city.toString());
			conn = new Connection(city.getHost(), city.getPort22());

			/* Now connect */

			conn.connect();

			/* Authenticate */

			boolean isAuthenticated = conn.authenticateWithPassword(city.getUsername(),city.getPassword());

			if (isAuthenticated == false)
				throw new IOException("Authentication failed.");

			/* Create a session */

			sess = conn.openSession();

			// sess.execCommand("echo \"Huge amounts of text on STDOUT\"; echo \"Huge amounts of text on STDERR\" >&2");
			command = "jps";

			sess.execCommand(command);

			/*
			 * Advanced: The following is a demo on how one can read from stdout
			 * and stderr without having to use two parallel worker threads
			 * (i.e., we don't use the Streamgobblers here) and at the same time
			 * not risking a deadlock (due to a filled SSH2 channel window,
			 * caused by the stream which you are currently NOT reading from =).
			 */

			/*
			 * Don't wrap these streams and don't let other threads work on
			 * these streams while you work with Session.waitForCondition()!!!
			 */

			stdout = sess.getStdout();
			stderr = sess.getStderr();

			String s = doit(1, stdout, stderr, sess);

			sess.close();

			command = "ulimit -n";
			allfilecount = GetEachFileCount(conn, command,"MaxFileCount");

			int i = s.split(",").length;
			String a[] = s.split(",");
			String appname[] = new String[i];
			String count[] = new String[i];

			for (int b = 0; b < i; b++) {
				if (a[b].contains("BusServerApp:")) {
					command = "lsof -p " + a[b].split("BusServerApp: ")[1]
							+ " | wc -l";
					filecount = GetEachFileCount(conn, command,"BusServerApp");
					Array.set(appname, b, "BusServerApp");
					Array.set(count, b, filecount);

				} else if (a[b].contains("GpsReceiverApp:")) {

					command = "lsof -p " + a[b].split("GpsReceiverApp: ")[1]
							+ " | wc -l";
					filecount = GetEachFileCount(conn, command,"GpsReceiverApp");
					Array.set(appname, b, "GpsReceiverApp");
					Array.set(count, b, filecount);

				} else if (a[b].contains("Main:")) {
					command = "lsof -p " + a[b].split("Main: ")[1] + " | wc -l";
					filecount = GetEachFileCount(conn, command,"Main");
					Array.set(appname, b, "Main");
					Array.set(count, b, filecount);
				}else if(a[b].contains("DownloadServerApp:")){
					command = "lsof -p " + a[b].split("DownloadServerApp: ")[1] + " | wc -l";
					filecount = GetEachFileCount(conn, command,"DownloadServerApp");
					Array.set(appname, b, "DownloadServerApp");
					Array.set(count, b, filecount);
				}
			}
			
			for(int k=0,j=0;k<appname.length&&j<count.length;k++,j++){
				FileCount fileCount=new FileCount(city.getCitynamecn(), appname[k], new Date(), Long.parseLong(count[j]), Long.parseLong(allfilecount));
				fileCountService.save(fileCount);
			}
			
		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.exit(2);

		}  finally {
			// Close this session
			if (sess != null) {
				sess.close();
			}
			// Close the connection
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	@Scheduled(cron = "0 0 8,12,18 * * ?")
	public void start() {
		List<City> lc=cityService.findAll();
		for(City city:lc){
			GetFileCount(city);
		}
	}
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		start();

	}

}
