package upgrade.pojo;


public class FileInfo {
	private Long id;
	private String filename;
	private String filepath;
	public FileInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public FileInfo(String filename, String filepath) {
		this.filename = filename;
		this.filepath = filepath;
	}

	public FileInfo(Long id, String filename, String filepath) {
		this.id = id;
		this.filename = filename;
		this.filepath = filepath;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
}
