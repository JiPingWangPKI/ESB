package tools.perkinelmer.Service;

import com.alibaba.fastjson.JSONObject;

import tools.perkinelmer.entity.Result;

public interface ServiceService {

	/**
	 * 获得服务信息
	 * @param page
	 * @param limit
	 * @return
	 */
	JSONObject getServiceData(int page, int limit);

	/**
	 * 根据服务Id获得服务信息
	 * @param id
	 * @return
	 */
	Result deleteServiceById(String id);

	/**
	 * 添加服务
	 * @param systemName
	 * @param password
	 * @param systemCode
	 * @param contactName
	 * @param cellphone
	 * @param email
	 * @param remark
	 * @return
	 */
	Result addService(String serviceName, String serviceCode, String messageType, String remark,
			String serviceAddress, String serviceNamespace);

	/**
	 * 更新服务
	 * @param id
	 * @param systemName
	 * @param password
	 * @param systemCode
	 * @param contactName
	 * @param cellphone
	 * @param email
	 * @param remark
	 * @return
	 */
	Result updateService(String id, String serviceName, String serviceCode, String messageType, String remark,
			String serviceAddress, String serviceNamespace);

}
