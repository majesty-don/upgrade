package upgrade.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import upgrade.service.FileCountService;

@Controller
@RequestMapping("/count")
public class FileCountController{
	
	private static final Logger logger=LogManager.getLogger(DBMonitorController.class);
	
	@Resource(name="fileCountService")
	private FileCountService fileCountService;
	
	
	@RequestMapping("/todb")
	public ModelAndView toDB(HttpServletRequest request){
		logger.info("");
		return new ModelAndView("monitor/db");
	}
}