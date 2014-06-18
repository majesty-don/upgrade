package upgrade.dao;

import java.util.List;

import upgrade.common.MyBatisDao;
import upgrade.pojo.City;

@MyBatisDao(value="cityMapper")
public interface CityMapper {
	
	/**
	 * 没有任何判断，直接保存
	 * @param city
	 */
	public void add(City city);
	
	/**
	 * 判断字段是否为空的保存
	 * @param city
	 */
	public void save(City city);
	
	/**
	 * 依据ID删除记录
	 * @param id
	 */
	public void delete(long id);
	
	/**
	 * 查询全部记录
	 * @return
	 */
	public List<City> findAll();
	
	/**
	 * 依据ID查询一条记录
	 * @param id
	 * @return
	 */
	public City find(long id);
	
	/**
	 * 修改一条记录
	 * @param city
	 */
	public void modify(City city);
	
	/**
	 * 查询列表
	 * @param City
	 * @return
	 */
	public List<City> search(City City);
	
	/**
	 * 批量删除记录
	 */
	public void deleteList(List<Long> ids);
	
	public List<City> findList(List<Long> ids);
	
	
}
