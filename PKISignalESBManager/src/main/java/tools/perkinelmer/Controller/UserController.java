package tools.perkinelmer.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import tools.perkinelmer.Service.UserService;
import tools.perkinelmer.entity.Result;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userservice;
	/**
	 * 获得所有账户信息
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping(params="getUserData")
	public JSONObject getUserData(int page,int limit){
		JSONObject result = userservice.getUserData(page,limit);
		return result;
	}
	/**
	 * 根据账户id更新账户表信息
	 * @param Id
	 * @param SystemName
	 * @param SystemCode
	 * @param ContactName
	 * @param Cellphone
	 * @param Email
	 * @param Remark
	 * @return
	 */
	@RequestMapping(params="editUserById")
	public Result editUserById(String Id,String UserName,String PassWord,String CellPhone,String Email,String RoleIds,String isModify){
		//加密注册密码
		String password = new BCryptPasswordEncoder().encode(PassWord);
		Result result = userservice.updateUser(Id, UserName,password,CellPhone, Email,RoleIds,isModify);
		return result;
	}
	/**
	 * 删除信息
	 * @param Id
	 * @return
	 */
	@RequestMapping(params="deleteUserById")
	public Result deleteAccountById(String Id){
		Result result = userservice.deleteUserById(Id);
		return result;
	}
	/**
	 * 添加账户
	 * @param Id
	 * @return
	 */
	@RequestMapping(params="addUser")
	public Result addUser(String UserName,String PassWord,String CellPhone,String Email,String RoleIds){
		//加密注册密码
		String password = new BCryptPasswordEncoder().encode(PassWord);
		Result result = userservice.addUser( UserName,PassWord,CellPhone, Email,RoleIds);
		return result;
	}
}
