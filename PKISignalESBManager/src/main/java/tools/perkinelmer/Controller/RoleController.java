package tools.perkinelmer.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import tools.perkinelmer.Service.RoleService;
import tools.perkinelmer.entity.Result;

@RestController
@RequestMapping("role")
public class RoleController {
	@Autowired
	private  RoleService roleService;
	/**
	 * 获得所有角色
	 * @return
	 */
	@RequestMapping(params="getAllRoles")
	public Result getAllRoles(){
		Result roles = roleService.getAllRoles();
		return roles;
	}
	@RequestMapping(params="getAllRolesData")
	public JSONObject getAllRolesData(int page,int limit){
		JSONObject result = roleService.getAllRolesData(page,limit);
		return result;
	}
	@RequestMapping(params="getAuthoritysByRoleId")
	public JSONArray getAuthoritysByRoleId(int roleId){
		JSONArray result = roleService.getAuthoritysByRoleId(roleId);
		return result;
	}
	@RequestMapping(params="addRole")
	public Result addRole(String roleName,String authoritys){
		Result result = roleService.addRole(roleName,authoritys);
		return result;
	}
	@RequestMapping(params="deleteRoleById")
	public Result deleteRoleById(String roleId){
		Result result = roleService.deleteRoleById(roleId);
		return result;
	}
	@RequestMapping(params="editUserById")
	public Result editUserById(String roleId,String roleName,String authoritys){
		Result result = roleService.editUserById(roleId,roleName,authoritys);
		return result;
	}
}
