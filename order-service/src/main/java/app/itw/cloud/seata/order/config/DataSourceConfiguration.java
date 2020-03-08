package app.itw.cloud.seata.order.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import io.seata.rm.datasource.DataSourceProxy;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 将DruidDataSource数据源配交给 seata
 */
@Configuration
@EnableConfigurationProperties({MybatisPlusProperties.class})
public class DataSourceConfiguration {


  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource dataSource() {
    return new DruidDataSource();
  }

  @Bean
  public DataSourceProxy dataSourceProxy(DataSource dataSource) {
    return new DataSourceProxy(dataSource);
  }

  @Bean
  public SqlSessionFactory sqlSessionFactoryBean(DataSourceProxy dataSourceProxy,
      MybatisPlusProperties mybatisProperties) {
    MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
    bean.setDataSource(dataSourceProxy);

    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    try {
      Resource[] mapperLocaltions = resolver
          .getResources(mybatisProperties.getMapperLocations()[0]);
      bean.setMapperLocations(mapperLocaltions);

      if (StringUtils.isNotBlank(mybatisProperties.getConfigLocation())) {
        Resource[] resources = resolver.getResources(mybatisProperties.getConfigLocation());
        bean.setConfigLocation(resources[0]);
      }
      return bean.getObject();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
