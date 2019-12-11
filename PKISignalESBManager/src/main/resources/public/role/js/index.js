var WSM = parent.window.WSM;
WSM.role = {};

WSM.role.init = function(table){
	//初始化表格
	WSM.role.initTable("#roletb",table);

};
//初始化权限tree
WSM.role.initAuthoritys = function(form,roleId,$id){
	$("#"+$id).empty();
	var xtree3 = new layuiXtree({
	    elem: $id                 //必填三兄弟之老大
	    , form: form                    //必填三兄弟之这才是真老大
	    , data: '../role.do?getAuthoritysByRoleId&roleId='+roleId //必填三兄弟之这也算是老大
	    , isopen: false  //加载完毕后的展开状态，默认值：true
	    , ckall: false    //启用全选功能，默认值：false
	    , ckallback: function () { alert("全选状态改变")} //全选框状态改变后执行的回调函数
	    , icon: {        //三种图标样式，更改几个都可以，用的是layui的图标
	        open: "&#xe7a0;"       //节点打开的图标
	        , close: "&#xe622;"    //节点关闭的图标
	        , end: "&#xe621;"      //末尾节点的图标
	    }
	    , color: {       //三种图标颜色，独立配色，更改几个都可以
	        open: "#EE9A00"        //节点图标打开的颜色
	        , close: "#EEC591"     //节点图标关闭的颜色
	        , end: "#828282"       //末级节点图标的颜色
	    }
	    , click: function (data) {  //节点选中状态改变事件监听，全选框有自己的监听事件
	        console.log(data.elem); //得到checkbox原始DOM对象
	        console.log(data.elem.checked); //开关是否开启，true或者false
	        console.log(data.value); //开关value值，也可以通过data.elem.value得到
	        console.log(data.othis); //得到美化后的DOM对象
	    }
	});
	return xtree3;
}
/**
 * 
 * @param $tableId 表格的id
 * @param layuitable layui必须的table模块
 */
WSM.role.initTable = function($tableId,table){
	table.render({
		elem:$tableId,
		height:'full-110',
		url:'../role.do?getAllRolesData',
		page:true,
		cols:[[
		   {field:'id',title:'ID',width:80,sort:true},
		   {field:'name',title:'roleName',sort:true},
		   {title:'操作',toolbar:'#barUser',fixed:'right'}
		]]
	});
};
WSM.role.bind = function(table,layer,form){
	//添加角色
	$("#addRole").click(function(){
		//清空选项
		$("#roleName").val("");
		//初始化权限树
		var authoritysTree = WSM.role.initAuthoritys(form,1,'authority');
		layer.open({
			type:1,
			content:$("#operate"),
			title:"新增",
			btn:["提交","取消"],
			btnAlign:'c',
			yes:function(index,layero){//点击确认的回调函数
				//获得name和分配的权限
				var roleName = $("#roleName").val();
				var authoritys1 = authoritysTree.GetChecked();//后来变为authoritys
				var authoritys = new Array();
				var length = authoritys1.length;
				for(var i=0;i<length;i++){
					authoritys.push($(authoritys1[i]).val());
				}
				authoritys=authoritys.join(",");
				var role={
						roleName:roleName,
						authoritys:authoritys
				};
				layer.close(index);
				//提交信息
				$.ajax({
			    	 type:'post',
			    	 url:'../role.do?addRole',
			    	 data:role,
			    	 async:true,
			    	 dataType:'json',
			    	 success:function(res){
			    		 if(res.code==0){
			    			layer.msg("操作成功");
			    			//初始化表格
			    			WSM.role.initTable("#roletb",table);
			    		 }else{
			    			 layer.alert(res.msg);
			    		 }
			    	 },
			    	 error:function(){
			    		 layer.alert("数据添加失败！")
			    	 }
			      });
			},
			btn2:function(index,layero){//点击取消的回调函数
				layer.close(index);
			}
		})
	});
	//监听工具条
	table.on('tool(roletb)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
	  var data = obj.data; //获得当前行数据
	  var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
	  var tr = obj.tr; //获得当前行 tr 的DOM对象
	  if(layEvent === 'del'){ //删除
	    layer.confirm('真的删除行么', function(index){
	      layer.close(index);
	      //向服务端发送删除指令
	      var id = data.id;
	      $.ajax({
	    	 type:'post',
	    	 url:'../role.do?deleteRoleById&roleId='+id,
	    	 async:true,
	    	 dataType:'json',
	    	 success:function(res){
	    		 if(res.code==0){
	    			 layer.msg("操作成功");
	    			 obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
	    		 }else{
	    			 layer.alert(res.msg);
	    		 }
	    	 },
	    	 error:function(){
	    		 layer.alert("数据删除失败！")
	    	 }
	    	 
	      });
	    });
	  } else if(layEvent === 'edit'){ //编辑
		var id = data.id,
			roleName = data.name;
		$("#roleName").val(roleName);
		//打开编辑窗口，改变名称，分配权限
		//初始化权限树
		var authoritysTree = WSM.role.initAuthoritys(form,id,'authority');
		layer.open({
			type:1,
			content:$("#operate"),
			title:"编辑",
			btn:["提交","取消"],
			btnAlign:'c',
			yes:function(index,layero){//点击确认的回调函数
				//获得值：角色ID,角色Name,角色对应得权限（用树状图展示）
				var authoritys1 = authoritysTree.GetChecked();//后来变为authoritys
				var authoritys = new Array();
				var length = authoritys1.length;
				for(var i=0;i<length;i++){
					authoritys.push($(authoritys1[i]).val());
				}
				authoritys=authoritys.join(",");
				var roleName = $("#roleName").val();
				var role={
						roleId:id,
						roleName:roleName,
						authoritys:authoritys
				}
				layer.close(index);
				//提交信息
				$.ajax({
			    	 type:'post',
			    	 url:'../role.do?editUserById',
			    	 data:role,
			    	 async:true,
			    	 dataType:'json',
			    	 success:function(res){
			    		 if(res.code==0){
			    			layer.msg("操作成功");
			    			//同步更新缓存对应的值
		    			    obj.update({
		    			    	roleName:role.roleName
		    			    });
			    		 }else{
			    			 layer.alert(res.msg);
			    		 }
			    		//清除操作框内容
						layer.close(index);
			    	 },
			    	 error:function(){
			    		 layer.alert("数据更新失败！")
			    	 }
			      });
			},
			btn2:function(index,layero){//点击取消的回调函数
				//清除操作框内容
				layer.close(index);
			}
		})
	  }
	});
};

(function(){
	layui.use(['element','layer','form','table'], function(){
		var layer = layui.layer;
		var table = layui.table;
		var form = layui.form;
		WSM.role.init(table);
		WSM.role.bind(table,layer,form);
	})
}());
