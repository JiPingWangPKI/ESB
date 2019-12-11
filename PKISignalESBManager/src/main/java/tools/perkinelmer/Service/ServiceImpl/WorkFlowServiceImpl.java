package tools.perkinelmer.Service.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.perkinelmer.Enum.ResultEnum;
import tools.perkinelmer.Mapper.CallLogsMapper;
import tools.perkinelmer.Mapper.WorkFlowMapper;
import tools.perkinelmer.Service.WorkFlowService;
import tools.perkinelmer.Utils.ResultUtil;
import tools.perkinelmer.entity.Result;

@Service
public class WorkFlowServiceImpl implements WorkFlowService{
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	private WorkFlowMapper workflowMapper;
	@Autowired
	private CallLogsMapper callLogsMapper;
	protected final static Logger log = LoggerFactory.getLogger(WorkFlowServiceImpl.class);
	@Override
	public Result getWorkFlowData(String streambaseServer) {
		try {
			String start = sdf.format(new Date());
			List<Map<String,Object>> data = workflowMapper.getWorkFlowData(streambaseServer);
			List<Map<String,Object>> result = new ArrayList<>();
			for (Map<String, Object> map : data) {
				String WorkFlowName = map.get("workFlowName").toString();
				Integer failSum = callLogsMapper.getFailLogsSum(WorkFlowName, start);
				Integer highCostTimeSum = callLogsMapper.getHighCostTimeSum(WorkFlowName, start);
				map.put("failSum", failSum.toString());
				map.put("highCostTimeSum", highCostTimeSum.toString());
				result.add(map);
			}
			return ResultUtil.success(result);
		} catch (Exception e) {
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}

	@Override
	public Result updateWorkFlow(String workFlowName,String name,String serviceName,String requestIp,String targetIp,String platformIp,Integer authority,Integer state,String streambaseServer) {
		try{
			int count = workflowMapper.updateWorkFlow(workFlowName,name,serviceName,requestIp,targetIp,platformIp,authority,state,streambaseServer);
			if(count<=0){
				log.error("数据更新失败");
				return ResultUtil.error(ResultEnum.UpDateFail.getCode(), ResultEnum.UpDateFail.getMsg());
			}else{
				return ResultUtil.success();
			}
		}catch(Exception e){
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}

	@Override
	public Result deleteWorkFlowById(String workflowname,String streambaseServer) {
		try{
			int count = workflowMapper.deleWorkFlow(workflowname,streambaseServer);
			if(count<=0){
				log.error("数据更新失败");
				return ResultUtil.error(ResultEnum.DeleDateFail.getCode(), ResultEnum.DeleDateFail.getMsg());
			}else{
				return ResultUtil.success();
			}
		}catch(Exception e){
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}

	@Override
	public Result addWorkFlow(String workFlowName, String name, String serviceName, String requestIp, String targetIp,
			String platformIp, Integer authority, Integer state,String streambaseServer) {
		Integer count = workflowMapper.addWorkFlow(workFlowName,name,serviceName,requestIp,targetIp,platformIp,authority,state,streambaseServer);
		if(count<=0){
			log.error("数据删除失败");
			return ResultUtil.error(ResultEnum.DeleDateFail.getCode(),ResultEnum.DeleDateFail.getMsg());
		}else{
			return ResultUtil.success();
		}
	}

	@Override
	public Result updateWorkFlowState(String serviceName, Integer state,String streambaseServer) {
		Integer count = workflowMapper.updateWorkFlowState(serviceName,state,streambaseServer);
		if(count<=0){
			log.error("数据更新失败");
			return ResultUtil.error(ResultEnum.UpDateFail.getCode(), ResultEnum.UpDateFail.getMsg());
		}else{
			return ResultUtil.success();
		}
	}
}
