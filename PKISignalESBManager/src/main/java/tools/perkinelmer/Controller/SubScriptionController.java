package tools.perkinelmer.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import tools.perkinelmer.Service.SubScriptionService;
import tools.perkinelmer.entity.Result;

@RestController
@RequestMapping("/subscription")
public class SubScriptionController {
	@Autowired
	private SubScriptionService subScriptionservice;
	/**
	 * 获得所有账户信息
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping(params="getSubScriptionData")
	public JSONObject getSubScriptionData(int page,int limit){
		JSONObject result = subScriptionservice.getSubScriptionData(page,limit);
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
	@RequestMapping(params="editSubScriptionById")
	public Result editSubScriptionById(String Id,String AccountId,String ServiceId){
		Result result = subScriptionservice.updateSubScription(Id, AccountId,AccountId);
		return result;
	}
	/**
	 * 删除信息
	 * @param Id
	 * @return
	 */
	@RequestMapping(params="deleteSubScriptionById")
	public Result deleteSubScriptionById(String Id){
		Result result = subScriptionservice.deleteSubScriptionById(Id);
		return result;
	}
	/**
	 * 添加账户
	 * @param Id
	 * @return
	 */
	@RequestMapping(params="addSubScription")
	public Result addSubScription(String AccountId,String ServiceId){
		Result result = subScriptionservice.addSubScription(AccountId,ServiceId);
		return result;
	}
	/**
	 * 获得订阅的详细信息配置操作框select控件
	 * @return
	 */
	@RequestMapping(params="getSubscriptionInfoDetail")
	public Result getSubscriptionInfoDetail(){
		Result result = subScriptionservice.getSubscriptionInfoDetail();
		return result;
	}
}
