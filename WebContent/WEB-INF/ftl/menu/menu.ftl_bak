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
		<link rel="stylesheet" href="/js/layui/css/layui.css"  media="all">
		
		<script  src="http://open.itboy.net/common/jquery/jquery1.8.3.min.js"></script>
		<script  src="/js/common/layer/layer.js"></script>
		<script  src="/js/common/bootstrap/3.3.5/js/bootstrap.min.js"></script>
		<script  src="/js/shiro.demo.js"></script>
		<script src="/js/layui/layui.js"></script>
		
		<script >
			layui.use(['tree', 'layer'], function(){
				var layer = layui.layer
				  ,$ = layui.jquery,
				  node = new Array(); 
				
				function treeClick(node){
					console.log(node) //node即为当前点击的节点数据
					
				}
				//生成一个模拟树
				var createTree = function(node, start){
				    node = node || function(){
				      var arr = [];
				      for(var i = 1; i < 10; i++){
				        arr.push({
				          name: i.toString().replace(/(\d)/, '$1$1$1$1$1$1$1$1$1')
				        });
				      }
				      return arr;
				    }();
				    start = start || 1;  
				    layui.each(node, function(index, item){  
				      if(start < 10 && index < 9){
				        var child = [
				          {
				            name: (1 + index + start).toString().replace(/(\d)/, '$1$1$1$1$1$1$1$1$1')
				          }
				        ];
				        node[index].children = child;
				        createTree(child, index + start + 1);
				      }
				    });
				    return node;
				};
				
				var treeChange = function(node, parentId, childNode){
					if(node){
						layui.each(node, function(index, item){
							if(node[index].id == parentId){
								var chileren = node[index].children;
								if(chileren){
									chileren = new Arry();
								}
								chileren.push(childNode);
								node[index].children = chileren;
								return ;
							}else{
								treeChange();
							}
						});
					}
				};	
				
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
				var treeInit = function(node){
					node = init(node);
					if(node[0].nextLevel && node[0].isParent){
						
					}
					return node;
				};
				
				layui.tree({
				    elem: '#treeMenu'//指定元素
				    ,nodes: treeInit()
				    ,click: function(item){
				    	treeClick(item);
				    }
				  });
			});
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