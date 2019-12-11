package tools.perkinelmer.entity;

public class SysAuthority {
	private Integer authority_id;
	private Integer authority_pid;
	private String authority_name;
	private String authority_describe;
	public Integer getAuthority_id() {
		return authority_id;
	}
	public void setAuthority_id(Integer authority_id) {
		this.authority_id = authority_id;
	}
	public Integer getAuthority_pid() {
		return authority_pid;
	}
	public void setAuthority_pid(Integer authority_pid) {
		this.authority_pid = authority_pid;
	}
	public String getAuthority_name() {
		return authority_name;
	}
	public void setAuthority_name(String authority_name) {
		this.authority_name = authority_name;
	}
	public String getAuthority_describe() {
		return authority_describe;
	}
	public void setAuthority_describe(String authority_describe) {
		this.authority_describe = authority_describe;
	}
}
