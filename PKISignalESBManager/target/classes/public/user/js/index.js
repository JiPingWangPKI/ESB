var WSM = parent.window.WSM;
WSM.user = {};
WSM.user.init = function(table,form){
	//初始化表格
	WSM.user.initTable("#usertb",table);
	//初始化角色下拉框
	WSM.user.initRoles(form);
};
/**
 * 初始化角色下拉框
 */
WSM.user.initRoles = function(form){
	$.ajax({
		async:true,
		type:'get',
		url:'../role.do?getAllRoles',
		dataType:'json',
		success:function(data){
			if(data.code==0){
				var roles = data.data;
				//把每个角色添加到下拉框中，
				for(var i=0;i<roles.length;i++){
					var append = "<option value='"+roles[i].id+"'>"+roles[i].name+"</option>"
					$("#roles").append(append);
				}
				form.render();
			}else{
				layer.alert(data.msg);
			}
		},
		error:function(){
			layer.alert("获取角色失败！");
		}
	})
};
/**
 * 
 * @param $tableId 表格的id
 * @param layuitable layui必须的table模块
 */
WSM.user.initTable = function($tableId,table){
	table.render({
		elem:$tableId,
		height:'full-110',
		url:'../user.do?getUserData',
		page:true,
		cols:[[
		   {field:'id',title:'ID',width:80,sort:true},
		   {field:'username',title:'UserName',sort:true},
		   {field:'roleNames',title:'roles',sort:true},
		   {field:'cellphone',title:'Cellphone',sort:true},
		   {field:'email',title:'Email',sort:true},
		   {title:'操作',toolbar:'#barUser',fixed:'right'}
		]]
	});
};
WSM.user.bind = function(table,layer,form){
	$(document).on('click','.roleClear',  function() {//当点击这个按钮时删除按钮父级元素role
		$(this).parent().remove();
	});
	//监听开关
	form.on('switch(password)',function(data){
		if(data.elem.checked){//修改密码
			$("#PassWord").data("ismodify",true).show();
		}else{//密码不变
			$("#PassWord").data("ismodify",false).hide();
		}
	});
	//监听select
	form.on('select(roles)',function(data){
		var roleId=data.value,
			roleName = data.elem[data.elem.selectedIndex].text,
			flag=true;
		$(".forroles").each(function(){
			if($(this).attr('value')==roleId){
				flag=false;
			}
		});
		//添加到编辑框角色栏中
		if(flag){
			var role = "<div class='forroles' value='"+roleId+"'><div class='roleName'>"+roleName+"</div><div class='roleClear'>x<div></div>"
			$("#forroles").append(role);
		}
	});
	//监听工具条
	table.on('tool(usertb)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
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
	    	 url:'../user.do?deleteUserById&Id='+id,
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
		var id = data.id;
		$("#password").next().show();
		layui.jquery('input[name="password"]').next().removeClass('layui-form-onswitch'); 
		layui.jquery('input[name="password"]').next().find("em").text("不变");
		$("#PassWord").data("ismodify",false).hide();
		//赋值到编辑框
		$("#UserName").val(data.username);
		$("#PassWord").val(data.password);
		$("#CellPhone").val(data.cellphone);
		$("#Email").val(data.email);
		//赋值权限
		$("#forroles").empty();
		var roles = (data.roleuser==null?"":data.roleuser.split(","));
		for(var i=0;i<roles.length;i++){
			var role=roles[i].split(":"),
				roleId = role[0],
				roleName=role[1]
				append = "<div class='forroles' value='"+roleId+"'><div class='roleName'>"+roleName+"</div><div class='roleClear'>x<div></div>"
			$("#forroles").append(append);
		}
		//打开编辑窗口
		layer.open({
			type:1,
			content:$("#operate"),
			title:"编辑",
			btn:["提交","取消"],
			btnAlign:'c',
			yes:function(index,layero){//点击确认的回调函数
				//获得用户权限
				var roleIds=new Array(),
					roleNames = new Array(),
					roleuser = new Array();
				$(".forroles").each(function(){
					var roleId=$(this).attr('value');
					roleIds.push(roleId);
				});
				$(".roleName").each(function(){
					var roleName=$(this).text();
					roleNames.push(roleName);
				});
				for (var i = 0; i < roleIds.length; i++) {
					var ele = roleIds[i]+":"+roleNames[i];
					roleuser.push(ele);
				}
				roleIds=roleIds.join(",");
				roleNames=roleNames.join(",");
				roleuser=roleuser.join(",");
				var user={
					UserName:$("#UserName").val(),
					PassWord:$("#PassWord").val(),
					CellPhone:$("#CellPhone").val(),
					Email:$("#Email").val(),
					RoleIds:roleIds,
					isModify:$("#PassWord").data("ismodify")
				};
				layer.close(index);
				//提交信息
				$.ajax({
			    	 type:'post',
			    	 url:'../user.do?editUserById&Id='+id,
			    	 data:user,
			    	 async:true,
			    	 dataType:'json',
			    	 success:function(res){
			    		 if(res.code==0){
			    			layer.msg("操作成功");
			    			//同步更新缓存对应的值
		    			    obj.update({
		    			    	username:user.UserName,
		    					password:user.PassWord,
		    					cellphone:user.CellPhone,
		    					email:user.Email,
		    					roleuser:roleuser,
		    					roleNames:roleNames,
		    					roleIds:roleIds
		    			    });
			    		 }else{
			    			 layer.alert(res.msg);
			    		 }
			    		//清除操作框内容
						$("#UserName").val("");
						$("#PassWord").val("");
						$("#CellPhone").val("");
						$("#Email").val("");
						layer.close(index);
			    	 },
			    	 error:function(){
			    		 layer.alert("数据更新失败！")
			    	 }
			      });
			},
			btn2:function(index,layero){//点击取消的回调函数
				//清除操作框内容
				$("#UserName").val("");
				$("#PassWord").val("");
				$("#CellPhone").val("");
				$("#Email").val("");
				layer.close(index);
			}
		})
	  }
	});
	$("#addUser").click(function(){
		$("#password").hide();
		$("#password").next().hide();
		$("#PassWord").show();
		$("#PassWord").val("123456");
		$("#CellPhone").val("");
		$("#Email").val("");
		$("#UserName").val("");
		$("#forroles").empty();
		layer.open({
			type:1,
			content:$("#operate"),
			title:"新增",
			btn:["提交","取消"],
			btnAlign:'c',
			yes:function(index,layero){//点击确认的回调函数
				//获得用户角色
				var roleIds=new Array();
				$(".forroles").each(function(){
					var roleId=$(this).attr('value');
					roleIds.push(roleId);
				});
				roleIds=roleIds.join(",")
				var user={
					UserName:$("#UserName").val(),
					PassWord:$('#PassWord').val(),
					RoleIds:roleIds,
					CellPhone:$("#CellPhone").val(),
					Email:$("#Email").val()
				};
				layer.close(index);
				//提交信息
				$.ajax({
			    	 type:'post',
			    	 url:'../user.do?addUser',
			    	 data:user,
			    	 async:true,
			    	 dataType:'json',
			    	 success:function(res){
			    		 if(res.code==0){
			    			layer.msg("操作成功");
			    			//初始化表格
			    			WSM.user.initTable("#usertb",table);
			    		 }else{
			    			 layer.alert(res.msg);
			    		 }
			    	 },
			    	 error:function(){
			    		 layer.alert("数据更新失败！")
			    	 }
			      });
			},
			btn2:function(index,layero){//点击取消的回调函数
				layer.close(index);
			}
		})
	});
};

(function(){
	layui.use(['element','layer','form','table'], function(){
		var layer = layui.layer;
		var table = layui.table;
		var form = layui.form;
		WSM.user.init(table,form);
		WSM.user.bind(table,layer,form);
	})
}());
