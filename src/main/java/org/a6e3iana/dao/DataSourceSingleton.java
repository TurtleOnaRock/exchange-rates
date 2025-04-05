package org.a6e3iana.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceSingleton {

    private final static String DRIVER_CLASS_NAME = "org.sqlite.JDBC";
    private final static String DATA_BASE_URL ="jdbc:sqlite:/opt/apache-tomcat-11.0.5/webapps/exchange-rate-1.0.0/WEB-INF/classes/ExchangeDataBase.db";

    private static DataSourceSingleton instance;
    private static HikariDataSource dataSource;

    private DataSourceSingleton(){
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(DRIVER_CLASS_NAME);
        config.setJdbcUrl(DATA_BASE_URL);
        dataSource = new HikariDataSource(config);
    }

    public static synchronized DataSourceSingleton getInstance(){
        if(instance == null){
            instance = new DataSourceSingleton();
        }
        return instance;
    }

    public DataSource getDataSource(){
        return dataSource;
    }
}
