package upgrade.service;

import java.util.List;

import upgrade.pojo.City;

public interface CityService {
	
	public void insert(City city);

	public void deleteByID(long id);
	
	public void deleteListByIds(List<Long> ids);

	public List<City> findAll();

	public void update(City city);
	
	public City getCityByID(long id);
	
	public List<City> search(City city);
	
	public List<City> getCitysByIds(List<Long> ids);
}
