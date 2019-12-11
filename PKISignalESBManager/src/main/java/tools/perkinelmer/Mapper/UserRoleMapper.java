package tools.perkinelmer.Mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRoleMapper {

	/**
	 * 插入用户角色对应表
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@Insert("insert into wsm.dbo.sys_role_user(Sys_Role_id,Sys_User_id) values(${roleId},${userId}) ")
	Integer insertRoleUser(@Param("userId")String userId, @Param("roleId")String roleId);

	/**
	 * 根据用户id删除角色用户对应关系
	 * @param id
	 * @return
	 */
	@Delete("delete from wsm.dbo.sys_role_user where Sys_User_id=${id}")
	Integer deleteRoleUserByUserId(@Param("id")String id);
}
