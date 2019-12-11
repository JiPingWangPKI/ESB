package tools.perkinelmer.Service;

import tools.perkinelmer.entity.Result;

public interface WorkFlowService {

	/**
	 * 获得SBService信息
	 * @param streambaseServer 
	 */
	Result getWorkFlowData(String streambaseServer);

	/**
	 * 更新SBService信息
	 * @param streambaseServer 
	 * @return
	 */
	Result updateWorkFlow(String workFlowName,String name,String serviceName,String requestIP,String targetIp,String platformId,Integer authority,Integer state, String streambaseServer);

	/**
	 * 删除SBService信息
	 * @param id
	 * @param streambaseServer 
	 * @return
	 */
	Result deleteWorkFlowById(String id, String streambaseServer);

	/**
	 * 添加workflwo配置信息
	 * @param workFlowName
	 * @param name
	 * @param serviceName
	 * @param requestIP
	 * @param targetIp
	 * @param platformId
	 * @param authority
	 * @param state
	 * @param streambaseServer 
	 * @return
	 */
	Result addWorkFlow(String workFlowName, String name, String serviceName, String requestIp, String targetIp,
			String platformIp, Integer authority, Integer state, String streambaseServer);

	/**
	 * 更新状态信息
	 * @param serviceName
	 * @param state
	 * @param streambaseServer 
	 * @return
	 */
	Result updateWorkFlowState(String serviceName, Integer state, String streambaseServer);
	
}
