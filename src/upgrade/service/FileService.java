package upgrade.service;

import java.util.List;


import upgrade.pojo.FileInfo;


public interface FileService {

	public List<FileInfo> findAll();
	
	public void insert(FileInfo fileInfo);
	
	public void deleteByID(long id);
	
	public void deleteListByIds(List<Long> ids);
}
