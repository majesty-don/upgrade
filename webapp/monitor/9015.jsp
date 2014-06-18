<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>RECEIVE</title>
<!-- <META HTTP-EQUIV="Refresh" content="10">  定时刷新 10秒-->
<style type="text/css">
	a:link { text-decoration: none;color: blue;}
　　 a:active { text-decoration:blink;}
　　 a:hover { text-decoration:underline;color: red;}
　　 a:visited { text-decoration: none;color: green;}
</style>

<style type="text/css">
#customers
  {
  font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
  width:98%;
  border-collapse:collapse;
  margin-top:15px;
  }

#customers td, #customers th 
  {
  font-size:1em;
  border:0px solid #D3D3D3;
  padding:3px 7px 2px 7px;
  }

#customers th 
  {   
  font-size:1.1em;
  text-align:left;
  padding-top:5px;
  padding-bottom:4px;
  color:#000000;
  }
</style>

<link rel="stylesheet" type="text/css" href="<c:url value="/easyui/themes/gray/easyui.css"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/easyui/themes/icon.css"/>">
<script type="text/javascript" src="<c:url value="/easyui/jquery.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/easyui/jquery.easyui.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
</head>
<body class="easyui-layout" data-options="fit : true,border : false" >
  <div id="home" region="center" data-options="fit : true" >
	<table id="customers" align="center">
		<%
			String arg=request.getParameter("arg");
			if(arg==null){
				arg="";
			}
		%>
		<c:set var="i" value="0"/>
		<c:set var="j" value="2"/>
		<c:forEach items="${list }"  step="3" >
			<tr>
				<c:forEach items="${list }" var="city"  begin="${i }" end="${j }" varStatus="status">
				<td>
					<a target="_blank" href="http://${city.remote}:${city.port15 }/<%=arg%>"><c:out value="${city.name }"/></a>
					<br/>
					<iframe name="${city.name }" src="http://${city.remote}:${city.port15 }/<%=arg%>"  height=250 width=100% style="border:1px solid #D3D3D3;">No Support Frame </iframe>
				</td>
				</c:forEach>
				<c:set var="i" value="${i+3 }"/>
				<c:set var="j" value="${j+3 }"/>
			</tr>
		</c:forEach>
   </table>	
 </div>
</body>
</html>