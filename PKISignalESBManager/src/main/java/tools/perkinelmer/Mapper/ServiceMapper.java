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
public interface ServiceMapper {
	@Select("select * from ( select *,row_number() over(order by id) n from wsm.dbo.services) a where a.n>= ${start} and a.n<= ${end}")
	List<Map<String, Object>> getServiceData(@Param("start")int start, @Param("end")int end);

	@Select("select count(1) as sum from wsm.dbo.services")
	Integer getServiceSum();

	/**
	 * 删除服务信息
	 * @param id
	 * @return
	 */
	@Delete("delete from wsm.dbo.services where id = ${id}")
	Integer deleteServiceById(@Param("id")String id);

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
	@Update("Update wsm.dbo.services set ServiceName=#{ServiceName},ServiceCode=#{ServiceCode},MessageType=#{MessageType},Remark=#{Remark},ServiceAddress=#{ServiceAddress},ServiceNamespace=#{ServiceNamespace} where id=${id}")
	Integer updateService(@Param("id")String Id, @Param("ServiceName")String ServiceName,@Param("ServiceCode") String ServiceCode, @Param("MessageType")String MessageType, @Param("Remark")String Remark, @Param("ServiceAddress")String ServiceAddress,
			@Param("ServiceNamespace")String ServiceNamespace);

	@Insert("insert into wsm.dbo.services(ServiceName,ServiceCode,MessageType,Remark,ServiceAddress,ServiceNamespace) value(#{ServiceName},#{ServiceCode},#{MessageType},#{Remark},#{ServiceAddress},#{ServiceNamespace})")
	Integer addService(@Param("ServiceName")String ServiceName,@Param("ServiceCode") String ServiceCode, @Param("MessageType")String MessageType, @Param("Remark")String Remark, @Param("ServiceAddress")String ServiceAddress,
			@Param("ServiceNamespace")String ServiceNamespace);

	
}
