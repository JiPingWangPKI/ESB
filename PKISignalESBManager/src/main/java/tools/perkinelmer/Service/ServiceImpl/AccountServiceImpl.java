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
import tools.perkinelmer.Service.AccountService;
import tools.perkinelmer.Utils.ResultUtil;
import tools.perkinelmer.entity.Result;
@Service
public class AccountServiceImpl implements AccountService{
	@Autowired
	private AccountMapper accountMapper;
	protected final static Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
	@Override
	public JSONObject getAccountData(int page, int limit) {
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
			sum = accountMapper.getAccountSum();
			rows = accountMapper.getAccountData(start,end);
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
	public Result updateAccount(String Id, String SystemName,String Password, String SystemCode, String ContactName, String Cellphone,
			String Email, String Remark) {
		try {
			Integer count = accountMapper.updataAccount(Id, SystemName,Password, SystemCode, ContactName, Cellphone, Email, Remark);
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
	public Result deleteAccountById(String id) {
		try {
			Integer count = accountMapper.deleteAccountById(id);
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
	public Result addAccount(String SystemName, String Password,String SystemCode, String ContactName, String Cellphone,
			String Email, String Remark) {
		try {
			Integer count = accountMapper.addAccount(SystemName,Password, SystemCode, ContactName, Cellphone, Email, Remark);
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
