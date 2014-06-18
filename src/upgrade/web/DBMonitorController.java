package upgrade.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import upgrade.pojo.FileCount;
import upgrade.service.FileCountService;

@Controller
@RequestMapping("/dbmonitor")
public class DBMonitorController{
	private static final Logger logger=LogManager.getLogger(DBMonitorController.class);
	
	@Resource(name="fileCountService")
	private FileCountService fileCountService;
	
	@RequestMapping("/toList")
	public ModelAndView toList(){		
		return new ModelAndView("city/list");
	}
	
	@RequestMapping("/toFileCount")
	public ModelAndView toFileCount(HttpServletRequest request){
		List<FileCount> lc=fileCountService.getNameAndMax();
		logger.info("文件数信息："+JSON.toJSONString(lc));
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("lc", lc);
		return new ModelAndView("monitor/file_cnt");
	}
}