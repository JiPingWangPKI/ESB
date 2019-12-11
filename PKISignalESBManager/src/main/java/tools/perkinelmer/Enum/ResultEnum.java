package tools.perkinelmer.Enum;
/**
 * http请求返回的code对应值
 * @author wangjiping
 *
 */
public enum ResultEnum {
	UNKONW_ERROR(-1, "未知错误"),
    SUCCESS(0, "成功"), 
    FAILDELEDATA(1,"数据删除失败！"), 
    FAILUPDATEDATA(2,"数据更新失败"), FAILADDATA(3,"数据添加失败!"), 
    UpDateFail(4,"数据更新失败"), DeleDateFail(5,"数据删除失败！");
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
