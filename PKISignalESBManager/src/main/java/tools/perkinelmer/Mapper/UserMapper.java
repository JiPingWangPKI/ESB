package tools.perkinelmer.Mapper;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import tools.perkinelmer.entity.SysUser;

public interface UserMapper {
	public SysUser findByUserName(String username);

	/**
	 * 获得用户总数
	 * @return
	 */
	@Select("select count(1) from wsm.dbo.sys_user")
	public Integer getUserSum();
	
	@Select("select * from ( select *,row_number() over(order by id) n from wsm.dbo.sys_user where username=#{UserName}) a where a.n>= 0 and a.n<= 1")
	public Map<String,Object> getUserDataByUserName(@Param("UserName")String userName);
	/**
	 * 获得所有用户
	 * @param start
	 * @param end
	 * @return
	 */
	@Select("select * from(select *,(stuff((select ',' ,convert(varchar,b.id) from wsm.dbo.sys_role_user a left join wsm.dbo.sys_role b on b.id=a.sys_role_id where a.sys_user_id=c.id for xml path('')),1,1,'')) roleIds,(stuff((select ',' ,convert(varchar,name) from wsm.dbo.sys_role_user a left join wsm.dbo.sys_role b on b.id=a.sys_role_id where a.sys_user_id=c.id for xml path('')),1,1,'')) roleNames,(stuff((select ',' ,convert(varchar,b.id)+':'+convert(varchar,b.name) from wsm.dbo.sys_role_user a left join wsm.dbo.sys_role b on b.id=a.sys_role_id where a.sys_user_id=c.id for xml path('')),1,1,'')) roleuser,row_number() over(order by c.id) n from wsm.dbo.sys_user c )a where a.n>= ${start} and a.n<= ${end}")
	public List<Map<String, Object>> getUserData(@Param("start")int start, @Param("end")int end);

	/**
	 * 更新用户信息
	 * @param id
	 * @param userName
	 * @param passWord
	 * @param cellPhone
	 * @param email
	 * @param isModify 
	 * @param roleIds
	 * @return
	 */
	@Update("<script>"
			+ "update wsm.dbo.sys_user set username=#{username} ,"
			+ "<if test='isModify==\"true\"'>"
			+ "password=#{password},"
			+ "</if>"
			+ "cellphone=#{cellphone},email=#{email} where id=${id}"
			+ "</script>")
	public Integer updataUser(@Param("id")String id, @Param("username")String userName, @Param("password")String passWord, @Param("cellphone")String cellPhone, @Param("email")String email,@Param("isModify") String isModify);

	/**
	 * 删除用户信息
	 * @param id
	 * @return
	 */
	@Delete("delete from wsm.dbo.sys_user where id=${id}")
	public Integer deleteUserById(@Param("id")String id);

	/**
	 * 添加用户
	 * @param userName
	 * @param passWord
	 * @param cellPhone
	 * @param email
	 * @param roleIds
	 * @return
	 */
	@Insert("insert into wsm.dbo.sys_user(username,password,cellphone,email) values(#{username},#{password},#{cellphone},#{email})")
	public Integer addUser(@Param("username")String userName, @Param("password")String passWord, @Param("cellphone")String cellPhone,@Param("email") String email);
}
