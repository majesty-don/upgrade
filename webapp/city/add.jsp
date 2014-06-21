<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加城市信息</title>
<style type="text/css">
a:link {
	text-decoration: none;
	color: blue
}
a:active {
	text-decoration: blink
}
a:hover {
	text-decoration: underline;
	color: red
}
a:visited {
	text-decoration: none;
	color: green
}
body{
	font-family: Arial,宋体;
	font-size:12px
}
</style>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/easyui/themes/gray/easyui.css"/>">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/easyui/themes/icon.css"/>">
<script type="text/javascript"
	src="<c:url value="/easyui/jquery.min.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/easyui/jquery.easyui.min.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/easyui/jquery-easyui/ext-for-framework.js.jsp"/>"></script>
<script type="text/javascript"
	src="<c:url value="/easyui/plugins/jquery/jquery.form.js"/>"></script>

<script type="text/javascript">
 	function initForm() {
		$("#add").form(
			{
			 url : "<c:url value='/city/add.html'/>",
			 onSubmit : function() {				
				return $(this).form('validate');
			 },
			 success : function(data) {								
				if (data.failure && data.failure == true) {
					$.messager.alert('错误', "保存失败！",'error');
				} else {
					$.messager.alert('提示','保存成功！','info',function() {						
						parent.close('add_dialog');//调用父窗口的close方法
					});
				}								
			 }
		});
	}	
 	
 	$(document).ready(function() { 
		initForm();
	});
 	
	function submitForm(){
		$('#add').form('submit');
		return;
	}
	function clearForm(){
		$('#add').form('clear');
		return;
	}
</script>	

<body onload="initForm()">
	<div style="margin:auto;">
		<form id="add" method="post" >
			<div style="margin:auto">
			<table align="center" style="width: 700px; height: 300px; margin-top: 15px;" border="0">
				<tr>
					<td align="right" style="width: 120px">城市名称：</td>
					<td valign="middle" style="width: 300px">
						<input  class="easyui-validatebox" id="citynamecn" name="citynamecn" type="text" style="width: 280px" />
					</td>
				</tr>
				<tr>
					<td align="right" style="width: 120px">城市拼音：</td>
					<td valign="middle" style="width: 300px">
						<input  class="easyui-validatebox" id="cityNameEn" name="citynameen" type="text" style="width: 280px" />
					</td>
				</tr>
				<tr>
					<td align="right" style="width: 120px">网关地址：</td>
					<td valign="middle" style="width: 300px">
						<input  class="easyui-validatebox" id="host" name="host" type="text" style="width: 280px" />
					</td>
				</tr>
				<tr>
					<td align="right" style="width: 120px">网关端口：</td>
					<td valign="middle" style="width: 300px">
					<input class="easyui-numberbox" id="port22" name="port22" type="text" style="width: 285px" />
					</td>
				</tr>
				<tr>
					<td align="right" style="width: 120px">页面端口：</td>
					<td valign="middle" style="width: 300px">
					<input class="easyui-numberbox" id="port10" name="port10" type="text" style="width: 285px" />
					</td>
				</tr>
				<tr>
					<td align="right" style="width: 120px">REC端口：</td>
					<td valign="middle" style="width: 300px">
					<input class="easyui-numberbox" id="port15" name="port15" type="text" style="width: 285px" />
					</td>
				</tr>
				<tr>
					<td align="right" style="width: 120px">SER端口：</td>
					<td valign="middle" style="width: 300px">
					<input class="easyui-numberbox" id="port16" name="port16" type="text" style="width: 285px" />
					</td>
				</tr>
				<tr>
					<td align="right" style="width: 120px">升级端口：</td>
					<td valign="middle" style="width: 300px">
					<input class="easyui-numberbox" id="port17" name="port17" type="text" style="width: 285px" />
					</td>
				</tr>
				<tr>
					<td align="right" style="width: 120px">网关用户：</td>
					<td valign="middle" style="width: 300px">
					<input  class="easyui-validatebox" id="username" name="username" type="text" style="width: 280px" />
					</td>
				</tr>
				<tr>
					<td align="right" style="width: 120px">网关密码：</td>
					<td valign="middle" style="width: 300px">
					<input  class="easyui-validatebox" id="password" name="password" type="text" style="width: 280px" />
					</td>
				</tr>
				<tr>
					<td align="right" style="width: 120px">数据库监听：</td>
					<td valign="middle" style="width: 300px">
					<input  class="easyui-validatebox" id="dburl" name="dburl" type="text" style="width: 280px" />
					</td>
				</tr>
				<tr>
					<td align="right" style="width: 120px">数据库账户：</td>
					<td valign="middle" style="width: 300px">
					<input  class="easyui-validatebox" id="dbusername" name="dbusername" type="text" style="width: 280px" />
					</td>
				</tr>
				<tr>
					<td align="right" style="width: 120px">数据库密码：</td>
					<td valign="middle" style="width: 300px">
					<input  class="easyui-validatebox" id="dbpassword" name="dbpassword" type="text" style="width: 280px" />
					</td>
				</tr>
				<tr>
					<td align="right" style="width: 120px">管理员账户：</td>
					<td valign="middle" style="width: 300px">
					<input  class="easyui-validatebox" id="dbadminusername" name="dbadminusername" type="text" style="width: 280px" />
					</td>
				</tr>
				<tr>
					<td align="right" style="width: 120px">管理员密码：</td>
					<td valign="middle" style="width: 300px">
					<input  class="easyui-validatebox" id="dbadminpassword" name="dbadminpassword" type="text" style="width: 280px" />
					</td>
				</tr>
				<tr>
					<td align="right" style="width: 120px">GPS表类型：</td>
					<td valign="middle" style="width: 300px">
					 <select class="easyui-combobox" panelHeight="auto"  name="gpsTableType" style="width: 285px">
					 	<option value="NETPACK_GPS">NETPACK_GPS</option>
					 	<option value="NETPACK_GPS_YYYYMMDD">NETPACK_GPS_YYYYMMDD</option>
					 </select>
					</td>
				</tr>
			</table>
			
			<table align="center" style="margin-top:30px">
				<tr>
					<td colspan="2">
						<a href="javascript:void(0)" class="easyui-linkbutton" style="margin-right:20px" onclick="submitForm();">确&nbsp;&nbsp;&nbsp;定</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm();">清&nbsp;&nbsp;&nbsp;除</a>
					</td>
				</tr>
			</table>
			
			</div>
		</form>
	</div>
</body>
</html>