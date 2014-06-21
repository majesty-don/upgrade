<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>城市列表</title>
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
<script type="text/javascript" src="<c:url value="/easyui/datagrid-filter.js"/>"></script>

<link rel="stylesheet"
	href="<c:url value="/plupload/js/jquery.plupload.queue/css/jquery.plupload.queue.css"/>"
	type="text/css">
<script type="text/javascript"
	src="<c:url value="/plupload/js/plupload.full.min.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/plupload/js/jquery.plupload.queue/jquery.plupload.queue.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/plupload/js/i18n/zh_CN.js"/>"></script>
<script type="text/javascript">
	//datagrid 中的 idField 属性，必须对应后台的实体属性字段，而且该字段必须确保唯一性，一般用id标识，否则就无法获取在datagrid中选择的多条记录。
	function initList(listDisplayEId) {
		var width = $('body').width() - 164;
		var height = $('body').height() - 57-245;
		$("#" + listDisplayEId).datagrid({
			url : 'city/getList.json',
			idField : 'id',
			sortName : 'id',
			sortOrder : 'asc',
			width : width,
			height : height,
			nowrap : true,
			striped : true,
			fitColumns : false,
			fit:true,
			collapsible : true,
			remoteSort : true,
			singleSelect : false,
			selectOnCheck : false,
			checkOnSelect : true,
			pagination : true,
			rownumbers : true,
			toolbar : [ {
				text : '添&nbsp;&nbsp;&nbsp;加',
				iconCls : 'icon-add',
				handler : function() {
					openDialog('newCity','city/toAdd.html','add_dialog','添加城市信息');
				}
			}, '-', {
				text : '删&nbsp;&nbsp;&nbsp;除',
				iconCls : 'icon-remove',
				handler : function() {
					removeList(listDisplayEId);
				}
			},'-', {
				text : '上传文件',
				iconCls : 'icon-upload',
				handler : function() {
					addWindow('uploader','city/toUpload.html','上传文件');
				}
			},'-', {
				text : '升级SERV',
				iconCls : 'icon-serv',
				handler : function() {
					upgrade(listDisplayEId,'city/serv.html');
				}
			},'-', {
				text : '升级RECV',
				iconCls : 'icon-recv',
				handler : function() {
					upgrade(listDisplayEId,'city/recv.html');
				}
			},'-', {
				//text : '城市名：<input type="text" id="nameLike" name="nameLike"/>',
				text:$('#search'),
				handler : function() {
					// do nothing	
				}
			} ]
		});
	}
</script>
<script type="text/javascript">
	$(document).ready(function() {
		initList("gridlist");
	});
	
	//easyUI默认出现滚动条  
	function defaultHaveScroll(gridid){  
	    var opts=$('#'+gridid).datagrid('options');  
	    var text='{';  
	    for(var i=0;i<opts.columns.length;i++){  
	       var inner_len=opts.columns[i].length;  
	       for(var j=0;j<inner_len;j++){  
	           if((typeof opts.columns[i][j].field)=='undefined')break;  
	            text+="'"+opts.columns[i][j].field+"':''";  
	            if(j!=inner_len-1){  
	                text+=",";  
	            }  
	       }  
	    }  
	    text+="}";  
	    text=eval("("+text+")");  
	    var data={"total":1,"rows":[text]};  
	    $('#'+gridid).datagrid('loadData',data);  
	   // $('#grid').datagrid('appendRow',text);  
	   $("tr[datagrid-row-index='0']").css({"visibility":"hidden"});  
	}
	
	function addTab(title, url){
		if ($('#tab').tabs('exists', title)){
			$('#tab').tabs('select', title);
			var currTab = $('#tab').tabs('getTab', title);  
            $('#tab').tabs('update', {
            	tab: currTab, 
            	options: {
            		content: content, 
            		closable: true
            		}
            });
		} else {
			var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
			$('#tab').tabs('add',{
				title:title,
				content:content,
				closable:true
			});
		}
	}

	function remove(id) {
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			dataType : "json",
			url : "city/delete.html",// 请求的action路径
			data : "id=" + id,
			error : function() {
				$.messager.alert("失败", "删除失败", 'error');
			},
			success : function(data) { // 请求成功后处理函数。
				if (data.failure && data.failure == true) {
					$.messager.alert("失败", "删除失败", 'error');
				} else {
					$.messager.alert("成功", "删除成功", 'info', function() {
						$('#gridlist').datagrid('clearSelections');
						$('#gridlist').datagrid('reload');
					});
				}
			}
		});
	}

	function removeList(datagridId) {
		/* var rows = $('#'+datagridId).datagrid('getSelections');  
		$.messager.alert("信息", "选中的行数："+rows.length, 'info');
		
		for(var i=0; i<rows.length; i++){  
			$.messager.alert("信息", 'ID:'+rows[i].id+"Name:"+rows[i].name, 'info'); 
		} */
		var checkedItems = $('#' + datagridId).datagrid('getChecked');
		var length = checkedItems.length;
		//$.messager.alert("信息", length, 'info');
		if (length == 0) {
			$.messager.alert("信息", '请选择记录！', 'info');
		} else {
			$.messager.confirm('删除', '您确认删除所选记录？', function(r) {
				if (r) {
					var ids = "";
					$.each(checkedItems, function(index, item) {
						if (length - 1 == index) {
							ids = ids + item.id;
						} else {
							ids = ids + item.id + ",";
						}
					});
					$.ajax({
						async : false,
						cache : false,
						type : 'POST',
						dataType : "json",
						url : "city/deleteList.html",
						data : "ids=" + ids,
						error : function() {
							$.messager.alert('错误', "删除失败", 'error');
						},
						success : function(data) { // 请求成功后处理函数。
							if (data.failure && data.failure == true) {
								$.messager.alert('错误', "删除失败", 'error');
							} else {
								$.messager.alert("成功", "删除成功", 'info',
										function() {
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
	
	function upgrade(datagridId,url){
		var checkedItems = $('#' + datagridId).datagrid('getChecked');
		var length = checkedItems.length;
		if (length == 0) {
			$.messager.alert("信息", '请选择升级城市！', 'info');
		} else {
			$.messager.confirm('删除', '您确认升级所选城市？', function(r) {
				if (r) {
					var ids = "";
					$.each(checkedItems, function(index, item) {
						if (length - 1 == index) {
							ids = ids + item.id;
						} else {
							ids = ids + item.id + ",";
						}
					});
					$.ajax({
						async : false,
						cache : false,
						type : 'POST',
						dataType : "json",
						url : url,
						data : "ids=" + ids,
						error : function() {
							$.messager.alert('错误', "升级失败", 'error');
						},
						success : function(data) { // 请求成功后处理函数。
							if (data.failure && data.failure == true) {
								$.messager.alert('错误', "升级失败", 'error');
							} else {
								$.messager.alert("成功", "正在升级中...", 'info',
										function() {
											$('#gridlist').datagrid('clearSelections');
											//$('#gridlist').datagrid('reload');
										});
							}
						}
					});
				}
			});
		}
	}
	
	function operate(selectO, rowid) {
		if(selectO.value=='edit'){
			openDialog('editCity','city/toEdit.html?id='+rowid,'edit_dialog','修改城市信息');
		}else if (selectO.value == 'delete') {
			$.messager.confirm('删除', '您确认删除所选记录？', function(r) {
				if (r) {
					remove(rowid);
				}
			});
		}
		selectO.selectedIndex = 0;
	}

	function cSelect(value, row, index, field) {
		var se = "<select onChange='javascript:operate(this,\"" + row.id+ "\")'>";
		se = se + "<option value='op'>操作</option>";
		se = se + "<option value='edit'>修改</option>";
		se = se + "<option value='delete'>删除</option>";
		se = se + "</select>";
		return se;
	}

	/**
	 * 点添加按钮操作，打开新窗口
	 * @return
	 */
	function addWindow(divid,url,title) {
		var cont = '<iframe scrolling="yes" frameborder="0" src="<c:url value="'+url+'"/>" style="width:100%;height:100%;"></iframe>';
		$('#'+divid).window({
			title : title,
			width : 780,
			height : 390,
			//隐藏内容
			collapsible : false,
			//最小化按钮是否可用
			minimizable : false,
			//最大化按钮
			maximizable : false,
			//是否可关闭
			closable : true,
			//关闭状态
			closed : false,
			//是否可自由拖动
			draggable : false,
			//是否可改变大小
			resizable : false,
			//阴影
			shadow : false,
			//模态方式打开
			modal : true,
			//内容
			content : cont
		});
		$("#"+divid).unload(function() {
			$('#gridlist').datagrid('reload');
		});
	}
	
	  function openDialog(iframeid,url,dialogId,title) {
		  $('#'+iframeid)[0].src=url;
		  $('#'+dialogId).dialog('open').dialog('setTitle', title);
      }
	  
	  function close(dialogId){
		  $('#'+dialogId).dialog('close');
		  $('#gridlist').datagrid('reload');
	  }
	  
	  function doSearch(value){
		  var url='city/search.json?nameLike='+encodeURI(encodeURI(value));
		  $('#gridlist').datagrid("options").url = url;
		  $('#gridlist').datagrid('load');  
	 }
</script>

</head>
<body class="easyui-layout" style="overflow-y: auto;">
	<div data-options="region:'west',split:false,collapsible:false" title="功能区" style="width:150px;">
		
		<table>
			<tr style="height:30px;">
				<td align="center"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-monitor'" onclick="addTab('系统监控','<c:url value="druid/index.html" />')">系统监控</a></td>
			</tr>
			<tr style="height:30px;">
				<td align="center"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-monitor'" onclick="addTab('文件监控','<c:url value="count/toCrawl.html" />')">文件监控</a></td>
			</tr>
			<tr style="height:30px;">
				<td align="center"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-monitor'" onclick="addTab('页面链接','<c:url value="city/9010.html" />')">页面链接</a></td>
			</tr>
			<tr style="height:30px;">
				<td align="center"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-monitor'" onclick="addTab('9016监控','<c:url value="city/9016.html" />')">SERV监控</a></td>
			</tr>
			<tr style="height:30px;">
				<td align="center"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-monitor'" onclick="addTab('9015监控','<c:url value="city/9015.html" />')">RECV监控</a></td>
			</tr>
			<tr style="height:30px;">
				<td align="center"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-monitor'" onclick="addTab('图形监控','<c:url value="city/pic.html" />')">图形监控</a></td>
			</tr>
			<tr style="height:30px;">
				<td align="center"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-list'" onclick="addTab('上传文件','<c:url value="file/toList.html" />')">上传文件</a></td>
			</tr>	
		</table>
	
	</div>
	<div data-options="region:'south',split:false" style="height:25px;">
		<div style="text-align:center;font-size:9pt;font-family:arial; margin-top:5px">
			&copy;2014 Majesty. All rights reserved.
		</div>
	</div>
	
	<div region="center" style="verflow:hidden;" >
		<div id="tab" class="easyui-tabs" data-options="fit : true,border : false" >
			<div title="城市信息">
				<table align="center" id="gridlist"  loadMsg='正在处理,请稍候...'>
					<thead>
						<tr>
							<th field="ck" width="10" checkbox="true"></th>
							<th field="id" width="80" align="center" hidden="true" >序号</th>					
							<th field="citynamecn" width="100" align="center">城市名</th>
							<th field="citynameen" width="100" align="center">拼音</th>
							<th field="host" width="100" align="center">主机</th>
							<th field="port22" width="80" align="center">SSH</th>
							<th field="port10" width="80" align="center">WEB</th>
							<th field="port15" width="80" align="center">RECV</th>
							<th field="port16" width="80" align="center">SERV</th>
							<th field="port17" width="80" align="center">升级</th>
							<th field="username" width="100" align="center">网关帐号</th>
							<th field="password" width="100" align="center">网关密码</th>
							<th field="dburl" width="450" align="center">数据库监听</th>
							<th field="dbusername" width="100" align="center">DB账号</th>
							<th field="dbpassword" width="100" align="center">DB密码</th>
							<th field="dbadminusername" width="100" align="center">管理员账号</th>
							<th field="dbadminpassword" width="100" align="center">管理员密码</th>
							<th field="gpsTableType" width="100" align="center">GPS表类型</th>
							<th field="opt" width="100" formatter="cSelect" align="center">操作</th>
						</tr>
					</thead>
	   			</table>
			</div>
		</div>
 	</div>
 	
 	<div id="upload" title="上传文件" style="font-size: 10; display: none;">
		<div id="uploader"></div>
 	</div>
 	
 	<div id="add_dialog" class="easyui-dialog"  style="width: 780px; height: 570px; padding: 10px 20px;" closed="true" > 
 		<iframe scrolling="auto" id='newCity' frameborder="0"  src="" style="width:100%;height:100%;"></iframe>
 		
    </div>
	
	<div id="edit_dialog" class="easyui-dialog"  style="width: 780px; height: 570px; padding: 10px 20px;" closed="true" > 
 		<iframe scrolling="auto" id='editCity' frameborder="0"  src="" style="width:100%;height:100%;"></iframe> 		
    </div>
	
	 <div id="search" style="padding:5px;height:auto">
	 	<input id="nameLike" name="nameLike" class="easyui-searchbox" data-options="prompt:'城市名称',searcher:doSearch" style="width:150px"/>
	 </div>

</body>
</html>