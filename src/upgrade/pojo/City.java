package upgrade.pojo;

public class City {
	private Long id;
	private String name;//城市名称
	private String remote;//城市IP
	private Integer port22;//SSH访问端口
	private Integer port10;//WEB访问端口
	private Integer port15;
	private Integer port16;
	private Integer port17;//升级端口
	
	private String username;//SSH访问的用户名
	private String password;//SSH访问的密码

	// private boolean checked=false;

	public City() {
		// TODO Auto-generated constructor stub
	}
	
	public City(Long id, String name, String remote, Integer port22,
			Integer port10, Integer port15, Integer port16,Integer port17,String username,String password) {
		this.id = id;
		this.name = name;
		this.remote = remote;
		this.port22 = port22;
		this.port10 = port10;
		this.port15 = port15;
		this.port16 = port16;
		this.port17 = port17;
		this.username=username;
		this.password=password;
	}
	

	public City(String name, String remote, Integer port22, Integer port10,
			Integer port15, Integer port16,Integer port17,String username,String password) {
		this.name = name;
		this.remote = remote;
		this.port22 = port22;
		this.port10 = port10;
		this.port15 = port15;
		this.port16 = port16;
		this.port17 = port17;
		this.username=username;
		this.password=password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
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

	// public boolean isChecked() {
	// return checked;
	// }
	// public void setChecked(boolean checked) {
	// this.checked = checked;
	// }
	@Override
	public String toString() {
		return "ID:" + this.id + ",Name:" + this.name + ",Address:"
				+ this.remote + ":" + this.port10+","+this.port15+","+this.port16+","+this.port22+","+this.port17
				+",username:"+this.getUsername()+",password:"+this.getPassword();
	}
}
