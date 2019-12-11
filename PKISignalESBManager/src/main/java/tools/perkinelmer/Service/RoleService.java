package tools.perkinelmer.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import tools.perkinelmer.entity.Result;

public interface RoleService {

	/**
	 * 获得所有角色
	 * @return
	 */
	Result getAllRoles();

	/**
	 * 获得所有角色
	 * @param page
	 * @param limit
	 * @return
	 */
	JSONObject getAllRolesData(int page, int limit);

	/**
	 * 根据角色id获得对应的权限树json集
	 * @param roleId
	 * @return
	 */
	JSONArray getAuthoritysByRoleId(int roleId);

	/**
	 * 添加角色
	 * @param roleName
	 * @param authoritys
	 * @return
	 */
	Result addRole(String roleName, String authoritys);

	/**
	 * 删除指定角色
	 * @param roleId
	 * @return
	 */
	Result deleteRoleById(String roleId);

	/**
	 * 更新角色数据
	 * @param roleId
	 * @param roleName
	 * @param authoritys
	 * @return
	 */
	Result editUserById(String roleId, String roleName, String authoritys);
}
