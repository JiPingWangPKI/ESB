var WSM = parent.window.WSM;
WSM.workflow = {};
var streambaseServer="http://127.0.0.1:7000/";//SB服务所在机器对外开放的api接口供平台调用测试

WSM.workflow.init = function(layer,form){
	//初始化卡片
	$('.gridly').gridly({
	    base: 60,
	    gutter: 20,
	    columns: 12
	});
	//初始化所有的workflow
	WSM.workflow.initAllWorkflow_new(form,layer,streambaseServer);
};
WSM.workflow.initAllWorkflow=function(form,layer,streambaseServer){
	var sbip = streambaseServer.match("(?<=http://).*(?=:)")[0];
	$(".iptitle").text(sbip);
	$(".gridly").empty();
	//获得所有workflow配置
	$.ajax({
		async:'true',
		type:'post',
		url:'../workflow.do?getWorkflowData',
		data:{streambaseServer:sbip},
		success:function(data){
			if(data.code==0){
				var data = data.data;
				for(var i=0;i<data.length;i++){
					var workflow = {
							name:data[i].name,
							serviceName:data[i].serviceName,
							workFlowName:data[i].workFlowName,
							state:data[i].state==0?true:false,
							callId:data[i].requestIp,
							targetId:data[i].targetIp,
							platformId:data[i].platformIp,
							authority:data[i].authority==0?true:false,
							highCostTimeSum:data[i].highCostTimeSum,
							failRequestSum:data[i].failSum,
							hangUpSum:3//以后可以添加
						};
					var workflowhtml =
						 "<div class='workflow small'>" +
						 " 	<label class='workflow_titel'>"+workflow.name+"</label>" +
						 "	<span class='workflowclose'>X</span>" +
						 "	<hr/>" +
						 "	<div class='workflow_onoff'>" +
						 "		<div class='layui-form-item layui-form'>" +
						 "			<label class='workflow_onoff_label'>服务</label>" +
						 "			<input type='checkbox' class='workflow_onoff_control' lay-filter='workflow_onoff' lay-skin='switch' lay-text='on|off' "+(workflow.state?" checked ":" ")+">" +
						 "		</div>" +
						 "	</div>" +
						 "	<div class='serviceName'>" +
						 "		<label class='serviceName_label'>服务名称：</label>" +
						 "		<label class='serviceName_content'>"+workflow.serviceName+"</label>" +
						 "	</div>" +
						 "	<div class='workFlowName'>" +
						 "		<label class='workFlowName_label'>事物流实例：</label>" +
						 "		<label class='workFlowName_content'>"+workflow.workFlowName+"</label>" +
						 "	</div>" +
						 "	<div class='workflow_serviceip'>" +
						 "		<div class='workflow_serviceipfrom'>" +
						 "			<label class='workflow_serviceipfrom_label'>调用方地址：</label>" +
						 "			  			<label class='workflow_serviceipfrom_content'>"+workflow.callId+"</label>" +
						 "		</div>" +
						 "		<div class='workflow_serviceipto'>" +
						 "			<label class='workflow_serviceipto_label'>转发地址：</label>" +
						 "			<label class='workflow_serviceipto_content'>"+workflow.targetId+"</label>" +
						 "		</div>" +
						 "		<div class='workflow_serviceipmy'>" +
						 "			<label class='workflow_serviceipmy_label'>平台开发地址：</label>" +
						 "			<label class='workflow_serviceipmy_content'>"+workflow.platformId+"</label>" +
						 "		</div>" +
						 "	</div>" +
						 "	<div class='workflow_useauthority'>" +
						 "		<label class='workflow_useauthority_label'>权限验证已<span class='workflow_useauthority_content'>"+(workflow.authority?"开启":"关闭")+"</span></label>" +
						 "	</div>" +
						 " <div class='workflow_highcosttime'>" +
						 "	 <label class='workflow_highcosttime_label'>高耗时警告（时间段,当天）<span class='layui-badge'>"+workflow.highCostTimeSum+"</span></label>" +
						 " </div>" +
						 " <div class='worlflow_failrequest'>" +
						 "	 <label class='worlflow_failrequest_label'>失败请求（当天）<span class='layui-badge'>"+workflow.failRequestSum+"</span></label>" +
						 " </div>" +
						 " <div class='worlflow_hangup'>" +
						 "	 <label class='worlflow_hangup_label'>挂起服务（当天）<span class='layui-badge'>"+workflow.hangUpSum+"</span></label>" +
						 " </div>" +
						 " <button class='layui-btn layui-btn-xs editworkflow'>修改配置</button>" +
						 "</div>";
					$(".gridly").append(workflowhtml);
				}
			}else{
				layer.alert(data.msg);
			}
			$('.gridly').gridly();
			form.render();
		},
		error:function(){
			layer.alert("数据获取失败！");
		}
	});
}
WSM.workflow.initAllWorkflow_new=function(form,layer,streambaseServer){
	var sbip = streambaseServer.match("(?<=http://).*(?=:)")[0];
	$(".iptitle").text(sbip);
	$(".gridly").empty();
	//获得所有workflow配置
	$.ajax({
		async:'true',
		type:'post',
		dataType:'jsonp',
		jsonp:'callback',//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
		jsonpCallback:"getResult",//自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以不写这个参数，jQuery会自动为你处理数据
		url:streambaseServer+'PKISignalServer/Api/getWorkflowData',//'../workflow.do?getWorkflowData',
		success:function(data){
			if(data.result.code==0){
				var data = data.result.obj;
				for(var i=0;i<data.length;i++){
					var workflow = {
							name:data[i].serviceName,
							serviceName:data[i].serviceName,
							workFlowName:data[i].workFlowName,
							state:data[i].serviceState,
							callId:data[i].requestIPAddress,
							targetId:data[i].urlReplaceTo,
							platformId:data[i].urlReplaceFrom,
							platformId_vip:data[i].urlReplaceFrom1,
							authority:data[i].authoritys=="true"?true:false,
						};
					var workflowhtml =
						 "<div class='workflow small'>" +
						 " 	<label class='workflow_titel'>"+workflow.name+"</label>" +
						 "	<span class='workflowclose'>X</span>" +
						 "	<hr/>" +
						 "	<div class='workflow_onoff'>" +
						 "		<div class='layui-form-item layui-form'>" +
						 "			<label class='workflow_onoff_label'>服务</label>" +
						 "			<input type='checkbox' class='workflow_onoff_control' lay-filter='workflow_onoff' lay-skin='switch' lay-text='on|off' "+(workflow.state?" checked ":" ")+">" +
						 "		</div>" +
						 "	</div>" +
						 "	<div class='serviceName'>" +
						 "		<label class='serviceName_label'>服务名称：</label>" +
						 "		<label class='serviceName_content'>"+workflow.serviceName+"</label>" +
						 "	</div>" +
						 "	<div class='workFlowName'>" +
						 "		<label class='workFlowName_label'>事物流实例：</label>" +
						 "		<label class='workFlowName_content'>"+workflow.workFlowName+"</label>" +
						 "	</div>" +
						 "	<div class='workflow_serviceip'>" +
						 "		<div class='workflow_serviceipfrom'>" +
						 "			<label class='workflow_serviceipfrom_label'>调用方地址：</label>" +
						 "			  			<label class='workflow_serviceipfrom_content'>"+workflow.callId+"</label>" +
						 "		</div>" +
						 "		<div class='workflow_serviceipto'>" +
						 "			<label class='workflow_serviceipto_label'>转发地址：</label>" +
						 "			<label class='workflow_serviceipto_content'>"+workflow.targetId+"</label>" +
						 "		</div>" +
						 "		<div class='workflow_serviceipmy'>" +
						 "			<label class='workflow_serviceipmy_label'>平台开放地址：</label>" +
						 "			<label class='workflow_serviceipmy_content'>"+workflow.platformId+"</label>" +
						 "		</div>" +
						 "		<div class='workflow_serviceipmy_vip'>" +
						 "			<label class='workflow_serviceipmy_vip_label'>负载均衡地址：</label>" +
						 "			<label class='workflow_serviceipmy_vip_content'>"+workflow.platformId_vip+"</label>" +
						 "		</div>" +
						 "	</div>" +
						 "	<div class='workflow_useauthority'>" +
						 "		<label class='workflow_useauthority_label'>权限验证已：<span class='workflow_useauthority_content'>"+(workflow.authority?"开启":"关闭")+"</span></label>" +
						 "	</div>" +
						 " <button class='layui-btn layui-btn-xs editworkflow'>修改配置</button>" +
						 "</div>";
					$(".gridly").append(workflowhtml);
				}
			}else{
				layer.alert(data.msg);
			}
			$('.gridly').gridly();
			form.render();
		},
		error:function(){
			layer.alert("数据获取失败！");
		}
	});
}
	
WSM.workflow.bind = function(layer,form){
	//监听streambase服务地址切换
	form.on('select(IpAddressSwitch)',function(data){
		streambaseServer = "http://"+data.value+":7000/"
		WSM.workflow.initAllWorkflow_new(form,layer,streambaseServer);
	});
	//监听开关
	form.on('switch(workflow_onoff)',function(data){
		var serviceName=$(this.parentNode.parentNode.parentNode.getElementsByClassName("serviceName_content")).text(),
			method=(data.elem.checked?"on":"off"),
			$this=$(this);
		$.ajax({
			async:false,
			type:'post',
			url:streambaseServer+'PKISignalServer/Api/switchSBServiceState',
			data:{ServiceName:serviceName,Method:method},
			dataType:'jsonp',
			jsonp:'callback',//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
			jsonpCallback:"getResult",//自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以不写这个参数，jQuery会自动为你处理数据
			success:function(data1){
				if(data1.result.code==0){
					layer.msg("切换开关指令发送成功！")
				}else{
					//服务到原来状态
					layer.alert(data1.result.msg);
				}
			},
			error:function(){
				//服务到原来状态
				layer.alert("切换开关指令发送失败！")
			}
		});
	});
	//添加workflow
	$(".addworkflow").click(function(){
		$("#Name").val("");
		$("#ServiceName").val("");
		$("#WorkFlowName").val("");
		$("#RequestIP").val("");
		$("#TargetIp").val("");
		$("#StreambaseIp").val("");
		$("#UseAuthority").next().removeClass('layui-form-onswitch'); 
		$("#UseAuthority").next().find("em").text("关闭");
		layer.open({
			type:1,
			content:$("#operate"),
			title:'新增',
			btn:['提交','取消'],
			btnAlign:'c',
			yes:function(index,layero){//点击确认的回调函数
				var data = {
					name:$("#Name").val(),
					serviceName:$("#ServiceName").val(),
					workFlowName:$("#WorkFlowName").val(),
					state:1,
					requestIp:$("#RequestIP").val(),
					targetIp:$("#TargetIp").val(),
					streambaseIp:$("#StreambaseIp").val(),
					streambaseIp_vip:$("#StreambaseIp_vip").val(),
					authority:$("#UseAuthority").next().find("em").text()=="开启"?0:1,
					streambaseServer:$(".iptitle").text()
				};
				$.ajax({
					async:true,
					type:'post',
					url:'../workflow.do?addWorkFlow',
					data:data,
					success:function(data){
						if(data.code==0){
							//刷新workflow页面
							WSM.workflow.initAllWorkflow_new(form,layer,streambaseServer);
						}else{
							layer.alert(data.msg);
						}
					},
					error:function(){
						layer.alert("workflow配置本地保存失败！");
					}
				});
				layer.close(index);
			},
			btn2:function(index,layero){
				layer.close(index);
			}
		})
	});
	$(document).on('click','.editworkflow',function(){//编辑器
		//对operate赋值
		var name = $(this).parent().find("[class='workflow_titel']").text(),
			serviceName = $(this).parent().find("[class='serviceName_content']").text(),
			workFlowName = $(this).parent().find("[class='workFlowName_content']").text(),
			state = $(this).parent().find("[class='workflow_onoff_control']").next().find("em").text(),
			requestIp = $(this).parent().find("[class='workflow_serviceipfrom_content']").text(),
			targetIp = $(this).parent().find("[class='workflow_serviceipto_content']").text(),
			streambaseIp = $(this).parent().find("[class='workflow_serviceipmy_content']").text(),
			streambaseIp_vip = $(this).parent().find("[class='workflow_serviceipmy_vip_content']").text(),
			useAthority = $(this).parent().find("[class='workflow_useauthority_content']").text();
		$("#Name").val(name);
		$("#ServiceName").val(serviceName),
		$("#WorkFlowName").val(workFlowName),
		$("#State").text(state),
		$("#RequestIP").val(requestIp);
		$("#TargetIp").val(targetIp);
		$("#StreambaseIp").val(streambaseIp);
		$("#StreambaseIp_vip").val(streambaseIp_vip);
		if(useAthority=="开启"){
			$("#UseAuthority").next().addClass('layui-form-onswitch'); 
			$("#UseAuthority").next().find("em").text("开启");
		}else{
			$("#UseAuthority").next().removeClass('layui-form-onswitch'); 
			$("#UseAuthority").next().find("em").text("关闭");
		}
		layer.open({
			type:1,
			content:$('#operate'),
			title:'编辑',
			btn:['提交','取消'],
			btnAlign:'c',
			yes:function(index,layero){//点击确认的回调函数
				//改变streambase服务的配置
				var data = {
					name:$("#Name").val(),
					serviceName:$("#ServiceName").val(),
					workFlowName:$("#WorkFlowName").val(),
					requestIp:$("#RequestIP").val(),
					targetIp:$("#TargetIp").val(),
					platformIp:$("#StreambaseIp").val(),
					platformIp_vip:$("#StreambaseIp_vip").val(),
					authority:($("#UseAuthority").next().find("em").text()=="开启")?true:false,
					state:$("#State")=="on"?0:1,
					streambaseServer:$(".iptitle").text()
				};
				$.ajax({
					async:true,
					type:'post',
					url:streambaseServer+'PKISignalServer/Api/modifyWorkFlowParams',
					data:data,
					dataType:'jsonp',
					jsonp:'callback',//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
					jsonpCallback:"getResult",//自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以不写这个参数，jQuery会自动为你处理数据
					success:function(result){
						if(result.result.code==0){
							//workflow配置本地
							/*$.ajax({
								async:true,
								type:'post',
								data:data,
								url:'../workflow.do?editWorkflowByWorkFlowName',
								success:function(data){
									if(data.code==0){
										WSM.workflow.initAllWorkflow_new(form,layer,streambaseServer);
										layer.msg("workflow配置本地已保存");
									}else{
										layer.alert(data.msg);
									}
								},
								error:function(){
									layer.alert("workflow配置本地保存失败！");
								}
							});*/
							layer.msg("服务配置修改成功！（配置生效需要服务重启）");
						}else{
							layer.alert(result.result.msg);
						}
					}
				});
				layer.close(index);
			},
			btn2:function(index,layero){
				layer.close(index);
			}
		});
	});
	
	//监听删除workflow配置
	$(document).on('click','.workflowclose',function(){
		var $this = $(this);
		layer.confirm('确认删除吗', function(index){
		    layer.close(index);
		    //删除本地存储
			var workFlowName = $this.parent().find("[class='workFlowName']").text(),
				streambaseServerIp = $(".iptitle").text();
			$.ajax({
				async:true,
				type:'post',
				url:'../workflow.do?deleteWorkFlowByWorkFlowName',
				data:{WorkFlowName:workFlowName,streambaseServer:streambaseServerIp},
				success:function(data){
					if(data.code==0){
						WSM.workflow.initAllWorkflow_new(form,layer,streambaseServer);
					}else{
						layer.alert(data.msg);
					}
				},
				error:function(){
					layer.alert("此workflow配置删除失败！");
				}
			});
		    });
	});
	
};
(function(){
	layui.use(['element','form','layer'], function(){
		var layer = layui.layer;
		var form = layui.form;
		WSM.workflow.init(layer,form);
		WSM.workflow.bind(layer,form);
	})
}());
