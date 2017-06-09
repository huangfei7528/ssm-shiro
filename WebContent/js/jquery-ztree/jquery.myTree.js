/**
 * 简化zTree调用
 */
(function($) {
	var myTree = new function(){
		this.treeInit = function(param){
			this.config ={
				//访问树的id
				treeId: null,
				//异步访问url地址
				url: null, 
				//自动参数
				autoParam: ["id=parentId"],
				//异步加载数据，成功的回调页面
				callback: {},
				//是否允许复选框
				checkAble: false,
				//一次性加载全部
				loadAll: false,
				//一次性加载完成的回调，2个参数，第一个为返回的数据，第二个为生成tree对象
				loadSuccess: null,
				//其他参数
				otherParam :null
			};
			
			//扩展传入参数
			$.extend(this.config, param);
			
			//赋值id
			this.config.treeId = this.attr("id");
			
			//处理返回的数据
			this.filter = function(treeId, parentNode, childNodes) {
				if (!childNodes) {
					return null;
				}
				if(childNodes.msgcode == 100){
					return childNodes.msg;//返回树需要的json数据
				}else{
					return null;
				}
			}

			//异步加载数据，加载成功后是否自动展开孩子节点
			this.onAsyncSuccess = function(event, treeId, treeNode, msg) {
				var zTree = $.fn.zTree.getZTreeObj(treeId);
				var currentNode = null;//获取后台返回的当前树节点的json数据
				if(msg.msgcode == 100){
					currentNode = msg.msg;//返回树需要的json数据
				}
				$.each(currentNode, function(index,element){
					if(element.isParent == true && (element.open == true
							|| (element.openLevel && (treeNode.level + 1) < element.openLevel))){
						zTree.expandNode(zTree.getNodeByParam("id", element.id, treeNode), true, false, true, true);
					}
				});
			}
			
			//默认的异步处理成功方法
			if(this.config.loadAll == false){
				$.extend(this.config.callback, {"onAsyncSuccess": this.onAsyncSuccess});
			}
			
			//展开对应ztree的指定节点
			this.expandNodes = function(nodes) {
				var zTree = $.fn.zTree.getZTreeObj(this.config.treeId);
				if (!nodes){
					nodes = zTree.getNodes();//没有传参，则从根目录开始重新加载
				}
				for (var i=0, l=nodes.length; i<l; i++) {
					zTree.expandNode(nodes[i], true, false, false);
					if (nodes[i].isParent && nodes[i].zAsync) {//zAsync:true 表示父节点展开时不需要自动异步加载;false 表示父节点展开时需要自动异步加载
						this.expandNodes(nodes[i].children);
					}
				}
			}

			//异步加载ztree的节点
			this.asyncNodes = function(nodes) {
				var zTree = $.fn.zTree.getZTreeObj(this.config.treeId);
				if (!nodes){
					nodes = zTree.getNodes();//没有传参，则从根目录开始重新加载
				}	
				for (var i=0, l=nodes.length; i<l; i++) {
					if (nodes[i].isParent && nodes[i].zAsync) {
						this.asyncNodes(nodes[i].children);
					} else {
						zTree.reAsyncChildNodes(nodes[i], "refresh", true);
					}
				}
			}
			
			//获取checked变更的元素的id数组，返回格式：{addName: [1,2], deleteName: [3,4]}
			this.changeCheckedNodeIds = function(addName, deleteName){
				var zTree = $.fn.zTree.getZTreeObj(this.config.treeId);
				var nodes = zTree.getChangeCheckedNodes();
				
				var addMenus = new Array();
				var deleteMenus = new Array();
				$.each(nodes, function(index, element){
					if(element.checked){
						addMenus.push(element.id);
					}else{
						deleteMenus.push(element.id);
					}
				});
				
				if(addMenus.length == 0 && deleteMenus.length == 0){
					return null;
				}else{
					var changeRes = {};
					changeRes[addName] = addMenus;
					changeRes[deleteName] = deleteMenus;
					return changeRes;
				}
			}
			
			//选中节点
			this.selectNode = function(treeNode){
				$.fn.zTree.getZTreeObj(this.config.treeId).selectNode(treeNode);//选中节点
			}
			
			//展开节点
			this.expandNode = function(treeNode){
				this.selectNode(treeNode);
				$.fn.zTree.getZTreeObj(this.config.treeId).expandNode(treeNode, true, false, true, true);//展开孩子节点，并触发展开事件
			}
			
			//在父节点下面，添加新的孩子节点
			this.addNodes = function(parentNode, newNode){
				$.fn.zTree.getZTreeObj(this.config.treeId).addNodes(parentNode, newNode);//动态添加孩子节点
			}
			
			//修改当前节点
			this.updateNode = function(currentNode){
				$.fn.zTree.getZTreeObj(this.config.treeId).updateNode(currentNode);//动态修改当前节点
			}
			
			//删除当前节点
			this.removeNode = function(currentNode){
				$.fn.zTree.getZTreeObj(this.config.treeId).removeNode(currentNode);
			}
			
			//根据父亲节点和id数组，删除对应的节点数据
			this.removeChildNodes = function(parentNode, removeIds){
				var treeObj = $.fn.zTree.getZTreeObj(this.config.treeId);
				$.each(removeIds, function(idx, element){
					treeObj.removeNode(treeObj.getNodeByParam("id", element, parentNode));//删除节点数据
				});
			}
			
			//初始化ztree
			this.init = function() {
				if(this.config.loadAll){
					var setting = {
							view: {
								dblClickExpand: false//禁用双击
							},
							data: {
								simpleData: {
									enable: true
								}
							},
							check: {
								enable: this.config.checkAble//是否显示复选框
							},
							callback: this.config.callback
						};
					var $tree = this;
					$.post(this.config.url, this.config.otherParam,
					   function(data){
							//var openLevel = 0;
							//$.each(data.treeNodes, function(index,element){
							//	if(index == 0){
							//		if(element.openLevel){
							//			openLevel = element.openLevel;
							//		}
							//		return false;
							//	}
							//});
							var currentNode = null;//获取后台返回的当前树节点的json数据
							if(data.msgcode == 100){
								currentNode = data.msg;//返回树需要的json数据
							}
							var treeObj = $.fn.zTree.init($("#" + $tree.config.treeId), setting, currentNode);
							//if(openLevel > 0){
							//	$.each(treeObj.getNodes(), function(index,element){
							//		if(element.level < openLevel){
							//			treeObj.expandNode(element);//展开节点
							//		}
							//	});
							//}
							if ($tree.config.loadSuccess && $.isFunction($tree.config.loadSuccess)){
								$tree.config.loadSuccess(data, treeObj);
							}
					   }, "json");
				}else{
					//ztree的设置
					var setting = {
						async: {
							enable: true,
							url: this.config.url, 
							autoParam: this.config.autoParam,
							otherParam: this.config.otherParam,
							dataFilter: this.filter,
							dataType: "json",
							type: "get"
						},
						view: {
							dblClickExpand: false//禁用双击
						},
						check: {
							enable: this.config.checkAble
						},
						callback: this.config.callback
					};
				}
				$.fn.zTree.init($("#" + this.config.treeId), setting);
			}
			
			this.init();
			return this;
		}
	}
	
	$.extend({myTree: myTree});
	$.fn.myTree = myTree.treeInit;
})(jQuery);