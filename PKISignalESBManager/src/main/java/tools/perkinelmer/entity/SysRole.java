package tools.perkinelmer.entity;

import java.util.List;

public class SysRole {
	private Integer id;
	private String name;
	private List<SysAuthority> authoritys;
	public List<SysAuthority> getAuthoritys() {
		return authoritys;
	}
	public void setAuthoritys(List<SysAuthority> authoritys) {
		this.authoritys = authoritys;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
