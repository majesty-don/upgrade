package upgrade.pojo;

public class DBMonitor implements Comparable<DBMonitor> {

	private Long id;
	private String cityNameCn;
	private String cityNameEn;
	private String dburl;
	private String dbusername;
	private String dbpassword;
	private String gpsTableType;

	public DBMonitor() {
		// TODO Auto-generated constructor stub
	}

	public DBMonitor(String cityNameCn, String cityNameEn, String dburl,
			String dbusername, String dbpassword, String gpsTableType) {
		this.cityNameCn = cityNameCn;
		this.cityNameEn = cityNameEn;
		this.dburl = dburl;
		this.dbusername = dbusername;
		this.dbpassword = dbpassword;
		this.gpsTableType = gpsTableType;
	}
	
	

	public DBMonitor(Long id, String cityNameCn, String cityNameEn,
			String dburl, String dbusername, String dbpassword,
			String gpsTableType) {
		super();
		this.id = id;
		this.cityNameCn = cityNameCn;
		this.cityNameEn = cityNameEn;
		this.dburl = dburl;
		this.dbusername = dbusername;
		this.dbpassword = dbpassword;
		this.gpsTableType = gpsTableType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCityNameCn() {
		return cityNameCn;
	}

	public void setCityNameCn(String cityNameCn) {
		this.cityNameCn = cityNameCn;
	}

	public String getCityNameEn() {
		return cityNameEn;
	}

	public void setCityNameEn(String cityNameEn) {
		this.cityNameEn = cityNameEn;
	}

	public String getDburl() {
		return dburl;
	}

	public void setDburl(String dburl) {
		this.dburl = dburl;
	}

	public String getDbusername() {
		return dbusername;
	}

	public void setDbusername(String dbusername) {
		this.dbusername = dbusername;
	}

	public String getDbpassword() {
		return dbpassword;
	}

	public void setDbpassword(String dbpassword) {
		this.dbpassword = dbpassword;
	}

	public String getGpsTableType() {
		return gpsTableType;
	}

	public void setGpsTableType(String gpsTableType) {
		this.gpsTableType = gpsTableType;
	}

	@Override
	public int compareTo(DBMonitor o) {
		// TODO Auto-generated method stub
		return cityNameEn.compareTo(o.cityNameEn);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "城市中文名称" + this.cityNameCn 
				+ ",城市英文名称" + this.cityNameEn
				+ ",GPS表类型：" + this.gpsTableType 
				+ ",数据库参数：" + this.dburl + ","+ this.dbusername + "," + this.dbpassword;
	}

}
