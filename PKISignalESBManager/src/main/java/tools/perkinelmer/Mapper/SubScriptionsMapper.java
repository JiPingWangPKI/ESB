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
public interface SubScriptionsMapper {
	@Select("select ss.Id as SubScriptionId,Account_Id as AccountId,SystemName,SystemCode,Service_Id as ServiceId,ServiceName,ServiceCode  from wsm.dbo.Accounts  a ,wsm.dbo.services s,(select *,row_number() over(order by Id) n from wsm.dbo.SubScriptions) ss where a.Id=ss.Account_Id and s.Id=ss.Service_Id and ss.n>=${start} and ss.n<=${end}")
	//@Select("select ss.Id as SubScriptionId,Account_Id as AccountId,SystemName,SystemCode,Service_Id as ServiceId,ServiceName,ServiceCode  from accounts as a,services as s,subscriptions as ss where a.Id=ss.Account_Id and s.Id=ss.Service_Id limit ${start} , ${end}")
	List<Map<String, Object>> getSubScriptionsData(@Param("start")int start, @Param("end")int end);

	@Select("select count(1) as sum from wsm.dbo.subscriptions")
	Integer getSubScriptionsSum();

	/**
	 * 删除服务信息
	 * @param id
	 * @return
	 */
	@Delete("delete from wsm.dbo.subscriptions where id = ${id}")
	Integer deleteSubScriptionsById(@Param("id")String id);

	/**
	 * 更新services数据
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
	@Update("Update wsm.dbo.subscriptions set account_id=${Account_Id} ,service_id=${Service_Id}  where id=${id}")
	Integer updataSubScriptions(@Param("id")String Id, @Param("Account_Id")String Account_Id,@Param("Service_Id") String Service_Id);

	@Insert("insert into wsm.dbo.subscriptions(Account_Id,Service_Id) values(${Account_Id},${Service_Id})")
	Integer addSubScriptions(@Param("Account_Id")String Account_Id,@Param("Service_Id") String Service_Id);

	
}
