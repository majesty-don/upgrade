package upgrade.dbmonitor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import upgrade.pojo.DBMonitor;

public class GraphTask implements Callable<DBMonitor> {

	private static final Logger logger = LogManager.getLogger(GraphTask.class);

	private final DBMonitor city;
	private final int firstDay;
	private final int dateCnt;
	private final String path;

	long now;
	long dbMinutes;
	long busMinutes;
	int busCnt;
	String log = "";

	public GraphTask(DBMonitor city, int firstDay, int dateCnt, String path) {
		this.city = city;
		this.firstDay = firstDay;
		this.dateCnt = dateCnt;
		this.path = path;
	}

	private static final DateFormat YYYYMMDD_HHMM = new SimpleDateFormat(
			"yyyy.MM.dd HH:mm");

	@Override
	public DBMonitor call() throws Exception {
		boolean sucess = false;
		long from = System.currentTimeMillis();

		logger.info(city.getCityNameCn() + " ----开始生成绘制图片---- ");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			conn = DBSource.getConnection(city);

			final long toDay = System.currentTimeMillis()- TimeUnit.DAYS.toMillis(firstDay);
			for (int i = 0; i < dateCnt; i++) {
				Date curDate = new Date(toDay - TimeUnit.DAYS.toMillis(i));

				String gpsTable = "NETPACK_GPS".equals(city.getGpsTableType()) ? "NETPACK_GPS PARTITION (P_GPS_"+ YYYYHHDD(new Date()) + ")": "NETPACK_GPS_" + YYYYHHDD(new Date());
				String fileName = new SimpleDateFormat("yyMMdd").format(curDate) + "" + city.getCityNameEn() + ".png";

				logger.debug("Load File Name:" + fileName);

				String nowStr = new SimpleDateFormat("yyyy.MM.dd").format(curDate);
				if (Math.abs(System.currentTimeMillis() - curDate.getTime()) < TimeUnit.HOURS.toMillis(2)) {// if today
					nowStr = YYYYMMDD_HHMM.format(curDate);
				}
				Map<Integer, int[]> map = Collections.emptyMap();
				try {
					stmt = conn.prepareStatement("SELECT SYSDATE DB_NOW, MAX(SVR_TIME) SVR_TIME, COUNT(DISTINCT BUS_NO) BUS_CNT FROM EXT_BUS_CURRENT_STATE WHERE SVR_TIME>=SYSDATE-3");
					// try{
					rs = stmt.executeQuery();
					rs.next();
					Timestamp dbCurrentTime = rs.getTimestamp(1);
					Timestamp busActiveTime = rs.getTimestamp(2);
					busCnt = rs.getInt(3);
					now = System.currentTimeMillis();
					dbMinutes = (now - dbCurrentTime.getTime()) / 1000L / 60L;
					if (busActiveTime != null) {// 无记录时可能返回NULL
						busMinutes = (dbCurrentTime.getTime() - busActiveTime.getTime()) / 1000L / 60L;
					}
					// }finally{
					// close(stmt);
					// }

					map = loadDbData(conn, gpsTable);
				} catch (SQLException ex) {
					new Exception(city.getCityNameEn() + " NG:", ex).printStackTrace();// ex.printStackTrace();
					log = YYYYMMDD_HHMM.format(System.currentTimeMillis())+ " " + ex.getLocalizedMessage();// 20140220 不再输出类名 ex.toString();
					break;
				} finally {
					// connection会在下次循环中继续使用，故不再关闭连接
					if (rs != null) {
						rs.close();
						rs = null;
					}
					if (stmt != null) {
						stmt.close();
						stmt = null;
					}
					int minutes = (int) (System.currentTimeMillis() - from) / 1000 / 60;
					if (minutes >= 2) {// 如果用时过长，显示时长
						nowStr = nowStr + " " + minutes + "m";
					}
					BufferedImage image = drawChart(city.getCityNameCn(), map,nowStr);// 增加用时

					File dir = new File(path);
					if (!dir.exists()) {
						dir.mkdir();
					}

					String tmppath = dir.getAbsolutePath();

					ImageIO.write(image, "png", new File(tmppath, fileName));// 将文件保存到指定的路�?

					logger.debug("finished: " + fileName);
				}
			}
			sucess = true;
		} catch (Exception ex) {
			new Exception(city.getCityNameEn() + " NG:" + ex.toString(), ex).printStackTrace();
		} finally {
			// DBSource.close(conn);//同时会关闭连接
			logger.debug(city.getCityNameEn() + (sucess ? " OK" : "NG") + " "+ ((System.currentTimeMillis() - from) / 1000)+ " seconds.");
		}
		return city;
	}

	private final static int maxWeiShu = 10;
	private final static Color[] lineColors = new Color[] {
			new Color(0x99, 0xFF, 0x99), Color.BLACK, Color.BLUE };
	private final static int[] INIT_ZERO = new int[maxWeiShu];

	private static Map<Integer, int[]> loadDbData(Connection conn,String gpsTableAndWhere) throws SQLException {

		// INIT
		Map<Integer, int[]> map = new TreeMap<>();
		Arrays.fill(INIT_ZERO, -1);
		for (int i = 0; i < 24 * 60; i++) {
			map.put(i, INIT_ZERO);
		}

		Statement statement = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT TO_CHAR(DATETIME, 'HH24:MI') DT, COUNT(1)  GPSCNT, COUNT(DISTINCT BUS_NO) BUSCNT FROM "
					+ gpsTableAndWhere
					+ " GROUP BY TO_CHAR(DATETIME, 'HH24:MI')ORDER BY DT ";
			logger.debug("load data sql:" + sql);
			statement = conn.createStatement();
			statement.setQueryTimeout(60 * 10);// 20131115add

			rs = statement.executeQuery(sql);
			while (rs.next()) {
				String timeStr = rs.getString(1);
				String[] timeStrs = timeStr.split(":");
				int serial = Integer.parseInt(timeStrs[0]) * 60+ Integer.parseInt(timeStrs[1]);
				int gpsPackCnt = rs.getInt(2);// gpsCnt
				int busCnt = rs.getInt(3);// busCnt

				map.put(serial, new int[] { busCnt * 4, gpsPackCnt, busCnt });
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
		}
		return map;
	}

	private static int lineHeight(Graphics2D g2d) {
		return g2d.getFontMetrics().getHeight();// return
												// g2d.getFont().getSize()*4/3;
	}

	private BufferedImage drawChart(String city, Map<Integer, int[]> map,String dtStr) {
		int[] maxVals = new int[maxWeiShu];
		int maxVal = 400;
		for (int[] vals : map.values()) {
			for (int i = 0; i < vals.length; i++) {
				if (maxVals[i] < vals[i]) {
					maxVals[i] = vals[i];
				}
			}
		}

		for (Integer val : maxVals) {
			if (maxVal < val) {
				maxVal = val;
			}
		}

		// /////////////////////////////////////////
		int width = 1200;
		int height = 400;

		BufferedImage image = new TextAreaBufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setBackground(Color.WHITE);
		g2d.clearRect(0, 0, width, height);
		// 保存文件
		int lastLinePos = 0;
		Stroke normalLineStroke = new BasicStroke(1), strongLineStroke = new BasicStroke(1.5f);// new BasicStroke(2);
		for (int val = 0; val < maxVal; val += 100) {
			int y = val * height / maxVal;

			if (y - lastLinePos > 16) {
				g2d.setColor(Color.GRAY);
				g2d.setStroke(normalLineStroke);
				g2d.drawLine(0, height - y, width, height - y);

				g2d.setColor(Color.BLACK);
				g2d.drawString("" + val, 0, height - y);
				g2d.drawString("" + val, width / 2, height - y);
				lastLinePos = y;
			}
		}

		for (int hour = 0; hour < 24; hour++) {
			g2d.setStroke(hour % 6 == 0 ? strongLineStroke : normalLineStroke);
			g2d.setColor(Color.GRAY);
			g2d.drawLine(hour * width / 24, 0, hour * width / 24, height);
			g2d.setColor(Color.BLACK);
			g2d.drawString("" + hour, hour * width / 24, height);
		}

		// //////////////////////////////////
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(1.5f));

		int[] lastVal = new int[maxWeiShu];
		for (Map.Entry<Integer, int[]> entry : map.entrySet()) {
			int timeSerial = entry.getKey();
			int[] vs = entry.getValue();
			for (int i = 0; i < Math.min(vs.length, lastVal.length); i++) {
				g2d.setColor(lineColors[Math.min(i, lineColors.length - 1)]);

				@SuppressWarnings("unused")
				int x, y;

				g2d.drawLine(
						(timeSerial - 1) * width / (24 * 60), 
						height- lastVal[i] * height / maxVal, 
						x = timeSerial * width/ (24 * 60), 
						y = height - vs[i] * height / maxVal);

			}

			lastVal = Arrays.copyOf(vs, vs.length);
		}

		// 文字输出
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(5));

		int pos = 0;

		g2d.setFont(new Font(null, Font.PLAIN, 2 * 16));
		g2d.drawString(dtStr, 16, pos);
		pos += lineHeight(g2d);

		g2d.setFont(new Font("黑体", Font.BOLD, 4 * 16));
		g2d.drawString(city, 16, pos);
		pos += lineHeight(g2d);

		g2d.setFont(new Font("Times New Roman", Font.PLAIN, 1 * 16));
		for (int i = 0; i < lineColors.length; i++) {
			g2d.setColor(lineColors[i]);
			g2d.drawString("line" + i + "  max: " + maxVals[i], 16 * 4, pos);
			pos += lineHeight(g2d);
		}
		//
		g2d.setColor(Color.BLACK);
		StringBuilder sb = new StringBuilder();
		sb.append(YYYYMMDD_HHMM.format(now)).append(" bus:").append(busCnt);
		if (Math.abs(dbMinutes) > 3) {// 如果数据库时间不准则输出
			sb.append(" db:").append(dbMinutes).append("m");
		}
		if (Math.abs(busMinutes) > 5) {// 如果车辆超过5分钟没有上传数据，则输出
			sb.append("m, busActive: ").append(busMinutes).append("m");
		}
		g2d.drawString(sb.toString(), 16, pos);
		pos += lineHeight(g2d);

		//
		g2d.setColor(Color.RED);
		g2d.drawString(log, 16, pos);

		g2d.dispose();

		return image;
	}

	private static String YYYYHHDD(Date date) {
		return new SimpleDateFormat("yyyyMMdd").format(date);
	}

}
