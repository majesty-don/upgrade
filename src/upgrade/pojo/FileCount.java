package upgrade.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileCount {
	private String cityname;
	private String appname;
	private Date   datetime;
	private long   filecount;
	private long   filemaxcount;
	
	public FileCount() {
		// TODO Auto-generated constructor stub
	}
	
	public FileCount(String cityname, String appname, Date datetime,
			long filecount, long filemaxcount) {
		this.cityname = cityname;
		this.appname = appname;
		this.datetime = datetime;
		this.filecount = filecount;
		this.filemaxcount = filemaxcount;
	}



	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	public long getFilecount() {
		return filecount;
	}
	public void setFilecount(long filecount) {
		this.filecount = filecount;
	}
	
	public long getFilemaxcount() {
		return filemaxcount;
	}

	public void setFilemaxcount(long filemaxcount) {
		this.filemaxcount = filemaxcount;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "City Name:"+this.getCityname()+
				",App Name:"+this.getAppname()+
				",Date:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getDatetime())+
				",File Count:"+this.getFilecount()+
				",Max File Count:"+this.getFilemaxcount();
	}
}
