package tools.perkinelmer.Service.ServiceImpl;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONObject;
import tools.perkinelmer.Enum.ResultEnum;
import tools.perkinelmer.Mapper.UserMapper;
import tools.perkinelmer.Mapper.UserRoleMapper;
import tools.perkinelmer.Service.UserService;
import tools.perkinelmer.Utils.ResultUtil;
import tools.perkinelmer.entity.Result;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserRoleMapper userroleMapper;
	protected final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	@Override
	public JSONObject getUserData(int page, int limit) {
		JSONObject result = new JSONObject();
		List<Map<String,Object>> rows = null;
		Integer sum =0;
		result.put("count", sum);
		result.put("data", rows);
		result.put("code", ResultEnum.SUCCESS.getCode());
		result.put("msg", "getdata success！");
		try {
			int start = (page-1)*limit;
			int end = start+limit-1;
			sum = userMapper.getUserSum();
			rows = userMapper.getUserData(start,end);
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
	@Transactional
	public Result updateUser(String Id, String UserName, String PassWord, String CellPhone, String Email,
			String roleIds,String isModify) {
		try {
			Integer count = userMapper.updataUser(Id, UserName,PassWord,CellPhone, Email,isModify);
			String[] roleids = roleIds.split(",");
			//先删除用户所有角色
			Integer count2 = userroleMapper.deleteRoleUserByUserId(Id);
			if(count2<=0){
				log.info("成功删除用户："+count2+"个");
			}
			for (int i = 0; i < roleids.length; i++) {
				Integer count1 = userroleMapper.insertRoleUser(Id,roleids[i]);
				if(count1<=0){
					return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
				}
			}
			if(count>0){
				return ResultUtil.success();
			}else{
				return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
			}
		} catch (Exception e) {
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}

	@Override
	public Result deleteUserById(String id) {
		try {
			Integer count = userMapper.deleteUserById(id);
			if(count>0){
				return ResultUtil.success();
			}else{
				return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
			}
		} catch (Exception e) {
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}

	@Override
	@Transactional
	public Result addUser(String UserName, String PassWord, String CellPhone, String Email, String roleIds) {
		try {
			//添加用户
			Integer count = userMapper.addUser(UserName,PassWord,CellPhone, Email);
			//添加角色用户对应关系
			String UserId = userMapper.findByUserName(UserName).getId().toString();
			String[] roleids = roleIds.split(",");
			for (int j = 0; j < roleids.length; j++) {
				userroleMapper.insertRoleUser(UserId, roleids[j]);
			}
			if(count>0){
				return ResultUtil.success();
			}else{
				return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
			}
		} catch (Exception e) {
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}

}
