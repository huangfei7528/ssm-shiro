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
		<!-- <link href="/js/jquery-ztree/css/zTreeStyle.css" rel="stylesheet" type="text/css" /> -->
		<link href="/js/jquery-ztree/css/metroStyle/metroStyle.css" rel="stylesheet"  type="text/css">
		
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
					addHoverDom:ztreeAddHoverDom,//自定义添加图标
					removeHoverDom: removeHoverDom,//自定义修改图标
					dblClickExpand: false//禁用双击
				},
				edit:{
					enable:true,
					showRenameBtn:false//显示删除图标
				},
				callback:{
					onClick: zTreeOnClick,
					onExpand:zTreeOnExpand,
					beforeRemove:ztreeBeforeRemove
				}
			};
			
			function ztreeAddHoverDom(treeId, treeNode){
				 console.log("ztreeAddHoverDom:"+treeId+"-"+treeNode);
				 var sObj = $("#" + treeNode.tId + "_span");
	             if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) 
	            	 return;
	             var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
	                + "' title='添加' onfocus='this.blur();'></span>";
	             var editStr = "<span class='button edit' id='editBtn_"+treeNode.tId
	             	+"' title='修改' onfocus='this.blur();'></span>";
	             sObj.after(addStr);
	             sObj.after(editStr);
	             var add_btn = $("#addBtn_"+treeNode.tId);
	             var edit_btn = $("#editBtn_"+treeNode.tId);
	             if (add_btn){
	            	 add_btn.bind("click", function(){
		                $("#parentTreeId").val(treeNode.tId);
		                $("#childParentId").val(treeNode.id);	
		                $("#addmenu").modal();
		              	 return false;
	             	});
	             }
	             if(edit_btn){
	            	 edit_btn.bind("click", function(){
		                $("#parentTreeId").val(treeNode.tId);
		                $("#childParentId").val(treeNode.id);	
		                $("#editmenu").modal();
		            	$.ajax({
							 type: 'POST',
							 url: '${basePath}/menu/targetEdit.shtml',
							 data: {'id':treeNode.id},
							 dataType:"json",
					         success:function(data){
					        	 if(data && data.status == 200){
					        		 $("#editMenuForm").setForm($.parseJSON(data.node));
					        	 }else{
					        		 layer.alert(data.message);
					        	 }
					         }
						});
		              	 return false;
	             	});
	             }
			};
			
			function removeHoverDom(treeId, treeNode) {
	            $("#addBtn_"+treeNode.tId).unbind().remove();
	            $("#editBtn_"+treeNode.tId).unbind().remove();
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
				$('#addmenu').on('hide.bs.modal', function (e) {
					  // 执行一些动作...
					$(e.currentTarget).find("input").each(function () { 
						$(this).val(""); 
					}); 
				});
				$('#editmenu').on('hide.bs.modal', function (e) {
					  // 执行一些动作...
					$(e.currentTarget).find("input").each(function () { 
						$(this).val(""); 
					}); 
				});
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
			
			function addMenu(){
				var index = layer.load();
				$.ajax({
					 type: 'POST',
					 url: '${basePath}/menu/addMenu.shtml',
					 data: $("#boxMenuForm").serialize(),
					 success:function(data){
						 if(data && data.status == 200){
			        		var treeId = $("#parentTreeId").val();
			        		var ztreeObj = $.fn.zTree.getZTreeObj("treeMenu");
			        		var node = ztreeObj.getNodeByTId(treeId);
			        		var chileNode =$.parseJSON(data.node);
			        		ztreeObj.addNodes(node, -1, chileNode, true);
			        		$('#addmenu').modal('hide');
			        		layer.msg(data.message);
			        		layer.close(index)
			        	 }else{
			        		 layer.close(index);
			        		 layer.alert(data.message);
			        	 }
					 }
				});
			};
			
			$.fn.setForm = function(jsonValue) {  
			    var obj=this;  
			    $.each(jsonValue, function (name, ival) { 
			    	var $oinput ;
			    	try{
			    		$oinput = obj.find("input[name=" + name + "]");  
			    	}catch(e){
			    		return true
			    	}
			        if ($oinput.attr("type")== "radio" || $oinput.attr("type")== "checkbox"){  
			             $oinput.each(function(){  
			                 if(Object.prototype.toString.apply(ival) == '[object Array]'){//是复选框，并且是数组  
			                      for(var i=0;i<ival.length;i++){  
			                          if($(this).val()==ival[i])  
			                             $(this).attr("checked", "checked");  
			                      }  
			                 }else{  
			                     if($(this).val()==ival)  
			                        $(this).attr("checked", "checked");  
			                 }  
			             });  
			        }else if($oinput.attr("type")== "textarea"){//多行文本框  
			            obj.find("[name="+name+"]").html(ival);  
			        }else{  
			             obj.find("[name="+name+"]").val(ival);   
			        }  
			   });  
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
			<#--添加弹框-->
			<div class="modal fade" id="addmenu" tabindex="-1" role="dialog" aria-labelledby="addmenuLabel">
			  <div class="modal-dialog" menu="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="addroleLabel">添加菜单</h4>
			      </div>
			      <div class="modal-body">
			        <form id="boxMenuForm">
			          <div class="form-group">
			            <label for="recipient-name" class="control-label">菜单名称:</label>
			            <input type="text" class="form-control" name="name" id="name" placeholder="请输入菜单名称"/>
			            <input type="hidden" name="pid" id="childParentId" value=""/>
			            <input type="hidden" name="id" value=""/>
			            <input type="hidden" id="parentTreeId" value=""/>
			          </div>
			          <div class="form-group">
			            <label for="recipient-name" class="control-label">菜单url:</label>
			            <input type="text" class="form-control" id="type" name="url"  placeholder="请输入菜单url">
			          </div>
			          <div class="form-group">
			            <label for="recipient-name" class="control-label">排序:</label>
			            <input type="text" class="form-control" id="orderBy" name="orderBy"  placeholder="请输入排序  [数字]">
			          </div>
			          <div class="form-group">
			            <label for="recipient-name" class="control-label">备注:</label>
			            <input type="text" class="form-control" id="remark" name="remark"  placeholder="请输入备注">
			          </div>
			        </form>
			      </div>
			      <div class="modal-footer">
			        <button type="button" onclick="addMenu();" class="btn btn-primary">Save</button>
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			      </div>
			    </div>
			  </div>
			</div>
			<#--/添加弹框-->
			<#--修改弹框-->
			<div class="modal fade" id="editmenu" tabindex="-1" role="dialog" aria-labelledby="editmenuLabel">
			  <div class="modal-dialog" menu="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="addroleLabel">添加菜单</h4>
			      </div>
			      <div class="modal-body">
			        <form id="editMenuForm">
			          <div class="form-group">
			            <label for="recipient-name" class="control-label">菜单名称:</label>
			            <input type="text" class="form-control" name="name" id="name" placeholder="请输入菜单名称"/>
			            <input type="hidden" name="pid" id="childParentId" value=""/>
			            <input type="hidden" id="parentTreeId" value=""/>
			          </div>
			          <div class="form-group">
			            <label for="recipient-name" class="control-label">菜单url:</label>
			            <input type="text" class="form-control" id="type" name="url"  placeholder="请输入菜单url">
			          </div>
			          <div class="form-group">
			            <label for="recipient-name" class="control-label">排序:</label>
			            <input type="text" class="form-control" id="type" name="orderBy"  placeholder="请输入排序  [数字]">
			          </div>
			          <div class="form-group">
			            <label for="recipient-name" class="control-label">备注:</label>
			            <input type="text" class="form-control" id="type" name="remark"  placeholder="请输入备注">
			          </div>
			        </form>
			      </div>
			      <div class="modal-footer">
			        <button type="button" onclick="addMenu();" class="btn btn-primary">Save</button>
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			      </div>
			    </div>
			  </div>
			</div>
			<#--/修改弹框-->
		</div>
			
	</body>
</html>