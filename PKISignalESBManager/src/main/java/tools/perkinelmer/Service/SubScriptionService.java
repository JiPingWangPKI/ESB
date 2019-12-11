package tools.perkinelmer.Service;

import com.alibaba.fastjson.JSONObject;

import tools.perkinelmer.entity.Result;

public interface SubScriptionService {

	/**
	 * 获得订阅信息
	 * @param page
	 * @param limit
	 * @return
	 */
	JSONObject getSubScriptionData(int page, int limit);

	/**
	 * 根据订阅Id获得订阅信息
	 * @param id
	 * @return
	 */
	Result deleteSubScriptionById(String id);

	/**
	 * 添加订阅
	 * @param systemName
	 * @param password
	 * @param systemCode
	 * @param contactName
	 * @param cellphone
	 * @param email
	 * @param remark
	 * @return
	 */
	Result addSubScription(String AccountId,String ServiceId);

	/**
	 * 更新订阅
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
	Result updateSubScription(String id,String AccountId,String ServiceId);

	/**
	 * 获得订阅详细信息配置select控件
	 * @return
	 */
	Result getSubscriptionInfoDetail();

}
