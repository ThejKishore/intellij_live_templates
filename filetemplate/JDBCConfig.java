package ${PACKAGE_NAME};


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/***
 *
 edw.datasource.url=jdbc:h2:~/test
 edw.datasource.driverClassName=org.h2.Driver
 edw.datasource.username=sa
 edw.datasource.password=

 edw.datasource.maxIdle=3
 edw.datasource.maxActive=5
 edw.datasource.minIdle=2
 edw.datasource.testOnBorrow=false
 edw.datasource.testOnConnect=false
 edw.datasource.testOnReturn=false
 edw.datasource.validationQuery=select 1
 edw.datasource.validationQueryTimeOut=2
 edw.datasource.validationInterval=10000
 edw.datasource.jdbcInterceptor=StatementFinalizer;SlowQueryReport(logFailed=true)
 *
 *
 */


@Configuration
public class ${NAME} {


    @Value("${edw.datasource.driverClassName}")
    public String driverClassName;

    @Value("${edw.datasource.url}")
    public String url;

    @Value("${edw.datasource.username}")
    public String username;

    @Value("${edw.datasource.password}")
    public String password;

    @Value("${edw.datasource.maxActive}")
    public int maxActive;

    @Value("${edw.datasource.maxIdle}")
    public int maxIdle;

    @Value("${edw.datasource.minIdle}")
    public int minIdle;

    @Value("${edw.datasource.testOnBorrow}")
    public boolean testOnBorrow;

    @Value("${edw.datasource.testOnConnect}")
    public boolean testOnConnect;

    @Value("${edw.datasource.testOnReturn}")
    public boolean testOnReturn;

    @Value("${edw.datasource.validationQuery}")
    public String validationQuery;

    @Value("${edw.datasource.validationQueryTimeOut}")
    public int validationQueryTimeOut;

    @Value("${edw.datasource.jdbcInterceptor}")
    public String  jdbcInterceptor;

    @Value("${edw.datasource.validationInterval}")
    public long validationInterval;

    @Bean("beanName")
    @Primary
    public DataSource dataSource(){
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(driverClassName).url(url).type(org.apache.tomcat.jdbc.pool.DataSource.class).username(username).password(password);
        DataSource ds = dataSourceBuilder.build();
        setTomcatDataSource(ds);
        return ds;
    }

    private void setTomcatDataSource(DataSource ds){
        org.apache.tomcat.jdbc.pool.DataSource tomcatDatasource =  (org.apache.tomcat.jdbc.pool.DataSource)ds;
        tomcatDatasource.setMaxActive(maxActive);
        tomcatDatasource.setMaxIdle(maxIdle);
        tomcatDatasource.setMinIdle(minIdle);
        tomcatDatasource.setJdbcInterceptors(jdbcInterceptor);
        tomcatDatasource.setTestOnBorrow(testOnBorrow);
        tomcatDatasource.setTestOnReturn(testOnReturn);
        tomcatDatasource.setValidationQuery(validationQuery);
        tomcatDatasource.setValidationQueryTimeout(validationQueryTimeOut);
        tomcatDatasource.setValidationInterval(validationInterval);
    }

}
