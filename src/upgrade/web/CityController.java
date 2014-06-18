package upgrade.web;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;


import upgrade.netty.http.send.Send;
import upgrade.netty.util.FileTool;
import upgrade.pojo.City;
import upgrade.pojo.CityQuery;
import upgrade.service.CityService;

@Controller
@RequestMapping("/city")
public class CityController  {
	private static final Logger logger=Logger.getLogger(CityController.class);
	
	private final ExecutorService pool=Executors.newFixedThreadPool(10);
	
	@Resource(name="cityService")
	private CityService cityService;

	public CityService getCityService() {
		return cityService;
	}
		
	public void setCityService(CityService cityService) {
		this.cityService = cityService;
	}
	
	@RequestMapping("/toList")
	public ModelAndView toList(){		
		return new ModelAndView("city/list");
	}
	
	@RequestMapping("/getList")
	public ModelAndView getList(){	
		List<City> lc=cityService.findAll();
		Map<String,Object> map=new HashMap<>();
		map.put("total", lc.size());
		map.put("rows", lc);
		logger.debug("全部城市信息�?"+JSON.toJSONString(map));
		return new ModelAndView("city/list", map);
	}
	
	@RequestMapping("/add")
	public String add(ModelMap mm,City city){
		logger.debug(city.toString());
		cityService.insert(city);
		mm.addAttribute("failure", false);
		return "forward:toList";
	}
	
	@RequestMapping("/modify")
	public String modify(ModelMap mm,City city){
		logger.debug(city.toString());
		cityService.update(city);
		mm.addAttribute("failure", false);
		return "forward:toList";
	}
	
	@RequestMapping("/delete")
	public String delete(ModelMap mm,long id){
		logger.debug("id:"+id);
		cityService.deleteByID(id);
		mm.addAttribute("failure", false);
		return "forward:toList";
	}
	
	@RequestMapping("/deleteList")
	public String deleteList(HttpServletRequest request,ModelMap mm){
		String ids=request.getParameter("ids");
		String[] idArray=ids.split(",");
		List<Long> id_list=new ArrayList<>();
		for(String id:idArray){
			id_list.add(Long.parseLong(id));
		}
		logger.debug("ids:"+JSON.toJSONString(id_list));
		cityService.deleteListByIds(id_list);
		mm.addAttribute("failure", false);
		return "forward:toList";
	}
	
	
	@RequestMapping("/search")
	public ModelAndView search(CityQuery cq) throws UnsupportedEncodingException{

		logger.debug(cq.toString());
	
		cq.init();
		
		String params[]=cq.getParams();		
		Object values[]=cq.getValues();
		City city = new City();
		if (params != null && values != null && values.length > 0 
				&& params.length > 0 && params.length == values.length){
			
			for (int i = 0; i < params.length; i++) {
				if("name".equals(params[i].toString())){
					city.setName((String)values[i]);
				}
				if("remote".equals(params[i].toString())){
					city.setRemote((String)values[i]);
				}
				if("port22".equals(params[i].toString())){
					city.setPort22(Integer.parseInt((String)values[i]));
				}
				if("port10".equals(params[i].toString())){
					city.setPort10(Integer.parseInt((String)values[i]));
				}
				if("port15".equals(params[i].toString())){
					city.setPort15(Integer.parseInt((String)values[i]));
				}
				if("port16".equals(params[i].toString())){
					city.setPort16(Integer.parseInt((String)values[i]));
				}
				
			}
	    }
		
		List<City> lc=cityService.search(city);
		Map<String, Object> map=new HashMap<>();
		map.put("total", lc.size());
		map.put("rows", lc);
		
		logger.debug("查询出的城市�?"+JSON.toJSONString(map));
		return new ModelAndView("city/list",map);
	}
	
	@RequestMapping("/9010")
	public ModelAndView toWeb(HttpServletRequest request){
		List<City> lc=cityService.findAll();
		request.setAttribute("list", lc);
		return new ModelAndView("monitor/9010");
	}
	
	@RequestMapping("/9015")
	public ModelAndView toRecv(HttpServletRequest request){
		List<City> lc=cityService.findAll();
		request.setAttribute("list", lc);
		return new ModelAndView("monitor/9015");
	}
	
	@RequestMapping("/9016")
	public ModelAndView toServ(HttpServletRequest request){
		List<City> lc=cityService.findAll();
		request.setAttribute("list", lc);
		return new ModelAndView("monitor/9016");
	}
	
	@RequestMapping("/recv")
	public String upRecv(HttpServletRequest request,ModelMap mm){
		String ids=request.getParameter("ids");
		String[] idArray=ids.split(",");
		List<Long> id_list=new ArrayList<>();
		for(String id:idArray){
			id_list.add(Long.parseLong(id));
		}
		List<City> lc=cityService.getCitysByIds(id_list);
		logger.info("升级城市信息�?"+JSON.toJSONString(lc));
		
		//以下使用多线�?      因为会同时启动多个客户端连接多个服务�?  
		for(City city:lc){
			String uri="http://"+city.getRemote()+":"+city.getPort17();
			String filepath=FileTool.getFilePath("send", "TcpsGisReceiver.jar");
			Send send=new Send(uri,filepath);
			pool.execute(send);	
		}
		mm.addAttribute("failure", false);
		
		return "forward:toList";
	}
	
	@RequestMapping("/serv")
	public String upServ(HttpServletRequest request,ModelMap mm){
		String ids=request.getParameter("ids");
		String[] idArray=ids.split(",");
		List<Long> id_list=new ArrayList<>();
		for(String id:idArray){
			id_list.add(Long.parseLong(id));
		}
		List<City> lc=cityService.getCitysByIds(id_list);
		logger.info("升级城市信息�?"+JSON.toJSONString(lc));
		
		for(City city:lc){
			String uri="http://"+city.getRemote()+":"+city.getPort17();
			String filepath=FileTool.getFilePath("send", "BusServer.jar");
			Send send=new Send(uri,filepath);
			pool.execute(send);
		}
		mm.addAttribute("failure", false);
		return "forward:toList";
	}
	
}
