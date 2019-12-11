package tools.perkinelmer.Mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CallLogsMapper {
	@Select("select * from ( select *,row_number() over(order by SerialNo) n from wsm.dbo.CallLogs) a where a.n>= ${start} and a.n<= ${end}")
	List<Map<String, Object>> getCallLogsData(@Param("start")int start, @Param("end")int end);

	@Select("select count(1) as sum from wsm.dbo.calllogs")
	Integer getCallLogsSum();
	
	@Select("select count(1) from wsm.dbo.calllogs where StartDt>=#{start} and WorkFlowName=#{workflowname} and Status='Fail'")
	Integer getFailLogsSum(@Param("workflowname")String workflowName,@Param("start")String start);
	
	@Select("select count(1) from wsm.dbo.calllogs where StartDt>=#{start} and WorkFlowName=#{workflowname} and WSInterval>1000")
	Integer getHighCostTimeSum(@Param("workflowname")String workflowName,@Param("start")String start);
}
