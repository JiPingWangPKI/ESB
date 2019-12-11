package tools.perkinelmer.Service.ServiceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import tools.perkinelmer.Enum.ResultEnum;
import tools.perkinelmer.Mapper.ServiceMapper;
import tools.perkinelmer.Service.ServiceService;
import tools.perkinelmer.Utils.ResultUtil;
import tools.perkinelmer.entity.Result;

@Service
public class ServiceServiceImpl implements ServiceService {

	@Autowired
	private ServiceMapper serviceMapper;
	@Override
	public JSONObject getServiceData(int page, int limit) {
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
			sum = serviceMapper.getServiceSum();
			rows = serviceMapper.getServiceData(start,end);
			result.put("count", sum);
			result.put("data", rows);
		} catch (Exception e) {
			result.put("code", ResultEnum.UNKONW_ERROR.getCode());
			result.put("msg", "getdata failed！");
		}
		return result;
	}

	@Override
	public Result deleteServiceById(String id) {
		try {
			Integer count = serviceMapper.deleteServiceById(id);
			if(count>0){
				return ResultUtil.success();
			}else{
				return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
			}
		} catch (Exception e) {
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}

	@Override
	public Result updateService(String id, String serviceName, String serviceCode, String messageType, String remark,
			String serviceAddress, String serviceNamespace) {
		try {
			Integer count = serviceMapper.updateService(id, serviceName,  serviceCode,  messageType,  remark,
					 serviceAddress,  serviceNamespace);
			if(count>0){
				return ResultUtil.success();
			}else{
				return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
			}
		} catch (Exception e) {
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}

	@Override
	public Result addService(String serviceName, String serviceCode, String messageType, String remark,
			String serviceAddress, String serviceNamespace) {
		try {
			Integer count = serviceMapper.addService(serviceName, serviceCode, messageType, remark, serviceAddress, serviceNamespace);
			if(count>0){
				return ResultUtil.success();
			}else{
				return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
			}
		} catch (Exception e) {
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}

}
