package upgrade.dao;

import java.util.List;

import upgrade.pojo.DBMonitor;

public interface DBMonitorMapper {
	public List<DBMonitor> select();
	public void insert(DBMonitor dbCity);
	
	public void update(DBMonitor dbCity);
	
	public void delete(long id);
	
	public void deleteList(List<Long> ids);
}
