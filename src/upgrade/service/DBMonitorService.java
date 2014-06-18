package upgrade.service;

import java.util.List;

import upgrade.pojo.DBMonitor;

public interface DBMonitorService {
	public List<DBMonitor> findAll();
	
	public void save(DBMonitor dbCity);
	
	public void modify(DBMonitor dbCity);
	
	public void remove(long id);
	
	public void removeList(List<Long> ids);
}
