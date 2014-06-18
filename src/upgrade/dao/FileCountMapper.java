package upgrade.dao;

import java.util.List;

import upgrade.common.OracleDao;
import upgrade.pojo.FileCount;

@OracleDao(value="fileCountMapper")
public interface FileCountMapper {
	/**
	 * 查询出所有的记录
	 * @return
	 */
	public List<FileCount> findAll();
	/**
	 * 查询出城市名称和最大的文件数
	 * @return
	 */
	public List<FileCount> findNameAndMax();
	
	public void insert(FileCount fileCount);
}
