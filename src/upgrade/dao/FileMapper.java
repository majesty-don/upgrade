package upgrade.dao;

import java.util.List;

import upgrade.common.MyBatisDao;
import upgrade.pojo.FileInfo;

@MyBatisDao(value="fileMapper")
public interface FileMapper {
	
	public List<FileInfo> findAll();
	
	public void save(FileInfo fileInfo);
	
	/**
	 * 依据ID删除记录
	 * @param id
	 */
	public void delete(long id);
	
	/**
	 * 批量删除记录
	 */
	public void deleteList(List<Long> ids);
}
