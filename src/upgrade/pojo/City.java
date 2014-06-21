package upgrade.pojo;

public class City implements Comparable<City> {
	private Long id;
	private String citynamecn;//城市名称
	private String citynameen;//城市拼音缩写
	
	private String host;//网关主机
	private Integer port22;//SSH访问端口
	private Integer port10;//WEB访问端口
	private Integer port15;//Receiver监听端口
	private Integer port16;//BusServer监听端口
	private Integer port17;//升级端口
	
	private String username;//SSH访问的用户名
	private String password;//SSH访问的密码
	
	private String dburl;//数据库监听
	private String dbusername;//数据库普通用户帐号
	private String dbpassword;//数据库普通用户密码
	private String dbadminusername;//数据库管理员帐号
	private String dbadminpassword;//数据库管理员密码
	private String gpsTableType;//GPS表类型
	

	// private boolean checked=false;

	public City() {
		// TODO Auto-generated constructor stub
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getCitynamecn() {
		return citynamecn;
	}


	public void setCitynamecn(String citynamecn) {
		this.citynamecn = citynamecn;
	}


	public String getCitynameen() {
		return citynameen;
	}


	public void setCitynameen(String citynameen) {
		this.citynameen = citynameen;
	}

	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public Integer getPort22() {
		return port22;
	}


	public void setPort22(Integer port22) {
		this.port22 = port22;
	}


	public Integer getPort10() {
		return port10;
	}


	public void setPort10(Integer port10) {
		this.port10 = port10;
	}


	public Integer getPort15() {
		return port15;
	}


	public void setPort15(Integer port15) {
		this.port15 = port15;
	}


	public Integer getPort16() {
		return port16;
	}


	public void setPort16(Integer port16) {
		this.port16 = port16;
	}


	public Integer getPort17() {
		return port17;
	}


	public void setPort17(Integer port17) {
		this.port17 = port17;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
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


	public String getDbadminusername() {
		return dbadminusername;
	}


	public void setDbadminusername(String dbadminusername) {
		this.dbadminusername = dbadminusername;
	}


	public String getDbadminpassword() {
		return dbadminpassword;
	}


	public void setDbadminpassword(String dbadminpassword) {
		this.dbadminpassword = dbadminpassword;
	}


	public String getGpsTableType() {
		return gpsTableType;
	}


	public void setGpsTableType(String gpsTableType) {
		this.gpsTableType = gpsTableType;
	}


	@Override
	public int compareTo(City o) {
		// TODO Auto-generated method stub
		return citynameen.compareTo(o.citynameen);
	}
}
