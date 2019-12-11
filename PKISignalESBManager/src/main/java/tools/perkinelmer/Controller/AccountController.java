package tools.perkinelmer.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import tools.perkinelmer.Service.AccountService;
import tools.perkinelmer.entity.Result;

@RestController
@RequestMapping("/account")
public class AccountController {
	@Autowired
	private AccountService accountservice;
	/**
	 * 获得所有账户信息
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping(params="getAccountData")
	public JSONObject getAccountData(int page,int limit){
		JSONObject result = accountservice.getAccountData(page,limit);
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
	@RequestMapping(params="editAccountById")
	public Result editAccountById(String Id,String SystemName,String Password,String SystemCode,String ContactName,String Cellphone,String Email,String Remark){
		Result result = accountservice.updateAccount(Id, SystemName,Password, SystemCode, ContactName, Cellphone, Email, Remark);
		return result;
	}
	/**
	 * 删除信息
	 * @param Id
	 * @return
	 */
	@RequestMapping(params="deleteAccountById")
	public Result deleteAccountById(String Id){
		Result result = accountservice.deleteAccountById(Id);
		return result;
	}
	/**
	 * 添加账户
	 * @param Id
	 * @return
	 */
	@RequestMapping(params="addAccount")
	public Result addAccount(String SystemName,String Password,String SystemCode,String ContactName,String Cellphone,String Email,String Remark){
		Result result = accountservice.addAccount(SystemName,Password, SystemCode, ContactName, Cellphone, Email, Remark);
		return result;
	}
}
