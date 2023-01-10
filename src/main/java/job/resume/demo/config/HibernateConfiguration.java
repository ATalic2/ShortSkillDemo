package job.resume.demo.config;

import java.util.Properties;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource(value = { "classpath:hibernate.properties" })
public class HibernateConfiguration {
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(HibernateConfiguration.class);

	@Value("${jdbc.driverClassName}")
	private String jdbcDriverClassName;
	@Value("${jdbc.url}")
	private String jdbcUrl;
	@Value("${jdbc.username}")
	private String jdbcUsername;
	@Value("${jdbc.password}")
	private String jdbcPassword;

	@Value("${jdbc.partition.count}")
	private String jdbcPartitionCount;
	@Value("${jdbc.min.connections.per.partition}")
	private String jdbcMinConnectionsPerPartition;
	@Value("${jdbc.max.connections.per.partition}")
	private String jdbcMaxConnectionsPerPartition;
	@Value("${jdbc.maxStatements}")
	private String jdbcMaxStatements;
	@Value("${jdbc.idleConnectionTestPeriod}")
	private String jdbcIdleConnectionTestPeriod;
	@Value("${jdbc.loginTimeout}")
	private String jdbcLoginTimeout;
	@Value("${jdbc.preferredTestQuery}")
	private String jdbcPreferredTestQuery;

	@Value("${hibernate.dialect}")
	private String hibernateDialect;
	@Value("${hibernate.max_fetch_depth}")
	private String hibernateMaxFetchDepth;
	@Value("${hibernate.show_sql}")
	private String hibernateShowSql;
	@Value("${hibernate.format_sql}")
	private String hibernateFormatSql;
	@Value("${hibernate.prepare_sql}")
	private String hibernatePrepareSql;
	@Value("${hibernate.hbm2ddl.auto}")
	private String hibernateHbm2ddlAuto;
	@Value("${hibernate.jdbc.fetch_size}")
	private String hibernateJdbcFetchSize;
	@Value("${hibernate.jdbc.batch_size}")
	private String hibernateJdbcBatchSize;
	@Value("${hibernate.cache.use_second_level_cache}")
	private String hibernateCacheUseSecondLevelCache;
	@Value("${hibernate.enable_lazy_load_no_trans}")
	private String hibernateEnableLazyLoadNoTrans;

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan("job.resume.demo.entity");
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.dialect", hibernateDialect);
		hibernateProperties.setProperty("hibernate.max_fetch_depth", hibernateMaxFetchDepth);
		hibernateProperties.setProperty("hibernate.show_sql", hibernateShowSql);
		hibernateProperties.setProperty("hibernate.format_sql", hibernateFormatSql);
		hibernateProperties.setProperty("hibernate.prepare_sql", hibernatePrepareSql);
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
		hibernateProperties.setProperty("hibernate.jdbc.fetch_size", hibernateJdbcFetchSize);
		hibernateProperties.setProperty("hibernate.jdbc.batch_size", hibernateJdbcBatchSize);
		hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", hibernateCacheUseSecondLevelCache);
		hibernateProperties.setProperty("hibernate.enable_lazy_load_no_trans", hibernateEnableLazyLoadNoTrans);
		sessionFactory.setHibernateProperties(hibernateProperties);
		return sessionFactory;
	}

	@Bean
	public ComboPooledDataSource dataSource() {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass(jdbcDriverClassName);
			dataSource.setJdbcUrl(jdbcUrl);
			dataSource.setUser(jdbcUsername);
			dataSource.setPassword(jdbcPassword);
			dataSource.setMinPoolSize(Integer.valueOf(jdbcMinConnectionsPerPartition));
			dataSource.setMaxPoolSize(Integer.valueOf(jdbcMaxConnectionsPerPartition));
			dataSource.setIdleConnectionTestPeriod(Integer.valueOf(jdbcIdleConnectionTestPeriod));
			dataSource.setLoginTimeout(Integer.valueOf(jdbcLoginTimeout));
			dataSource.setPreferredTestQuery(jdbcPreferredTestQuery);
		} catch (Exception e) {
			LOGGER.info("Couldn't create dataSource");
		}
		return dataSource;
	}

	@Bean
	public HibernateTransactionManager transactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		return transactionManager;
	}
}