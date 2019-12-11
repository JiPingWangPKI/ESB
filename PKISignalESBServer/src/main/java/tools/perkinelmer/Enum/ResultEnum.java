package tools.perkinelmer.Enum;
/**
 * http请求返回的code对应值
 * @author wangjiping
 *
 */
public enum ResultEnum {
	UNKONW_ERROR(-1, "未知错误"),
    SUCCESS(0, "成功"), 
    ErrorMethod(1,"Streambase service服务状态切换没有此操作方法请重试！"), 
    ServiceStartFail(2,"Streambase service started failed"), 
    ServiceStopFail(3,"Streambase service stopped failed!");
    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
