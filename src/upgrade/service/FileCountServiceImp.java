package upgrade.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upgrade.dao.FileCountMapper;
import upgrade.pojo.FileCount;


@Service("fileCountService")
public class FileCountServiceImp implements FileCountService {

	@Resource(name="fileCountMapper")
	@Autowired
	private FileCountMapper fileCountMapper;
	
	@Override
	public List<FileCount> findAll() {
		// TODO Auto-generated method stub
		return fileCountMapper.findAll();
	}

	@Override
	public List<FileCount> getNameAndMax() {
		// TODO Auto-generated method stub
		return fileCountMapper.findNameAndMax();
	}

	@Override
	public void save(FileCount fileCount) {
		fileCountMapper.insert(fileCount);
		
	}

}
