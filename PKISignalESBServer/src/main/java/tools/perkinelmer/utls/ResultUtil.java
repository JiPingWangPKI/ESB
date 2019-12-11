package tools.perkinelmer.utls;

import tools.perkinelmer.entity.Result;

/**
 * 统一处理返回结果
 * @author wangjiping
 *
 */
public class ResultUtil {
	public static Result success(Object object){
		Result result = new Result();
		result.setCode(0);
		result.setMsg("成功");
		result.setData(object);
		return result;
	}
	public static Result success(){
		return success(null);
	}
	public static Result error(Integer code,String msg){
		Result result = new Result();
		result.setCode(code);
		result.setMsg(msg);
		return result;
	}
}
