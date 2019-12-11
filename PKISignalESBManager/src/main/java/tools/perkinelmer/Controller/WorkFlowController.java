package tools.perkinelmer.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.perkinelmer.Service.WorkFlowService;
import tools.perkinelmer.entity.Result;

@RestController
@RequestMapping("/workflow")
public class WorkFlowController {
	@Autowired
	private WorkFlowService workflowservice;
	/**
	 * 获得所有账户信息
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping(params="getWorkflowData")
	public Result getWorkflowData(String streambaseServer){
		Result result = workflowservice.getWorkFlowData(streambaseServer);
		return result;
	}
	/**
	 * 根据账户id更新账户表信息
	 * @param Id
	 * @param SystemName
	 * @param SystemCode
	 * @param ContactName
	 * @param Cellphone
	 * @param Email
	 * @param Remark
	 * @return
	 */
	@RequestMapping(params="editWorkflowByWorkFlowName")
	public Result editWorkflowByWorkFlowName(String workFlowName,String name,String serviceName,String requestIp,String targetIp,String platformIp,Integer authority,Integer state,String streambaseServer){
		Result result = workflowservice.updateWorkFlow(workFlowName,name,serviceName,requestIp,targetIp,platformIp,authority,state,streambaseServer);
		return result;
	}
	/**
	 * 删除workflow信息
	 * @param Id
	 * @return
	 */
	@RequestMapping(params="deleteWorkFlowByWorkFlowName")
	public Result deleteAccountById(String WorkFlowName,String streambaseServer){
		Result result = workflowservice.deleteWorkFlowById(WorkFlowName,streambaseServer);
		return result;
	}
	/**
	 * 添加workflow配置信息
	 * @param Id
	 * @return
	 */
	@RequestMapping(params="addWorkFlow")
	public Result addWorkFlow(String name,String serviceName,String workFlowName,String requestIp,String targetIp,String streambaseIp,Integer authority,Integer state,String streambaseServer){
		Result result = workflowservice.addWorkFlow(workFlowName,name,serviceName,requestIp,targetIp,streambaseIp,authority,state,streambaseServer);
		return result;
	}
	/**
	 * 本地更新workflow状态信息
	 * @param ServiceName
	 * @param State
	 * @return
	 */
	@RequestMapping(params="updateWorkFlowState")
	public Result updateWorkFlowState(String ServiceName,Integer State,String streambaseServer){
		Result result = workflowservice.updateWorkFlowState(ServiceName,State,streambaseServer);
		return result;
	}
}
