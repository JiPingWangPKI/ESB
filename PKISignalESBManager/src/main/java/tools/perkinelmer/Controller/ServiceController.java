package tools.perkinelmer.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import tools.perkinelmer.Service.ServiceService;
import tools.perkinelmer.entity.Result;

@RestController
@RequestMapping("/service")
public class ServiceController {
	@Autowired
	private ServiceService serviceservice;
	/**
	 * 获得所有服务信息
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping(params="getServiceData")
	public JSONObject getServiceData(int page,int limit){
		JSONObject result = serviceservice.getServiceData(page,limit);
		return result;
	}
	/**
	 * 根据服务id更新服务表信息
	 * @param Id
	 * @param SystemName
	 * @param SystemCode
	 * @param ContactName
	 * @param Cellphone
	 * @param Email
	 * @param Remark
	 * @return
	 */
	@RequestMapping(params="editServiceById")
	public Result editServiceById(String Id,String ServiceName,String ServiceCode,String MessageType,String Remark,String ServiceAddress,String ServiceNamespace){
		Result result = serviceservice.updateService(Id,ServiceName, ServiceCode, MessageType, Remark, ServiceAddress, ServiceNamespace);
		return result;
	}
	/**
	 * 删除信息
	 * @param Id
	 * @return
	 */
	@RequestMapping(params="deleteServiceById")
	public Result deleteServiceById(String Id){
		Result result = serviceservice.deleteServiceById(Id);
		return result;
	}
	/**
	 * 添加服务
	 * @param Id
	 * @return
	 */
	@RequestMapping(params="addService")
	public Result addService(String ServiceName,String ServiceCode,String MessageType,String Remark,String ServiceAddress,String ServiceNamespace){
		Result result = serviceservice.addService(ServiceName, ServiceCode, MessageType, Remark, ServiceAddress, ServiceNamespace);
		return result;
	}
}
