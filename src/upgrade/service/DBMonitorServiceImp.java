package upgrade.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upgrade.dao.DBMonitorMapper;
import upgrade.pojo.DBMonitor;

@Service("dbMonitorService")
public class DBMonitorServiceImp implements DBMonitorService {

	@Autowired
	private DBMonitorMapper dbMonitorMapper;
	
	@Override
	public List<DBMonitor> findAll() {
		// TODO Auto-generated method stub
		return dbMonitorMapper.select();
	}

	@Override
	public void save(DBMonitor dbCity) {
		dbMonitorMapper.insert(dbCity);

	}

	@Override
	public void modify(DBMonitor dbCity) {
		dbMonitorMapper.update(dbCity);

	}

	@Override
	public void remove(long id) {
		dbMonitorMapper.delete(id);

	}

	@Override
	public void removeList(List<Long> ids) {
		dbMonitorMapper.deleteList(ids);

	}

}
