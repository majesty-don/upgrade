package upgrade.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import upgrade.dao.FileMapper;
import upgrade.pojo.FileInfo;

@Service("fileService")
public class FileServiceImp implements FileService {

	@Resource(name="fileMapper")
	private FileMapper fileMapper;
	
	public FileMapper getFileMapper() {
		return fileMapper;
	}

	public void setFileMapper(FileMapper fileMapper) {
		this.fileMapper = fileMapper;
	}

	@Override
	public List<FileInfo> findAll() {
		// TODO Auto-generated method stub
		return fileMapper.findAll();
	}

	@Override
	public void insert(FileInfo fileInfo) {
		fileMapper.save(fileInfo);
		
	}

	@Override
	public void deleteByID(long id) {
		fileMapper.delete(id);
		
	}

	@Override
	public void deleteListByIds(List<Long> ids) {
		fileMapper.deleteList(ids);
		
	}

}
