package tools.perkinelmer.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tools.perkinelmer.entity.Result;
import tools.perkinelmer.service.ApiService;

/**
 * 提供对外开放的api来操作本机
 * @author wangj01052
 *
 */
@RestController
@RequestMapping("/Api")
public class ApiController {
	@Autowired
	private ApiService apiService;
	/**
	 * 切换StreanBaseService开关
	 * @param count
	 * @return
	 */
	@RequestMapping("switchSBServiceState")
	public void switchService(HttpServletRequest request,HttpServletResponse response,@RequestParam(name="ServiceName")String ServiceName,@RequestParam(name="Method")String Method,@RequestParam(name="callback")String callback){
		Result result = apiService.switchService(ServiceName,Method);
		String jsoncallback = callback + "({'result':"+result.getJsonStr()+"})";
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(jsoncallback);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 改变指定文件参数
	 * @param targetId
	 * @param platformId
	 * @param authority
	 * @param sbappId
	 * @return
	 */
	@RequestMapping("modifyWorkFlowParams")
	public void modifyWorkFlowParams(String serviceName,HttpServletRequest request,HttpServletResponse response,@RequestParam(name="targetIp")String targetIp,@RequestParam(name="platformIp")String platformIp,@RequestParam(name="platformIp_vip")String platformIp_vip,@RequestParam(name="requestIp")String requestIp,@RequestParam(name="authority")String authority,@RequestParam(name="workFlowName")String workFlowName,@RequestParam(name="callback")String callback){
		Result result = apiService.modifyWorkFlowParams(serviceName,targetIp,platformIp,platformIp_vip,requestIp,authority,workFlowName);
		String jsoncallback = callback + "({'result':"+result.getJsonStr()+"})";
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(jsoncallback);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获得所有的服务配置
	 * @param request
	 * @param response
	 * @param callback
	 */
	@RequestMapping("getWorkflowData")
	public void getWorkflowData(HttpServletRequest request,HttpServletResponse response,@RequestParam(name="callback")String callback){
		Result result = apiService.getWorkflowData();
		String jsoncallback = callback + "({'result':"+result.getJsonStr()+"})";
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(jsoncallback);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
