package tools.perkinelmer.Service.ServiceImpl;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import tools.perkinelmer.Enum.ResultEnum;
import tools.perkinelmer.Mapper.AccountMapper;
import tools.perkinelmer.Mapper.ServiceMapper;
import tools.perkinelmer.Mapper.SubScriptionsMapper;
import tools.perkinelmer.Service.SubScriptionService;
import tools.perkinelmer.Utils.ResultUtil;
import tools.perkinelmer.entity.Result;

@Service
public class SubScriptionServiceImpl implements SubScriptionService{

	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	private ServiceMapper serviceMapper;
	@Autowired
	private SubScriptionsMapper subscriptionsMapper;
	protected final static Logger log = LoggerFactory.getLogger(SubScriptionServiceImpl.class);
	@Override
	public JSONObject getSubScriptionData(int page, int limit) {
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
			sum = subscriptionsMapper.getSubScriptionsSum();
			rows = subscriptionsMapper.getSubScriptionsData(start,end);
			result.put("count", sum);
			result.put("data", rows);
		} catch (Exception e) {
			result.put("code", ResultEnum.UNKONW_ERROR.getCode());
			result.put("msg", "getdata failed！");
		}
		return result;
	}

	@Override
	public Result deleteSubScriptionById(String id) {
		try {
			Integer count = subscriptionsMapper.deleteSubScriptionsById(id);
			if(count>0){
				return ResultUtil.success();
			}else{
				return ResultUtil.error(ResultEnum.FAILDELEDATA.getCode(), ResultEnum.FAILDELEDATA.getMsg());
			}
		} catch (Exception e) {
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}

	@Override
	public Result addSubScription(String AccountId,String ServiceId) {
		try {
			Integer count = subscriptionsMapper.addSubScriptions(AccountId, ServiceId);
			if(count>0){
				return ResultUtil.success();
			}else{
				return ResultUtil.error(ResultEnum.FAILADDATA.getCode(), ResultEnum.FAILADDATA.getMsg());
			}
		} catch (Exception e) {
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}

	@Override
	public Result updateSubScription(String id,String AccountId,String ServiceId) {
		try {
			Integer count = subscriptionsMapper.updataSubScriptions(id, AccountId, ServiceId);
			if(count>0){
				return ResultUtil.success();
			}else{
				return ResultUtil.error(ResultEnum.FAILUPDATEDATA.getCode(), ResultEnum.FAILUPDATEDATA.getMsg());
			}
		} catch (Exception e) {
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}

	@Override
	public Result getSubscriptionInfoDetail() {
		try {
			JSONObject result = new JSONObject();
			//AccountId
			List<Map<String,Object>> accounts = accountMapper.getAccountData(0, 100000);
			//ServiceId
			List<Map<String,Object>> services = serviceMapper.getServiceData(0, 100000);
			result.put("accounts", accounts);
			result.put("services", services);
			return ResultUtil.success(result);
		} catch (Exception e) {
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}

}
