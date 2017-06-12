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
		<script src="/js/jquery-ztree/jquery.ztree.all-3.5.js"></script>
		<script src="/js/jquery-ztree/jquery.myTree.js"></script>
		<script >
			var $currentNode;// 当前操作的节点
			$(document).ready(function() {
				var myTree = $("#treeMenu").myTree({
					url : "${basePath}/menu/loadMenu.shtml",
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
			// 展开树节点
			function zTreeOnExpand(event, treeId, treeNode) {
				$currentNode = treeNode;
				$.fn.zTree.getZTreeObj("treeDemo").selectNode(treeNode);// 选中被展开的节点
				refreshData(treeNode);
			};
			
			// 刷新数据
			function refreshData(treeNode) {
			/* 	writeCurrentMenu(treeNode);
				writeJuniorMenu(treeNode); */
			}
			// 当前菜单
			function writeCurrentMenu(treeNode) {
				$("#currentMenu").writeAttr(treeNode.menu);
				$("#idForm [name='parentId']").val("");
				$("#idForm [name='entityId']").val("");
				if (treeNode.level == 0) {// 根目录不能编辑
					$("#modifyMenuBtn").hide();// 隐藏按钮
					// 新增菜单时，用到的父亲ID和排序
					$("#idForm [name='parentId']").val(treeNode.menu.id);
				} else {// 编辑当前菜单，需要的内容
					/*
					 * $("#modifyMenuBtn").show();//显示按钮
					 * $("#modifyMenuDiv").writeAttr(treeNode.menu);//填充对应属性
					 */$("#idForm [name='entityId']").val(treeNode.menu.id);
					/* $("#idForm [name='oldTitle']").val(treeNode.menu.title); */
					// 新增菜单时，用到的父亲ID和排序
					$("#idForm [name='parentId']").val(treeNode.pId);
				}

			}
			// 下级的菜单
			function writeJuniorMenu(treeNode) {
				$("#juniorMenu tbody").children().remove();
				var html = ""
				if (treeNode.isParent) {// 遍历所有孩子信息
					$.each(treeNode.children, function(idx, element) {
						html += "<tr><td><input type='checkbox' class='idcheckbox' value='"
								+ element.menu.id + "'></td><td>" + element.menu.title
								+ "</td><td>" + element.menu.name + "</td><td>"
								+ nullToEmpty(element.menu.url) + "</td><td>"
								+ nullToEmpty(element.menu.orderBy) + "</td><td>"
								+ nullToEmpty(element.menu.description) + "</td></tr>"
					});
				}
				$("#juniorMenu tbody").html(html);
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