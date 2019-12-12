var WSM = parent.window.WSM;
WSM.service = {};

WSM.service.init = function(table){
	//初始化表格
	WSM.service.initTable("#servicetb",table);
};
/**
 * 
 * @param $tableId 表格的id
 * @param layuitable layui必须的table模块
 */
WSM.service.initTable = function($tableId,table){
	table.render({
		elem:$tableId,
		height:'full-110',
		url:'../service.do?getServiceData',
		page:true,
		cols:[[
		   {field:'Id',title:'ID',width:80,sort:true},
		   {field:'ServiceName',title:'服务名称',sort:true},
		   {field:'ServiceCode',title:'服务编码',sort:true},
		   {field:'MessageType',title:'服务类型',sort:true,
			   templet:function(d){
				   if(d.ServiceCode==1){
					   return "推";
				   }else if(d.ServiceCode==2){
					   return "拿";
				   }else{
					   return "其他";
				   }
			   }},
		   {field:'Remark',title:'备注',sort:true},
		   {field:'ServiceAddress',title:'服务地址',sort:true},
		   {field:'ServiceNamespace',title:'服务命名空间',sort:true},
		   {title:'操作',toolbar:'#barService',fixed:'right'}
		]]
	});
};
WSM.service.bind = function(table,layer,form){
	//监听工具条
	table.on('tool(servicetb)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
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
	    	 url:'../service.do?deleteServiceById&Id='+id,
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
	      obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
	    });
	  } else if(layEvent === 'edit'){ //编辑
		var id = data.Id;
		//赋值到编辑框
		$("#ServiceName").val(data.ServiceName);
		$("#ServiceCode").val(data.ServiceCode);
		$(":radio[name='MessageType'][value='" + data.MessageType + "']").attr("checked", "checked");
		$("#Remark").val(data.Remark);
		$("#ServiceAddress").val(data.ServiceAddress);
		$("#ServiceNamespace").val(data.ServiceNamespace);
		form.render();//更新全部
		//打开编辑窗口
		layer.open({
			type:1,
			content:$("#operate"),
			title:"编辑",
			btn:["提交","取消"],
			btnAlign:'c',
			yes:function(index,layero){//点击确认的回调函数
				var service={
					ServiceName:$("#ServiceName").val(),
					ServiceCode:$("#ServiceCode").val(),
					MessageType:$(":radio[name='MessageType']:checked").val(),
					Remark:$("#Remark").val(),
					ServiceAddress:$("#ServiceAddress").val(),
					ServiceNamespace:$("#ServiceNamespace").val(),
				};
				layer.close(index);
				//提交信息
				$.ajax({
			    	 type:'post',
			    	 url:'../service.do?editServiceById&Id='+id,
			    	 data:service,
			    	 async:true,
			    	 dataType:'json',
			    	 success:function(res){
			    		 if(res.code==0){
			    			layer.msg("操作成功");
			    			//同步更新缓存对应的值
		    			    obj.update({
		    			    	ServiceName:service.ServiceName,
		    			    	ServiceCode:service.ServiceCode,
		    			    	MessageType:service.MessageType,
		    			    	Remark:service.Remark,
		    			    	ServiceAddress:service.ServiceAddress,
		    			    	ServiceNamespace:service.ServiceNamespace
		    			    });
			    		 }else{
			    			 layer.alert(res.msg);
			    		 }
			    		//清除操作框内容
						$("#ServiceName").val("");
						$("#ServiceCode").val("");
						$("#Remark").val("");
						$("#ServiceAddress").val("");
						$("#ServiceNamespace").val("");
						layer.close(index);
			    	 },
			    	 error:function(){
			    		 layer.alert("数据更新失败！")
			    	 }
			      });
			},
			btn2:function(index,layero){//点击取消的回调函数
				//清除操作框内容
				$("#ServiceName").val("");
				$("#ServiceCode").val("");
				$("#Remark").val("");
				$("#ServiceAddress").val("");
				$("#ServiceNamespace").val("");
				layer.close(index);
			}
		})
	  }
	});
	$("#addService").click(function(){
		layer.open({
			type:1,
			content:$("#operate"),
			title:"新增",
			btn:["提交","取消"],
			btnAlign:'c',
			yes:function(index,layero){//点击确认的回调函数
				var service={
					ServiceName:$("#ServiceName").val(),
					ServiceCode:$("#ServiceCode").val(),
					MessageType:$(":radio[name='MessageType']").val(),
					Remark:$("#Remark").val(),
					ServiceAddress:$("#ServiceAddress").val(),
					ServiceNamespace:$("#ServiceNamespace").val(),
				};
				layer.close(index);
				//提交信息
				$.ajax({
			    	 type:'post',
			    	 url:'../service.do?addService',
			    	 data:service,
			    	 async:true,
			    	 dataType:'json',
			    	 success:function(res){
			    		 if(res.code==0){
			    			layer.msg("操作成功");
			    			//初始化表格
			    			WSM.service.initTable("#servicetb",table);
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
		var form = layui.form
		WSM.service.init(table);
		WSM.service.bind(table,layer,form);
	})
}());
