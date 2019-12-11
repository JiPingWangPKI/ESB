package tools.perkinelmer.Mapper;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface WorkFlowMapper {
	@Select("select * from wsm.dbo.WorkFlows where streambaseServer=#{streambaseServer}")
	List<Map<String, Object>> getWorkFlowData(@Param("streambaseServer")String streambaseServer);
	
	@Update("update wsm.dbo.WorkFlows set name=#{name},serviceName=#{serviceName},requestIp=#{requestIp},targetIp=#{targetIp},platformIp=#{platformIp},authority=${authority},state=${state} where workFlowName=#{workFlowName} and streambaseServer=#{streambaseServer}")
	Integer updateWorkFlow(@Param("workFlowName")String workFlowName, @Param("name")String name, @Param("serviceName")String serviceName, @Param("requestIp")String requestIp, @Param("targetIp")String targetIp,
			@Param("platformIp")String platformIp, @Param("authority")Integer authority,@Param("state")Integer state, @Param("streambaseServer")String streambaseServer);
	
	@Delete("delete from wsm.dbo.WorkFlows where workFlowName=#{workFlowName} and streambaseServer=#{streambaseServer}")
	Integer deleWorkFlow(@Param("workFlowName")String workflowname, @Param("streambaseServer")String streambaseServer);
	
	@Insert("insert into wsm.dbo.WorkFlows(name,serviceName,requestIp,targetIp,platformIp,authority,state,workFlowName,streambaseServer) values(#{name},#{serviceName},#{requestIp},#{targetIp},#{platformIp},${authority},${state},#{workFlowName},#{streambaseServer})")
	Integer addWorkFlow(@Param("workFlowName")String workFlowName, @Param("name")String name, @Param("serviceName")String serviceName, @Param("requestIp")String requestIp, @Param("targetIp")String targetIp,
			@Param("platformIp")String platformIp, @Param("authority")Integer authority, @Param("state")Integer state, @Param("streambaseServer")String streambaseServer);

	@Update("update wsm.dbo.WorkFlows set state = ${state} where serviceName=#{serviceName} and streambaseServer=#{streambaseServer}")
	Integer updateWorkFlowState(@Param("serviceName")String serviceName, @Param("state")Integer state, @Param("streambaseServer")String streambaseServer);
}
