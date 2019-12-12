var WSM = parent.window.WSM;
WSM.account = {};

WSM.account.init = function(table){
	//初始化表格
	WSM.account.initTable("#accounttb",table);
};
/**
 * 
 * @param $tableId 表格的id
 * @param layuitable layui必须的table模块
 */
WSM.account.initTable = function($tableId,table){
	table.render({
		elem:$tableId,
		height:'full-110',
		url:'../account.do?getAccountData',
		page:true,
		cols:[[
		   {field:'Id',title:'ID',width:80,sort:true},
		   {field:'SystemName',title:'SystemName',sort:true},
		   {field:'Password',title:'Password',sort:true},
		   {field:'SystemCode',title:'SystemCode',sort:true},
		   {field:'ContactName',title:'ContactName',sort:true},
		   {field:'Cellphone',title:'Cellphone',sort:true},
		   {field:'Email',title:'Email',sort:true},
		   {field:'Remark',title:'Remark',sort:true,width:200},
		   {title:'操作',toolbar:'#barAcount',fixed:'right'}
		]]
	});
};
WSM.account.bind = function(table,layer){
	//监听工具条
	table.on('tool(accounttb)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
	  var data = obj.data; //获得当前行数据
	  var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
	  var tr = obj.tr; //获得当前行 tr 的DOM对象
	  if(layEvent === 'del'){ //删除
	    layer.confirm('真的删除行么', function(index){
	      layer.close(index);
	      //向服务端发送删除指令
	      var id = data.Id;
	      $.ajax({
	    	 type:'post',
	    	 url:'../account.do?deleteAccountById&Id='+id,
	    	 async:true,
	    	 dataType:'json',
	    	 success:function(res){
	    		 if(res.code==0){
	    			 layer.msg("操作成功");
	    		 }else{
	    			 layer.alert(res.msg);
	    		 }
	    	 },
	    	 error:function(){
	    		 layer.alert("数据删除失败！")
	    	 }
	    	 
	      });
	      obj.del(); //删除对应行（tr）的DOM结构，并更新缓存s
	    });
	  } else if(layEvent === 'edit'){ //编辑
		var id = data.Id;
		//赋值到编辑框
		$("#SystemName").val(data.SystemName);
		$("#Password").val(data.Password);
		$("#SystemCode").val(data.SystemCode);
		$("#ContactName").val(data.ContactName);
		$("#Cellphone").val(data.Cellphone);
		$("#Email").val(data.Email);
		$("#Remark").val(data.Remark);
		//打开编辑窗口
		layer.open({
			type:1,
			content:$("#operate"),
			title:"编辑",
			btn:["提交","取消"],
			btnAlign:'c',
			yes:function(index,layero){//点击确认的回调函数
				var account={
					SystemName:$("#SystemName").val(),
					Password:$("#Password").val(),
					SystemCode:$("#SystemCode").val(),
					ContactName:$("#ContactName").val(),
					Cellphone:$("#Cellphone").val(),
					Email:$("#Email").val(),
					Remail:$("#Remark").val()
				};
				layer.close(index);
				//提交信息
				$.ajax({
			    	 type:'post',
			    	 url:'../account.do?editAccountById&Id='+id,
			    	 data:account,
			    	 async:true,
			    	 dataType:'json',
			    	 success:function(res){
			    		 if(res.code==0){
			    			layer.msg("操作成功");
			    			//同步更新缓存对应的值
		    			    obj.update({
		    			    	SystemName:account.SystemName,
		    					SystemCode:account.SystemCode,
		    					ContactName:account.ContactName,
		    					Cellphone:account.Cellphone,
		    					Email:account.Email,
		    					Remail:account.Remail
		    			    });
			    		 }else{
			    			 layer.alert(res.msg);
			    		 }
			    		//清除操作框内容
						$("#SystemName").val("");
						$("#SystemCode").val("");
						$("#ContactName").val("");
						$("#Cellphone").val("");
						$("#Email").val("");
						$("#Remark").val("");
						layer.close(index);
			    	 },
			    	 error:function(){
			    		 layer.alert("数据更新失败！")
			    	 }
			      });
			},
			btn2:function(index,layero){//点击取消的回调函数
				//清除操作框内容
				$("#SystemName").val("");
				$("#SystemCode").val("");
				$("#ContactName").val("");
				$("#Cellphone").val("");
				$("#Email").val("");
				$("#Remark").val("");
				layer.close(index);
			}
		})
	  }
	});
	$("#addAccount").click(function(){
		layer.open({
			type:1,
			content:$("#operate"),
			title:"新增",
			btn:["提交","取消"],
			btnAlign:'c',
			yes:function(index,layero){//点击确认的回调函数
				var account={
					SystemName:$("#SystemName").val(),
					Password:$('#Password').val(),
					SystemCode:$("#SystemCode").val(),
					ContactName:$("#ContactName").val(),
					Cellphone:$("#Cellphone").val(),
					Email:$("#Email").val(),
					Remail:$("#Remark").val()
				};
				layer.close(index);
				//提交信息
				$.ajax({
			    	 type:'post',
			    	 url:'../account.do?addAccount',
			    	 data:account,
			    	 async:true,
			    	 dataType:'json',
			    	 success:function(res){
			    		 if(res.code==0){
			    			layer.msg("操作成功");
			    			//初始化表格
			    			WSM.account.initTable("#accounttb",table);
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
		WSM.account.init(table);
		WSM.account.bind(table,layer);
	})
}());
