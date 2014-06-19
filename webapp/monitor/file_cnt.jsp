<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件数</title>
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
  border:1px solid #D3D3D3;
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
  
#data
  {
  font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
  width:100%;
  border-collapse:collapse;
  margin-top:0px;
  }

#data td, #data th 
  {
  font-size:1em;
  border:1px solid #D3D3D3;
  padding:3px 7px 2px 7px;
  }

#data th 
  {   
  font-size:1.1em;
  text-align:left;
  padding-top:5px;
  padding-bottom:4px;
  color:#000000;
  }
</style>

<style type="text/css">

 .left3 { /*用在五列布局上*/
     border: 1px solid #D3D3D3;
     width: 100%;
     height: 240px;
    /*  background-color: #D3D3D3; */
     float: left;
     font-size: 14px;
     overflow-y: auto;
     overflow-x: hidden;
 }

 .row { /*定义每行*/
     width: 100%;
     clear: both;
     margin-left: 5px;
     border-top: 10px;
     padding-top: 0px;
 }
</style>

<link rel="stylesheet" type="text/css" href="<c:url value="/easyui/themes/gray/easyui.css"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/easyui/themes/icon.css"/>">
<script type="text/javascript" src="<c:url value="/easyui/jquery.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/easyui/jquery.easyui.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
</head>
<body class="easyui-layout" data-options="fit : true,border : false" style="overflow-x:auto;overflow-y:auto">
  <div id="home" region="center" data-options="fit : true"  class="row"> 	
	<table id="customers" align="center">
		<c:set var="i" value="0"/>
		<c:set var="j" value="2"/>
		<c:forEach items="${map }"  step="3" >
			<tr>
				<c:forEach items="${map }" var="item"  begin="${i }" end="${j }" >
					<td> ${item.key }
						<div class="left3" title="${item.key }">
                			<table id="data" align="center">							
								<c:forEach items="${item.value }" var="value">
								<tr>
									<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${value.datetime}" /></td>
									<td>${value.appname }</td>
									<td>${value.filecount }</td>
								</tr>
								</c:forEach>
							</table>
            			</div>					
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