package tools.perkinelmer.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 添加一个新的数据源（项目主数据库wsm）
 * @author wangjiping
 *
 */
@Configuration
@MapperScan(basePackages="tools.perkinelmer.Mapper",sqlSessionTemplateRef="SqlSessionTemplate")
public class DataSourceConfig {
	@Bean(name="DataSource")
	@ConfigurationProperties(prefix="spring.datasource.wsm")
	@Primary
	public DataSource tyzxDataSource(){
		return DataSourceBuilder.create().build();
	}
	@Bean(name="SqlSessionFactory")
	@Primary
	public SqlSessionFactory tyzxSqlSessionFactory(@Qualifier("DataSource") DataSource dataSource) throws Exception{
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver()
		        .getResources("classpath:mappers/*.xml"));
		return bean.getObject();
	}
	@Bean(name = "TransactionManager")
	@Primary
	public DataSourceTransactionManager tyzxTransactionManager(@Qualifier("DataSource") DataSource dataSource) {
	    return new DataSourceTransactionManager(dataSource);
	}
	@Bean(name="SqlSessionTemplate")
	public SqlSessionTemplate tyzxSqlSessionTemplate(@Qualifier("SqlSessionFactory") SqlSessionFactory sqlSessionFactory)throws Exception{
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
