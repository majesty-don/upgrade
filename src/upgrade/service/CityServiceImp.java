package upgrade.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import upgrade.dao.CityMapper;
import upgrade.pojo.City;

@Service("cityService")
public class CityServiceImp implements CityService {
	
	@Resource(name="cityMapper")
	private CityMapper cityMapper;
	
	public CityMapper getCityMapper() {
		return cityMapper;
	}
	
	public void setCityMapper(CityMapper cityMapper) {
		this.cityMapper = cityMapper;
	}

	@Override
	public void insert(City city) {
		cityMapper.add(city);
		
	}

	@Override
	public void deleteByID(long id) {
		cityMapper.delete(id);
		
	}

	@Override
	public List<City> findAll() {
		return cityMapper.findAll();
	}

	@Override
	public void update(City city) {
		cityMapper.modify(city);
		
	}

	@Override
	public City getCityByID(long id) {
		return cityMapper.find(id);
	}

	@Override
	public List<City> search(City city) {
		// TODO Auto-generated method stub
		return cityMapper.search(city);
	}

	@Override
	public void deleteListByIds(List<Long> ids) {
		cityMapper.deleteList(ids);
		
	}

	@Override
	public List<City> getCitysByIds(List<Long> ids) {
		// TODO Auto-generated method stub
		return cityMapper.findList(ids);
	}

	

}
