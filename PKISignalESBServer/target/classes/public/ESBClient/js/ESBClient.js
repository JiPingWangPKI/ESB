var Perkinelmer={};
Perkinelmer.ESBClient={};
Perkinelmer.Utils ={};
Perkinelmer.Utils.layer ={};
/**
 * 初始化
 */
Perkinelmer.ESBClient.init = function(){
	
};
/**
 * 绑定事件
 */
Perkinelmer.ESBClient.bind = function(){
	$("#webserviceBtn").click(function(){
		var wsName = $("#webserviceOptions").val(),
			soap = $("input[name='soapType']:checked").val();
		if(!wsName){
			Perkinelmer.Utils.layer.alert("请重新选择服务");
		}else{
			Perkinelmer.ESBClient.webserviceTest(wsName,soap);//调用开始调用
		}
	});
	$("#highlyConcurrentBtn").click(function(){
		var count = $("#highlyConcurrentCount").val();
		if(count<=0){
			Perkinelmer.Utils.layer.alert("请选择大于0的并发次数");
		}else{
			Perkinelmer.ESBClient.highlyConcurrent(count);
			Perkinelmer.Utils.layer.msg("并发命令已下达，请耐心等待！")
		}
	});
};
/**
 * 初始化工具
 */
Perkinelmer.ESBClient.initUtils = function(){
	layui.use(['form','layer'], function(){
		Perkinelmer.Utils.layer = layui.layer;
	});
	//1.暴露出layer工具
};
/**
 * 测试所有的webservice接口调用情况
 */
Perkinelmer.ESBClient.webserviceTest = function(webseriveName,soap){
	$.ajax({
		async : true,
		type:'post',
		url:'../ESBWs.do?weatherWebService',
		data:{webseriveName:webseriveName,soapType:soap},
		success:function(data){
			if(data.code==0){
				Perkinelmer.Utils.layer.msg(data.data);
			}else{
				Perkinelmer.Utils.layer.alert(data.msg);
			}
		},
		error:function(){
			Perkinelmer.Utils.layer.alert("数据加载失败");
		}
	});
};

/**
 * 高并发测试
 * @param highlyConcurrentCount
 */
Perkinelmer.ESBClient.highlyConcurrent = function(highlyConcurrentCount){
	$.ajax({
		async : true,
		type:'post',
		url:'../ESBWs/highlyConcurrentTest',
		data:{count:highlyConcurrentCount},
		success:function(data){
			if(data.code==0){
				var data = data.data
				Perkinelmer.Utils.layer.alert("并发"+highlyConcurrentCount+"线程中失败"+data.failSum+"次，执行时间："+data.interval);
			}else{
				Perkinelmer.Utils.layer.alert(data.msg);
			}
		},
		error:function(){
			Perkinelmer.Utils.layer.alert("并发测试失败");
		}
	});
}

$(function(){
	Perkinelmer.ESBClient.initUtils();
	Perkinelmer.ESBClient.init();
	Perkinelmer.ESBClient.bind();
}());