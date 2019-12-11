package tools.perkinelmer.Service.ServiceImpl;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import tools.perkinelmer.Enum.ResultEnum;
import tools.perkinelmer.Mapper.RoleMapper;
import tools.perkinelmer.Service.RoleService;
import tools.perkinelmer.Utils.ResultUtil;
import tools.perkinelmer.entity.Result;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleMapper roleMapper;
	protected final static Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);
	@Override
	public Result getAllRoles() {
		try {
			List<Map<String,Object>> roles = roleMapper.getAllRoles(0,100000);
			return ResultUtil.success(roles);
		} catch (Exception e) {
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
		
	}
	@Override
	public JSONObject getAllRolesData(int page, int limit) {
		JSONObject result = new JSONObject();
		List<Map<String,Object>> rows = null;
		Integer sum =0;
		result.put("count", sum);
		result.put("data", rows);
		result.put("code", ResultEnum.SUCCESS.getCode());
		result.put("msg", "getdata success！");
		try {
			int start = (page-1)*limit+1;
			int end = start+limit;
			sum = roleMapper.getAllRolesSum();
			rows = roleMapper.getAllRoles(start,end);
			result.put("count", sum);
			result.put("data", rows);
		} catch (Exception e) {
			log.error(e.toString());
			result.put("code", ResultEnum.UNKONW_ERROR.getCode());
			result.put("msg", "getdata failed！");
		}
		return result;
	}
	@Override
	public JSONArray getAuthoritysByRoleId(int roleId) {
		List<Map<String,Object>> authoritys = roleMapper.getAllAuthoritysByRoleId(roleId);
		JSONArray result = getAuthoritys(authoritys,"0");
		return result;
	}
	/**
	 * 获得父级元素为pid的所有子元素树控件JSON数据集
	 * @param authoritys
	 * @param pid_s
	 * @return
	 */
	JSONArray getAuthoritys(List<Map<String,Object>> authoritys,String pid_s){
		JSONArray result = new JSONArray();
		for(Map<String,Object> map:authoritys){
			String pid = map.get("authority_pid").toString();
			String id = map.get("authority_id").toString();
			boolean checked = "0".equals(map.get("checked").toString())?false:true;
			String name = map.get("authority_name").toString();
			if(pid_s.equals(pid)){//父级节点
				//判断id是否末枝节点
				//剩下的节点是末支节点则data=[]
				boolean flag_mozhi = true;
				for(Map<String,Object> map1:authoritys){
					if(id.equals(map1.get("authority_pid").toString())){//不是末枝节点
						flag_mozhi=false;
						break;
					}
				}
				if(flag_mozhi){
					JSONObject jo1 = new JSONObject();
					jo1.put("value", id);
					jo1.put("title", name);
					jo1.put("checked", checked);
					jo1.put("data", new JSONArray());
					result.add(jo1);
				}else{
					JSONArray data = getAuthoritys(authoritys,id);
					JSONObject jo = new JSONObject();
					jo.put("value", id);
					jo.put("title", name);
					jo.put("checked", checked);
					jo.put("data", data);
					result.add(jo);
				}
			}
		}
 		return result;
		
	}
	@Override
	@Transactional	
	public Result addRole(String roleName, String authoritys) {
		try{
			//添加角色
			roleMapper.addRole(roleName);
			int roleId = roleMapper.getRoleIdByRoleName(roleName);
			//添加角色权限对应关系
			String[] authoritys1 = authoritys.split(",");
			int length = authoritys1.length;
			for(int i=0;i<length;i++){
				String authority_id = authoritys1[i];
				roleMapper.addRoleAuthority(roleId,authority_id);
			}
			return ResultUtil.success();
		}catch(Exception e){
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(),ResultEnum.UNKONW_ERROR.getMsg());
		}
	}
	@Override
	@Transactional
	public Result deleteRoleById(String roleId) {
		try{
			//删除角色
			int count = roleMapper.deleteRoleById(roleId);
			if(count>0){
				return ResultUtil.success();
			}else{
				return ResultUtil.error(ResultEnum.FAILDELEDATA.getCode(), ResultEnum.FAILDELEDATA.getMsg());
			}
		}catch(Exception e){
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(),ResultEnum.UNKONW_ERROR.getMsg());
		}
	}
	@Override
	@Transactional
	public Result editUserById(String roleId, String roleName, String authoritys) {
		try{
			//更新角色名称
			int count = roleMapper.updateRoleNameById(roleId,roleName);
			//删除角色权限关系
			roleMapper.deleteRoleAuthority(roleId);
			//添加角色权限对应关系
			String[] authoritys1 = authoritys.split(",");
			int length = authoritys1.length;
			for(int i=0;i<length;i++){
				String authority_id = authoritys1[i];
				roleMapper.addRoleAuthority(Integer.parseInt(roleId),authority_id);
			}
			return ResultUtil.success();
		}catch(Exception e){
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(),ResultEnum.UNKONW_ERROR.getMsg());
		}
	}
}
