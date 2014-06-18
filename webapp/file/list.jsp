<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>升级列表</title>
<style type="text/css">
	a:link { text-decoration: none;color: blue}
　　 a:active { text-decoration:blink}
　　 a:hover { text-decoration:underline;color: red}
　　 a:visited { text-decoration: none;color: green}
</style>
<link rel="stylesheet" type="text/css" href="<c:url value="/easyui/themes/gray/easyui.css"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/easyui/themes/icon.css"/>">
<script type="text/javascript" src="<c:url value="/easyui/jquery.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/easyui/jquery.easyui.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
<script type="text/javascript" src="<c:url value="/easyui/jquery-easyui/ext-for-framework.js.jsp"/>"></script>
<script type="text/javascript" src="<c:url value="/easyui/plugins/jquery/jquery.form.js"/>"></script> 

<link rel="stylesheet" href="<c:url value="/plupload/js/jquery.plupload.queue/css/jquery.plupload.queue.css"/>" type="text/css">
<script type="text/javascript" src="<c:url value="/plupload/js/plupload.full.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/plupload/js/jquery.plupload.queue/jquery.plupload.queue.js"/>"></script> 
<script type="text/javascript" src="<c:url value="/plupload/js/i18n/zh_CN.js"/>"></script> 
<script type="text/javascript">
//datagrid 中的 idField 属性，必须对应后台的实体属性字段，而且该字段必须确保唯一性，一般用id标识，否则就无法获取在datagrid中选择的多条记录。
function initList(listDisplayEId){
	var width = $('body').width()-5;
	var height = $('body').height()-29;
	$("#" + listDisplayEId).datagrid({
		url : 'getList.json',
		queryParams:{},
		idField:'id',
		sortName: 'filename',
		sortOrder: 'asc',
		width:width,
		height:height,
		nowrap: true,
		striped: true,
		fitColumns: true,
		collapsible:true,
		remoteSort: true,
		singleSelect:false,
		selectOnCheck: false,
		checkOnSelect: true,
		pagination:true,
		rownumbers:true,
		toolbar: [{
			text:'上传文件',
			iconCls:'icon-upload',
			handler:function(){
				addWindow();
			}
		},'-',{
			text:'删&nbsp;&nbsp;&nbsp;除',
			iconCls:'icon-remove',
			handler:function(){removeList(listDisplayEId);}
		}]
	});
}


</script>
<script type="text/javascript">

$(document).ready(function() {
	initList("gridlist");
});

function remove(id){
	 $.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		url : "delete.html",// 请求的action路径
		data : "id="+ id,
		error : function() {
			$.messager.alert("失败", "删除失败", 'error');
		},
		success : function(data) { // 请求成功后处理函数。
			if(data.failure && data.failure==true){
				$.messager.alert("失败", "删除失败", 'error');
			}else{
				$.messager.alert("成功", "删除成功",'info', function(){
				$('#gridlist').datagrid('clearSelections');
				$('#gridlist').datagrid('reload');
			});
		}
    } 
  }); 
}

function removeList(datagridId){
	/* var rows = $('#'+datagridId).datagrid('getSelections');  
	$.messager.alert("信息", "选中的行数："+rows.length, 'info');
    
    for(var i=0; i<rows.length; i++){  
    	$.messager.alert("信息", 'ID:'+rows[i].id+"Name:"+rows[i].name, 'info'); 
    } */  
	var checkedItems=$('#'+datagridId).datagrid('getChecked');
    var length=checkedItems.length;
    //$.messager.alert("信息", length, 'info');
    if(length==0){
    	$.messager.alert("信息", '请选择记录！', 'info');
    }else{
    	$.messager.confirm('删除','您确认删除所选记录？',function(r){
			if(r){
				var ids="";
		    	$.each(checkedItems, function(index, item){
					if(length-1==index){
						ids=ids+item.id;
					}else{
						ids=ids+item.id+",";
					}   		
		    	}); 
		    	$.ajax({
					async : false,
					cache : false,
					type : 'POST',
					dataType : "json",
					url : "deleteList.html",
					data : "ids="+ ids,
					error : function() {
						$.messager.alert('错误', "删除失败",'error');
					},
					success : function(data) { // 请求成功后处理函数。
		         		if(data.failure && data.failure == true){             
		        	 		$.messager.alert('错误', "删除失败",'error');
		         		}else{
		         			$.messager.alert("成功", "删除成功",'info', function(){
		        				//$('#gridlist').datagrid('clearSelections');
		        				$('#gridlist').datagrid('reload');
		         			});
		         		}
		    		} 
		      	}); 
			}
		});
    	
    }
}

function operate(selectO,rowid){
	if(selectO.value == 'delete'){
		$.messager.confirm('删除','您确认删除所选记录？',function(r){
			if(r){
				remove(rowid);
			}
		});
	}
	selectO.selectedIndex = 0;
}
	
function cSelect(value,row,index,field){
	var se="<select onChange='javascript:operate(this,\""+row.id+"\")'>";
	 se=se+"<option value='op'>操作</option>";
	 se=se+"<option value='delete'>删除</option>";
	 se=se+"</select>";
	 return se;
}

/**
 * 点添加按钮操作，打开新窗口
 * @return
 */
function addWindow(){
    var cont = '<iframe scrolling="yes" frameborder="0" src="<c:url value="/file/upload.jsp"/>" style="width:100%;height:100%;"></iframe>';
    $('#uploader').window({ 
        title: '上传文件',
        width: 780, 
        height: 390, 
        //隐藏内容
        collapsible:false, 
        //最小化按钮是否可用
        minimizable:false, 
        //最大化按钮
        maximizable:false, 
        //是否可关闭
        closable:true, 
        //关闭状态
        closed: false, 
        //是否可自由拖动
        draggable:false, 
        //是否可改变大小
        resizable:false, 
        //阴影
        shadow:false, 
        //模态方式打开
        modal:true,
        //内容
        content:cont
    });
	$("#uploader").unload(function() {
    	$('#gridlist').datagrid('reload');
    }); 
}


</script>

</head>
<body class="easyui-layout" data-options="fit : true,border : false" >
  <div id="home" title="文件信息" region="center" data-options="fit : true" >
	<table align="center" id="gridlist"  loadMsg='正在处理,请稍候...'>
		<thead>
			<tr>
				<th field="ck" width="2%" checkbox="true"></th>
				<th field="id" width="8%" align="center" hidden="true" >序号</th>					
				<th field="filename" width="30%" align="center">文件名称</th>
				<th field="filepath" width="50%" align="center">文件路径</th>
				<th field="opt" width="10%" formatter="cSelect" align="center">操作</th>
			</tr>
		</thead>
   </table>	
 </div>
  
 <div id="upload" title="上传文件" style="font-size: 10; display: none;">
	<div id="uploader"></div>
 </div>

</body>
</html>