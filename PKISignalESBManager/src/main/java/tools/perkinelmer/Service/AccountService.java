package tools.perkinelmer.Service;

import com.alibaba.fastjson.JSONObject;

import tools.perkinelmer.entity.Result;

public interface AccountService {

	/**
	 * 获得账户信息
	 * @param page
	 * @param limit
	 */
	JSONObject getAccountData(int page, int limit);

	/**
	 * 更新账户信息
	 * @param id
	 * @param systemName
	 * @param systemCode
	 * @param contactName
	 * @param cellphone
	 * @param email
	 * @param remark
	 * @return
	 */
	Result updateAccount(String id, String systemName,String Password, String systemCode, String contactName, String cellphone,
			String email, String remark);

	/**
	 * 删除账户信息
	 * @param id
	 * @return
	 */
	Result deleteAccountById(String id);

	/**
	 * 添加账户信息
	 * @param systemName
	 * @param systemCode
	 * @param contactName
	 * @param cellphone
	 * @param email
	 * @param remark
	 * @return
	 */
	Result addAccount(String systemName,String Password, String systemCode, String contactName, String cellphone, String email,
			String remark);
	
}
