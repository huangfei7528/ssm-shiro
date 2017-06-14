<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html lang="zh-cn">
	<head>
		<meta charset="utf-8" />
		<title>用户角色分配 - 权限管理</title>
		<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
		<link   rel="icon" href="http://img.wenyifan.net/images/favicon.ico" type="image/x-icon" />
		<link   rel="shortcut icon" href="http://img.wenyifan.net/images/favicon.ico" />
		<link href="/js/common/bootstrap/3.3.5/css/bootstrap.min.css?${_v}" rel="stylesheet"/>
		<link href="/css/common/base.css?${_v}" rel="stylesheet"/>
		<link href="/js/jquery-ztree/css/zTreeStyle.css" rel="stylesheet" type="text/css" />
		
		<script src="/js/jquery-3.2.1.min.js"></script>
    	<script src="/js/jquery-ztree/jquery.ztree.core-3.5.26.js" type="text/javascript"></script>
   		<script src="/js/jquery-ztree/jquery.ztree.exedit-3.5.26.js" type="text/javascript"></script>
   		
		<script  src="/js/common/layer/layer.js"></script>
		<script  src="/js/common/bootstrap/3.3.5/js/bootstrap.min.js"></script>
		<script  src="/js/shiro.demo.js"></script>
		
		
		<script >
			var zTreeObj;
			var setting = {
				async: {
					enable: true,
					url: '${basePath}/menu/loadMenu.shtml', 
					autoParam: ['id=parentId'],
					otherParam: null,
					dataFilter: zTreeFilter,
					dataType: "json",
					type: "post"
				},
				view: {
					dblClickExpand: false,//禁用双击
					showIcon:true
				},
				edit:{
					enable:true,
					showRenameBtn:true
				},
				callback:{
					onClick: zTreeOnClick,
					onExpand:zTreeOnExpand,
					beforeRemove:ztreeBeforeRemove
				}
			};
			function ztreeBeforeRemove(reeId, treeNode){
				var flag = false;
				$.ajax({
					 type: 'POST',
					 url: '${basePath}/menu/deleteMenu.shtml',
					 data: {'id':treeNode.id},
					 dataType:"json",
					 async : false,
			         success:function(data){
			        	 if(data && data.status == 200){
			        		 flag = result(true);
			        	 }else{
			        		 flag = result(false);
			        		 layer.alert(data.message);
			        	 }
			         }
				});
				return flag;
			};
			
			function result(flag){
				return flag;
			}
			
			function zTreeFilter(treeId, parentNode, childData) {
				 if(childData && childData.status == 200){
					 childData = eval(childData.treeList);
					 console.log("zTreeFilter:"+childData)
			   	 }
			    return childData;
			};
			var zTreeOnExpand = function(){
				console.log("zTreeOnExpand:")
			};
			var zTreeOnClick = function(){
				var treeObj = $.fn.zTree.getZTreeObj("treeMenu");
				//得到选中的节点
			    var node = treeObj.getSelectedNodes();
			    console.log("zTreeOnClick:")
			};
			$(document).ready(function () {
				var node ;
				node = init(node);
				zTreeObj = $.fn.zTree.init($("#treeMenu"), setting, node);
			});
			
			var init = function(node, parentId){
				$.ajax({
					 type: 'POST',
					 url: '${basePath}/menu/loadMenu.shtml',
					 data: {'parentId':parentId},
					 dataType:"json",
					 async : false,
			         success:function(data){
			        	 if(data && data.status == 200){
			        		 node = node || eval(data.treeList);
			        		 console.log(node) //node即为当前点击的节点数据
			        	 }else{
			        		 layer.alert(data.message);
			        		 layer.close();
			        	 }
			         }
				});
				return node
			};
		</script>
	</head>
	<body data-target="#one" data-spy="scroll">
		<#--引入头部-->
		<@_top.top 3/>
		<div class="container" style="padding-bottom: 15px;min-height: 300px; margin-top: 40px;">
			<div class="row">
				<#--引入左侧菜单-->
				<@_left.role 2/>
				<div class="col-md-10">
					<h2>菜单管理</h2>
					<hr>
					<!-- 树 -->
					<div class="span3" style="float: left; overflow-y: scroll; overflow-x: auto;">
						<ul id="treeMenu" class="ztree"></ul>
						<input type = "hidden" name = "parentId" id = "parentId" value = ""/>
					</div>
				</div>
			</div><#--/row-->
			
			<#--弹框-->
			<div class="modal fade bs-example-modal-sm"  id="selectRole" tabindex="-1" role="dialog" aria-labelledby="selectRoleLabel">
			  <div class="modal-dialog modal-sm" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="selectRoleLabel">添加角色</h4>
			      </div>
			      <div class="modal-body">
			        <form id="boxRoleForm">
			          loading...
			        </form>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			        <button type="button" onclick="selectRole();" class="btn btn-primary">Save</button>
			      </div>
			    </div>
			  </div>
			</div>
			<#--/弹框-->
			
		</div>
			
	</body>
</html>