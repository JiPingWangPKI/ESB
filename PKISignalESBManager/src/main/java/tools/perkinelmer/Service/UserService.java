package tools.perkinelmer.Service;

import com.alibaba.fastjson.JSONObject;

import tools.perkinelmer.entity.Result;

public interface UserService {

	/**
	 * 获得用户信息
	 * @param page
	 * @param limit
	 */
	JSONObject getUserData(int page, int limit);

	/**
	 * 更新用户
	 * @param Id
	 * @param UserName
	 * @param PassWord
	 * @param CellPhone
	 * @param Email
	 * @param roleIds
	 * @param isModify 
	 * @return
	 */
	Result updateUser(String Id,String UserName,String PassWord,String CellPhone,String Email,String roleIds, String isModify);

	/**
	 * 删除用户信息
	 * @param id
	 * @return
	 */
	Result deleteUserById(String id);

	/**
	 * 添加用户信息
	 * @param systemName
	 * @param systemCode
	 * @param contactName
	 * @param cellphone
	 * @param email
	 * @param remark
	 * @return
	 */
	Result addUser(String UserName,String PassWord,String CellPhone,String Email,String roleIds);
	
}
