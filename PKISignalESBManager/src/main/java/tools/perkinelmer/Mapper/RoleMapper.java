package tools.perkinelmer.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RoleMapper {
	
	@Select("select * from ( select id,name,row_number() over(order by id) n from wsm.dbo.sys_role) a where a.n>= ${start} and a.n<= ${end}")
	List<Map<String, Object>> getAllRoles(@Param("start")int start,@Param("end")int end);

	@Select("select count(1) from wsm.dbo.sys_role")
	Integer getAllRolesSum();
	
	@Select("select sa.authority_id,sa.authority_pid,sa.authority_name,ISNULL(role_id,0) as checked from wsm.dbo.sys_authority sa left join (select * from wsm.dbo.sys_role_authority where role_id=${roleId}) sra on sa.authority_id=sra.authority_id")
	List<Map<String,Object>> getAllAuthoritysByRoleId(@Param("roleId") int roleId);
	
	@Insert("insert into wsm.dbo.sys_role values(#{roleName})")
	Integer addRole(@Param("roleName")String roleName);
	
	@Insert("insert into wsm.dbo.sys_role_authority values(${roleId},${authority_id})")
	Integer addRoleAuthority(@Param("roleId")int roleId, @Param("authority_id")String authority_id);

	@Delete("delete from wsm.dbo.sys_role where id = ${roleId}")
	int deleteRoleById(@Param("roleId")String roleId);

	@Update("update wsm.dbo.sys_role set name=#{roleName} where id = ${roleId}")
	int updateRoleNameById(@Param("roleId")String roleId,@Param("roleName")String roleName);

	@Delete("delete from wsm.dbo.sys_role_authority where role_id=${roleId}")
	void deleteRoleAuthority(@Param("roleId")String roleId);
	
	@Select("Select top(1)id from wsm.dbo.sys_role where name=#{roleName}")
	int getRoleIdByRoleName(@Param("roleName")String roleName);
}
