package upgrade.web;

import java.util.ArrayList;
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

/**
 * 文件数控制器
 * @author TCPS
 *
 */
@Controller
@RequestMapping("/count")
public class FileCountController{
	
	private static final Logger logger=LogManager.getLogger(DBMonitorController.class);
	
	@Resource(name="fileCountService")
	private FileCountService fileCountService;
	
	
	@RequestMapping("/toCrawl")
	public ModelAndView toCrawl(HttpServletRequest request){
		Map<String, List<FileCount>> map=new HashMap<String, List<FileCount>>();
		
		List<FileCount> cityNameAndMax=fileCountService.getNameAndMax();
		logger.info("城市名称和最大文件数："+JSON.toJSONString(cityNameAndMax));
		
		List<FileCount> all=fileCountService.findAll();
		for(FileCount name:cityNameAndMax){
			List<FileCount> list=new ArrayList<>();
			String cityname=name.getCityname();
			for(FileCount item:all){				
				if(cityname.equals(item.getCityname())){
					list.add(item);
				}
			}
			map.put(cityname+"  "+name.getFilemaxcount(), list);
		}
		request.setAttribute("map", map);
		return new ModelAndView("monitor/file_cnt");
	}
}