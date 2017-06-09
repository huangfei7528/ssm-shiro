<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8" />
		<title>用户角色分配 - 权限管理</title>
		<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
		<link   rel="icon" href="http://img.wenyifan.net/images/favicon.ico" type="image/x-icon" />
		<link   rel="shortcut icon" href="http://img.wenyifan.net/images/favicon.ico" />
		<link href="/js/common/bootstrap/3.3.5/css/bootstrap.min.css?${_v}" rel="stylesheet"/>
		<link href="/css/common/base.css?${_v}" rel="stylesheet"/>
		<script  src="http://open.itboy.net/common/jquery/jquery1.8.3.min.js"></script>
		<script  src="/js/common/layer/layer.js"></script>
		<script  src="/js/common/bootstrap/3.3.5/js/bootstrap.min.js"></script>
		<script  src="/js/shiro.demo.js"></script>
		<script >
		/* so.init(function(){
				//初始化全选。
				so.checkBoxInit('#checkAll','[check=box]');
				<@shiro.hasPermission name="/role/clearRoleByUserIds.shtml">
				//全选
				so.id('deleteAll').on('click',function(){
					var checkeds = $('[check=box]:checked');
					if(!checkeds.length){
						return layer.msg('请选择要清除的用户。',so.default),!0;
					}
					var array = [];
					checkeds.each(function(){
						array.push(this.value);
					});
					return deleteById(array);
				});
				</@shiro.hasPermission>
			}); */
			$(document).ready(function() {
				var myTree = $("#treeMenu").myTree({
					url : $ctx + "/menu/loadMenu.do",
					callback : {
						onClick : clickTree,
						onExpand : zTreeOnExpand
					}
				});
			});
			// 菜单点击事件
			function clickTree(event, treeId, treeNode, clickFlag) {
				if (!treeNode.isParent || treeNode.open) {// 打开的，则直接更改显示数据
					$currentNode = treeNode;
					refreshData(treeNode);
				} else {
					$.fn.zTree.getZTreeObj("treeDemo").expandNode(treeNode, true, false,
							true, true);
				}
			}
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
						<ul id="treeMenu" class="ztree" style="width: 150px; height: 77%"></ul>
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