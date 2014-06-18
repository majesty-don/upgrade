package upgrade.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import upgrade.pojo.FileInfo;
import upgrade.service.FileService;

/**
 * 上传文件的控制器
 * @author TCPS
 *
 */
@Controller
@RequestMapping("/file")
public class FileController {
	private static final Logger logger=LogManager.getLogger(FileController.class);
	private static final int BUFFER_SIZE = 100 * 1024;
	
	@Resource(name="fileService")
	private FileService fileService;

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}
	
	@RequestMapping("/toList")
	public ModelAndView toList(){
		
		return new ModelAndView("file/list");
	}
	
	@RequestMapping("/getList")
	public ModelAndView getList(){
		List<FileInfo> lf=fileService.findAll();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("total", lf.size());
		map.put("rows", lf);
		logger.debug("查询出的全部文件信息:"+JSON.toJSONString(map));
		return new ModelAndView("file/list",map);
	}
	
	public void save(FileInfo fileInfo){
		fileService.insert(fileInfo);
	}
	
	@RequestMapping("/toPlupload")
	public ModelAndView toPlupload(){
		return new ModelAndView("file/upload");
	}
	
	/**
	 * 使用plupload上传文件
	 * @param file		文件对象
	 * @param name		文件名称
	 * @param chunk		数据块序
	 * @param chunks	数据块数
	 * @return
	 */
	@RequestMapping(value="plupload",method=RequestMethod.POST)	
	public String plupload(@RequestParam MultipartFile file, HttpSession session, String name, int chunk, int chunks) {
		logger.debug("File Name:"+name);
		try {
			//检查文件目录，不存在则创建
			String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String relativePath = date+"/";
			String realPath = "E:/upgrade/";//session.getServletContext().getRealPath("");
			logger.debug("real path:"+realPath);
			String filepath=realPath + relativePath;
			logger.debug("filepath:"+filepath);
			File folder = new File(filepath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			
			//目标文件 
			File destFile = new File(folder, name);
			//文件已存在删除旧文件（上传了同名的文件） 
	        if (chunk == 0 && destFile.exists()) {  
	        	destFile.delete();  
	        	destFile = new File(folder, name);
	        }
	        //合成文件
	        appendFile(file.getInputStream(), destFile);  
	        if (chunk == chunks - 1) {  
	            logger.info("上传完成");
	            FileInfo fi=new FileInfo(name, filepath+name);
	            fileService.insert(fi);
	        }else {
	        	logger.info("还剩["+(chunks-1-chunk)+"]个块文件");
	        }
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		return "forward:toPlupload";
	}
	
	private void appendFile(InputStream in, File destFile) {
		OutputStream out = null;
		try {
			// plupload 配置了chunk的时候新上传的文件append到文件末�?
			if (destFile.exists()) {
				out = new BufferedOutputStream(new FileOutputStream(destFile, true), BUFFER_SIZE); 
			} else {
				out = new BufferedOutputStream(new FileOutputStream(destFile),BUFFER_SIZE);
			}
			in = new BufferedInputStream(in, BUFFER_SIZE);
			
			int len = 0;
			byte[] buffer = new byte[BUFFER_SIZE];			
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {		
			try {
				if (null != in) {
					in.close();
				}
				if(null != out){
					out.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	
	@RequestMapping("/delete")
	public String delete(ModelMap mm,long id){
		logger.debug("id:"+id);
		fileService.deleteByID(id);
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
		fileService.deleteListByIds(id_list);
		mm.addAttribute("failure", false);
		return "forward:toList";
	}
	
}
