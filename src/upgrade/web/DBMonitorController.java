package upgrade.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import upgrade.pojo.DBMonitor;
import upgrade.service.DBMonitorService;

@Controller
@RequestMapping("/dbmonitor")
public class DBMonitorController{
	private static final Logger logger=LogManager.getLogger(DBMonitorController.class);
	
	@Resource(name="dbMonitorService")
	private DBMonitorService dbMonitorService;
	
	@RequestMapping("/toList")
	public ModelAndView toList(){		
		return new ModelAndView("db/list");
	}
	
	@RequestMapping("/getList")
	public ModelAndView getList(){
		List<DBMonitor> list=dbMonitorService.findAll();
		logger.info("数据库信息："+JSON.toJSONString(list));
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("total", list.size());
		map.put("rows", list);
		logger.debug("查询出的全部文件信息:"+JSON.toJSONString(map));
		return new ModelAndView("db/list",map);
	}
	
	@RequestMapping("/add")
	public String add(ModelMap mm,DBMonitor dbMonitor){
		logger.debug(dbMonitor.toString());
		dbMonitorService.save(dbMonitor);
		mm.addAttribute("failure", false);
		return "forward:toList";
	}
	
	@RequestMapping("/modify")
	public String modify(ModelMap mm,DBMonitor dbMonitor){
		logger.debug(dbMonitor.toString());
		dbMonitorService.modify(dbMonitor);
		mm.addAttribute("failure", false);
		return "forward:toList";
	}
	
	@RequestMapping("/delete")
	public String delete(ModelMap mm,long id){
		logger.debug("id:"+id);
		dbMonitorService.remove(id);
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
		dbMonitorService.removeList(id_list);
		mm.addAttribute("failure", false);
		return "forward:toList";
	}
	
	private String formatDate(Date date){
		return new SimpleDateFormat("yyMMdd").format(date);
	}
	
	@RequestMapping("/pic")
	public ModelAndView toPic(HttpServletRequest request){
		List<DBMonitor> ldb=dbMonitorService.findAll();
		request.setAttribute("filedate", formatDate(new Date()));
		request.setAttribute("list", ldb);
		return new ModelAndView("monitor/db");
	}
	
}