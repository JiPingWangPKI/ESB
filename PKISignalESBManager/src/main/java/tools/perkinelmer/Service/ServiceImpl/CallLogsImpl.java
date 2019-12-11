package tools.perkinelmer.Service.ServiceImpl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import tools.perkinelmer.Enum.ResultEnum;
import tools.perkinelmer.Mapper.CallLogsMapper;
import tools.perkinelmer.Service.CallLogsService;

@Service
public class CallLogsImpl implements CallLogsService {

	protected final static Logger log = LoggerFactory.getLogger(CallLogsImpl.class);
	@Autowired
	private CallLogsMapper callLogsMapper;
	@Override
	public JSONObject getCallLogData(Integer page,Integer limit) {
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
			sum = callLogsMapper.getCallLogsSum();
			rows = callLogsMapper.getCallLogsData(start,end);
			result.put("count", sum);
			result.put("data", rows);
		} catch (Exception e) {
			result.put("code", ResultEnum.UNKONW_ERROR.getCode());
			result.put("msg", "getdata failed！");
		}
		return result;
	}

}
