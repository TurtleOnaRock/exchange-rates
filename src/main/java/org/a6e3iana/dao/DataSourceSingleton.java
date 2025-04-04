package org.a6e3iana.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceSingleton {

    private final static String DRIVER_CLASS_NAME = "org.sqlite.JDBC";
    private final static String DB_FILE_NAME = "ExchangeDataBase.db";
    private final static String URL="jdbc:sqlite:";
    private final static String USER="";
    private final static String PASSWORD="";

    private static DataSourceSingleton instance;
    private static HikariDataSource dataSource;

    private DataSourceSingleton(){
        try{
            Class.forName(DRIVER_CLASS_NAME);
        }catch (Exception e){
            e.printStackTrace();
        }
        HikariConfig config = new HikariConfig();
        String dbPath = getClass().getClassLoader().getResource(DB_FILE_NAME).getPath();
        config.setJdbcUrl(URL+dbPath);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
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
