package tools.perkinelmer.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import tools.perkinelmer.Service.CallLogsService;

@RestController
@RequestMapping("/calllog")
public class CallLogsController {
	@Autowired
	private CallLogsService callLogsService;
	@RequestMapping(params="getCallLogData")
	public JSONObject getCallLogData(int page,int limit){
		//之后可以添加查看指定的订阅信息
		JSONObject result = callLogsService.getCallLogData(page,limit);//只获得订阅的日志信息
		return result;
	}
}
