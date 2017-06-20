<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8" />
		<title>角色列表 - 权限管理</title>
		<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
		<link   rel="icon" href="${basePath}/favicon.ico" type="image/x-icon" />
		<link   rel="shortcut icon" href="${basePath}/favicon.ico" />
		<link href="${basePath}/js/common/bootstrap/3.3.5/css/bootstrap.min.css?${_v}" rel="stylesheet"/>
		<link href="${basePath}/css/common/base.css?${_v}" rel="stylesheet"/>
		<link href="/js/jquery-ztree/css/metroStyle/metroStyle.css" rel="stylesheet"  type="text/css">
		
	</head>
	<body>
		<!-- 树 -->
		<input type = "hidden" id = "roleId" name = "roleId" value = "${roleId }"/>
		<div class="span3" style="float: left; overflow-y: scroll; overflow-x: auto;">
			<ul id="roleTreeMenu" class="ztree"></ul>
		</div>
	</body>
		<script src="${basePath}/js/jquery-3.2.1.min.js"></script>
		<script  src="${basePath}/js/common/layer/layer.js"></script>
		<script  src="${basePath}/js/common/bootstrap/3.3.5/js/bootstrap.min.js"></script>
		<script  src="${basePath}/js/shiro.demo.js"></script>
		
    	<script src="${basePath}/js/jquery-ztree/jquery.ztree.core-3.5.26.js" type="text/javascript"></script>
   		<script src="${basePath}/js/jquery-ztree/jquery.ztree.exedit-3.5.26.js" type="text/javascript"></script>
		<script >
		
			var setting = {
				async: {
					enable: true,
					url: '${basePath}/role/loadRoleMenu.shtml', 
					autoParam: ['id=parentId'],
					otherParam: null,
					dataType: "json",
					type: "post"
				},
				view: {
					dblClickExpand: false//禁用双击
				},
				/* edit:{
					enable:true,
					showRenameBtn:false//显示删除图标
				}, */
				check:{
					enable:true,
					chkStyle:"checkbox"
				},
				data:{
					simpleData:{
						enable:true
					}
				}
				/* callback:{
					onClick: zTreeOnClick,
					onExpand:zTreeOnExpand,
					beforeRemove:ztreeBeforeRemove
				} */
			};
			$(document).ready(function(){
				$.ajax({
					 type: 'POST',
					 url: '${basePath}/role/loadRoleMenu.shtml',
					 data: {'roleId':$("#roleId").val()},
					 dataType:"json",
					 success:function(data){
			        	 if(data && data.status == 200){
			        		var node =  eval(data.treeList);
			        		var zTreeObj = $.fn.zTree.init($("#roleTreeMenu"), setting, node);
			        	 }else{
			        		 layer.alert(data.message);
			        	 }
			         }
				});
			});
		</script>
</html>