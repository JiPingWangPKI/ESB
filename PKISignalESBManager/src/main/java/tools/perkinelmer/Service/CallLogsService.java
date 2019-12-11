package tools.perkinelmer.Service;

import com.alibaba.fastjson.JSONObject;
public interface CallLogsService {

	/**
	 * 获得调用日志信息
	 * @return
	 */
	JSONObject getCallLogData(Integer page,Integer limit);

}
