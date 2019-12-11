var WSM = parent.window.WSM;
WSM.calllog = {};

WSM.calllog.init = function(table){
	//初始化表格
	WSM.calllog.initTable("#calllogtb",table);
};
/**
 * 
 * @param $tableId 表格的id
 * @param layuitable layui必须的table模块
 */
WSM.calllog.initTable = function($tableId,table){
	table.render({
		elem:$tableId,
		height:'full-110',
		url:'../calllog.do?getCallLogData',
		page:true,
		cols:[[
		   {field:'SerialNo',title:'ID',width:50,sort:true},
		   {field:'RequestId',title:'RequestId',sort:true},
		   {field:'ServiceName',title:'ServiceName',sort:true},
		   {field:'RequestIPAddress',title:'RequestIPAddress',sort:true},
		   {field:'SoapAction',title:'SoapAction',sort:true},
		   {field:'StartDt',title:'StartDt',sort:true,
			   templet:function(d){
				   return WSM.Utils.Date.toDateString(d.StartDt,'yyyy-MM-dd HH:mm:ss.ms')
			   }},
		   {field:'MessageInfo',title:'MessageInfo',sort:true},
		   {field:'MessageResponse',title:'MessageResponse',sort:true},
		   {field:'Status',title:'Status',sort:true},
		   {field:'WSInterval',title:'WSInterval',sort:true},
		   {field:'RemoteServer',title:'RemoteServer',sort:true},
		   {field:'StreamBaseServer',title:'StreamBaseServer',sort:true},
		   {field:'SubScription_Id',title:'SubScription_Id',sort:true},
		   {field:'MessageType',title:'MessageType',sort:true},
		]]
	});
};
WSM.calllog.bind = function(table,layer){
};

(function(){
	layui.use(['element','layer','form','table'], function(){
		var layer = layui.layer;
		var table = layui.table;
		WSM.calllog.init(table);
		WSM.calllog.bind(table,layer);
	})
}());
