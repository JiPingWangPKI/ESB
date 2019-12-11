package tools.perkinelmer.Mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AccountMapper {
	@Select("select * from ( select *,row_number() over(order by id) n from wsm.dbo.accounts) a where a.n>= ${start} and a.n<= ${end}")
	List<Map<String, Object>> getAccountData(@Param("start")int start, @Param("end")int end);

	@Select("select count(1) as sum from wsm.dbo.accounts")
	Integer getAccountSum();

	/**
	 * 删除账户信息
	 * @param id
	 * @return
	 */
	@Delete("delete from wsm.dbo.accounts where id = ${id}")
	Integer deleteAccountById(@Param("id")String id);

	/**
	 * 更新account数据
	 * @param id
	 * @param systemName
	 * @param systemCode
	 * @param contactName
	 * @param cellphone
	 * @param email
	 * @param remark
	 * @param remark2 
	 * @return
	 */
	@Update("Update wsm.dbo.accounts set systemName=#{systemName},Password=#{Password},systemCode=#{systemCode},contactName=#{contactName},cellPhone=#{cellPhone},email=#{email},remark=#{remark} where id=${id}")
	Integer updataAccount(@Param("id")String id, @Param("systemName")String systemName,@Param("Password") String Password, @Param("systemCode")String systemCode, @Param("contactName")String contactName, @Param("cellPhone")String cellphone,
			@Param("email")String email, @Param("remark")String remark);

	@Insert("insert into wsm.dbo.accounts(systemName,Password,systemCode,contactName,cellphone, email,remark) value(#{systemName},#{Password},#{systemCode},#{contactName},#{cellphone},#{email},#{remark})")
	Integer addAccount(@Param("systemName")String systemName,@Param("Password")String Password, @Param("systemCode")String systemCode, @Param("contactName")String contactName, @Param("cellphone")String cellphone, @Param("email")String email,
			@Param("remark")String remark);

	
}
