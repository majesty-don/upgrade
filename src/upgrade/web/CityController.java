package upgrade.web;


import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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


import upgrade.netty.http.send.HttpSend;
import upgrade.pojo.City;
import upgrade.pojo.CityQuery;
import upgrade.service.CityService;
import upgrade.util.FileTool;

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
		logger.info("全部城市信息："+JSON.toJSONString(map));
		return new ModelAndView("city/list", map);
	}
	
	@RequestMapping("/toUpload")
	public ModelAndView toUpload(){
		return new ModelAndView("file/upload");
	}
	
	@RequestMapping("/toAdd")
	public ModelAndView toAdd(){
		return new ModelAndView("city/add");
	}
	
	@RequestMapping("/add")
	public String add(ModelMap mm,City city){
		logger.info(city.toString());
		cityService.insert(city);
		mm.addAttribute("failure", false);
		return "forward:toAdd";
	}
	
	@RequestMapping("/toEdit")
	public ModelAndView toEdit(HttpServletRequest request,long id){
		City city=cityService.getCityByID(id);
		logger.info("City:"+city.toString());
		request.setAttribute("city", city);
		return new ModelAndView("city/edit");
	}
	
	@RequestMapping("/modify")
	public String modify(ModelMap mm,City city){
		logger.info(city.toString());
		cityService.update(city);
		mm.addAttribute("failure", false);
		return "forward:toList";
	}
	
	@RequestMapping("/delete")
	public String delete(ModelMap mm,long id){
		logger.info("id:"+id);
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
		logger.info("ids:"+JSON.toJSONString(id_list));
		cityService.deleteListByIds(id_list);
		mm.addAttribute("failure", false);
		return "forward:toList";
	}
	
	
	@RequestMapping("/search")
	public ModelAndView search(CityQuery cq) throws UnsupportedEncodingException{

		logger.info("查询关键字："+JSON.toJSONString(cq));
	
		cq.init();
		
		String params[]=cq.getParams();		
		Object values[]=cq.getValues();
		City city = new City();
		if (params != null && values != null && values.length > 0 
				&& params.length > 0 && params.length == values.length){
			
			for (int i = 0; i < params.length; i++) {
				if("citynamecn".equals(params[i].toString())){
					city.setCitynamecn((String)values[i]);
				}
			}
	    }
		
		List<City> lc=cityService.search(city);
		Map<String, Object> map=new HashMap<>();
		map.put("total", lc.size());
		map.put("rows", lc);
		
		logger.info("查询出的城市:"+JSON.toJSONString(map));
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
		logger.info("升级城市信息："+JSON.toJSONString(lc));
		
		//以下使用多线线程      因为会同时启动多个客户端连接多个服务器  
		for(City city:lc){
			String uri="http://"+city.getHost()+":"+city.getPort17();
			String filepath=FileTool.getFilePath("send", "TcpsGisReceiver.jar");
			HttpSend send=new HttpSend(uri,filepath);
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
		logger.info("升级城市信息："+JSON.toJSONString(lc));
		
		for(City city:lc){
			String uri="http://"+city.getHost()+":"+city.getPort17();
			String filepath=FileTool.getFilePath("send", "BusServer.jar");
			HttpSend send=new HttpSend(uri,filepath);
			pool.execute(send);
		}
		mm.addAttribute("failure", false);
		return "forward:toList";
	}
	
	private String formatDate(Date date){
		return new SimpleDateFormat("yyMMdd").format(date);
	}
	
	@RequestMapping("/pic")
	public ModelAndView toPic(HttpServletRequest request){
		List<City> ldb=cityService.getDBs();
		request.setAttribute("filedate", formatDate(new Date()));
		request.setAttribute("list", ldb);
		return new ModelAndView("monitor/db");
	}
	
}
