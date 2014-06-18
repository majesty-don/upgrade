package upgrade.service;

import java.util.List;


import upgrade.pojo.FileCount;

public interface FileCountService {
	public List<FileCount> findAll();
	public List<FileCount> getNameAndMax();
	public void save(FileCount fileCount);
}
