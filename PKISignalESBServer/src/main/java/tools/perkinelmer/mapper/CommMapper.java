package tools.perkinelmer.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommMapper {

	@Insert("${sql}")
	Integer commInsertSql(@Param("sql")String sql);
	@Insert("INSERT INTO wsm.dbo.CallLogs(RequestId,ServiceName,RequestIPAddress,SoapAction,StartDt,EndDt,WSInterval,MessageInfo,MessageResponse,RemoteServer,StreamBaseServer,Status,MessageType,SubScription_Id,WorkFlowName)VALUES('${RequestId}','${ServiceName}','${RequestIPAddress}','${SoapAction}','${StartDt}','${EndDt}',${WSInterval},#{MessageInfo},#{MessageResponse},'${RemoteServer}','${StreamBaseServer}','${Status}','${MessageType}',${SubScription_Id},'${WorkFlowName}')")
	Integer commInsertSqlForCallInfo(@Param("RequestId")String RequestId,@Param("ServiceName")String ServiceName,@Param("RequestIPAddress")String RequestIPAddress,@Param("SoapAction")String SoapAction,@Param("StartDt")String StartDt,@Param("EndDt")String EndDt,@Param("WSInterval")String WSInterval,@Param("MessageInfo")String MessageInfo,@Param("MessageResponse")String MessageResponse,@Param("RemoteServer")String RemoteServer,@Param("StreamBaseServer")String StreamBaseServer,@Param("Status")String Status,@Param("MessageType")String MessageType,@Param("SubScription_Id")String SubScription_Id,@Param("WorkFlowName")String WorkFlowName);
	
}
