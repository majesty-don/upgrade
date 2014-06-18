<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<title>plupload</title> 
<!-- 配置界面上的css --> 
<link rel="stylesheet" href="<c:url value="/plupload/js/jquery.plupload.queue/css/jquery.plupload.queue.css"/>" type="text/css">
<script type="text/javascript" src="<c:url value="/easyui/jquery.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/plupload/js/plupload.full.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/plupload/js/jquery.plupload.queue/jquery.plupload.queue.js"/>"></script> 
 
<!-- 国际化中文支持 --> 
<script type="text/javascript" src="<c:url value="/plupload/js/i18n/zh_CN.js"/>"></script> 

<script type="text/javascript"> 
/* Convert divs to queue widgets when the DOM is ready */ 
$(function(){ 
    function plupload(){ 
        $("#uploader").pluploadQueue({ 
            // General settings 
            runtimes : 'html5,gears,browserplus,silverlight,flash,html4', 
            url : 'plupload.html', 
            max_file_size : '100mb', 
            unique_names: true, 
            chunk_size: '10mb', 
            // Specify what files to browse for 
            filters : [ 
                {title: "Image files", extensions: "jpg,gif,png"}, 
                {title: "Zip files", extensions: "zip,jar"} 
            ], 
            resize: {width: 640, height: 480, quality: 90}, 
            // 参数 
            preinit : {
				UploadFile: function(up, file) {
					up.settings.multipart_params = {name: file.name};
				}
			} 
        }); 
        //$('#uploader').plupload('notify', 'info', "This might be obvious, but you need to click 'Add Files' to add some files.");
    } 
    
    plupload();
}); 
</script> 
<div style="width:750px; margin:0 auto">     
     <div id="uploader"> 
         <p align="center" style="margin:150px auto 100px auto;font-size:15pt">您的浏览器未安装 Flash, Silverlight, Gears, BrowserPlus 或者支持 HTML5 .</p> 
     </div>  
</div> 