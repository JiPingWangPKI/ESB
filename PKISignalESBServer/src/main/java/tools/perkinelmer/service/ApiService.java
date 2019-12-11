package tools.perkinelmer.service;

import tools.perkinelmer.entity.Result;

public interface ApiService {
	/**
	 * 切换SB服务开关
	 * @param serviceName
	 * @param method
	 * @return
	 */
	Result switchService(String serviceName, String method);

	/**
	 * 修改工作workflow指定sbapp参数
	 * @param targetId
	 * @param platformId
	 * @param authority
	 * @param liveViewPassWord 
	 * @param liveViewUserName 
	 * @param liveViewPort 
	 * @param sbappId
	 * @return
	 */
	Result modifyWorkFlowParams(String serviceName,String targetIp, String platformIp,String platformIp_vip,String requestIp ,String authority, String workFlowName, String liveViewPort, String liveViewUserName, String liveViewPassWord);

	/**
	 * 获得所有的workflowp配置信息
	 * @return
	 */
	Result getWorkflowData();
}
