var WSM = parent.window.WSM;
WSM.subscription = {};

WSM.subscription.init = function(table,layer,form){
	//初始化操作框accountId,serviceId
	WSM.subscription.initOperate(layer,form);
	//初始化表格
	WSM.subscription.initTable("#subscriptiontb",table);
};
/**
 * 初始化操作框accountId,serviceId
 * @param layer
 */
WSM.subscription.initOperate = function(layer,form){
	$.ajax({
		type:'post',
		url:'../subscription.do?getSubscriptionInfoDetail',
		async:true,
		dataType:'json',
		success:function(res){
			 if(res.code==0){
				 var account = res.data.accounts,
				 	 service = res.data.services;
				 for ( var element in account) {
					 var option = $("<option>").val(account[element].Id).text(account[element].SystemName+","+account[element].SystemCode)
					 $("#AccountId").append(option);
				 }
				 for ( var element in service) {
					 var option = $("<option>").val(service[element].Id).text(service[element].ServiceName+","+service[element].ServiceCode)
					 $("#ServiceId").append(option);
				 }
				 form.render(); //更新全部
			 }else{
				 layer.alert(res.msg);
			 }
		},
		error:function(){
			 layer.alert("初始化操作框失败");
		}
	});
};
/**
 * 
 * @param $tableId 表格的id
 * @param layuitable layui必须的table模块
 */
WSM.subscription.initTable = function($tableId,table){
	table.render({
		elem:$tableId,
		height:'full-110',
		url:'../subscription.do?getSubScriptionData',
		page:true,
		cols:[[
		   {field:'SubScriptionId',title:'ID',width:80,sort:true},
		   {field:'AccountId',title:'账户ID',sort:true},
		   {field:'SystemName',title:'系统名称',sort:true},
		   {field:'SystemCode',title:'系统密码',sort:true},
		   {field:'ServiceId',title:'服务ID',sort:true},
		   {field:'ServiceName',title:'服务名称',sort:true},
		   {field:'ServiceCode',title:'服务密码',sort:true},
		   {title:'操作',toolbar:'#barSubScription',fixed:'right'}
		]]
	});
};
WSM.subscription.bind = function(table,layer,form){
	//监听工具条
	table.on('tool(subscriptiontb)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
	  var data = obj.data; //获得当前行数据
	  var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
	  var tr = obj.tr; //获得当前行 tr 的DOM对象
	  if(layEvent === 'del'){ //删除
	    layer.confirm('真的删除行么', function(index){
	      layer.close(index);
	      //向服务端发送删除指令
	      var id = data.SubScriptionId;
	      $.ajax({
	    	 type:'post',
	    	 url:'../subscription.do?deleteSubScriptionById&Id='+id,
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
		var id = data.SubScriptionId;
		//赋值到编辑框
		$("#AccountId").val(data.AccountId);
		$("#ServiceId").val(data.ServiceId);
		form.render('select');
		//打开编辑窗口
		layer.open({
			type:1,
			content:$("#operate"),
			title:"编辑",
			btn:["提交","取消"],
			btnAlign:'c',
			yes:function(index,layero){//点击确认的回调函数
				//获得编辑后的值
				var subscription = {
					AccountId:$("#AccountId").next().find("dd[class='layui-this']").attr("lay-value"),
					ServiceId:$("#ServiceId").next().find("dd[class='layui-this']").attr("lay-value")
				};
				layer.close(index);
				//提交信息
				$.ajax({
			    	 type:'post',
			    	 url:'../subscription.do?editSubScriptionById&Id='+id,
			    	 data:subscription,
			    	 async:true,
			    	 dataType:'json',
			    	 success:function(res){
			    		 if(res.code==0){
			    			layer.msg("操作成功");
			    			//同步更新缓存对应的值
			    			WSM.subscription.initTable("#subscriptiontb",table);
			    		 }else{
			    			 layer.alert(res.msg);
			    		 }
			    		//清除操作框内容
			    		//清除操作框内容
						$("#AccountId").next().find("dd[class='layui-this']").removeClass("layui-this");
						$("#ServiceId").next().find("dd[class='layui-this']").removeClass("layui-this");
						layer.close(index);
			    	 },
			    	 error:function(){
			    		 layer.alert("数据更新失败！")
			    	 }
			      });
			},
			btn2:function(index,layero){//点击取消的回调函数
				//清除操作框内容
				$("#AccountId").next().find("dd[class='layui-this']").removeClass("layui-this");
				$("#ServiceId").next().find("dd[class='layui-this']").removeClass("layui-this");
				layer.close(index);
			}
		})
	  }
	});
	$("#addSubScription").click(function(){
		layer.open({
			type:1,
			content:$("#operate"),
			title:"新增",
			btn:["提交","取消"],
			btnAlign:'c',
			yes:function(index,layero){//点击确认的回调函数
				var subscription = {
						AccountId:$("#AccountId").next().find("dd[class='layui-this']").attr("lay-value"),
						ServiceId:$("#ServiceId").next().find("dd[class='layui-this']").attr("lay-value")
					};
				layer.close(index);
				//提交信息
				$.ajax({
			    	 type:'post',
			    	 url:'../subscription.do?addSubScription',
			    	 data:subscription,
			    	 async:true,
			    	 dataType:'json',
			    	 success:function(res){
			    		 if(res.code==0){
			    			layer.msg("操作成功");
			    			//初始化表格
			    			WSM.subscription.initTable("#subscriptiontb",table);
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
		WSM.subscription.init(table,layer,form);
		WSM.subscription.bind(table,layer,form);
	})
}());
