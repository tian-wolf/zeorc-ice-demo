package com.tlwl.demo.base;

import java.io.InputStream;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import org.apache.commons.lang3.StringUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Properties;

import java.util.Map;
import java.util.HashMap;

public class BaseDBManager {

    private static BaseDBManager instance;

    public DruidPooledConnection dpc = null;

    public DruidDataSource dbs = null;

    public BaseDBManager() {

        InputStream inputStream = null;
		Map<String, String> mapProperties = null;
		String[] strProperties = null;
        Properties property = null;
        
        try {
            // inputStream = new FileInputStream("com.tlwl.demo/config/db.properties");
            inputStream = BaseDBManager.class.getClassLoader().getResourceAsStream("db.properties");
            property = new Properties();
            property.load(inputStream);
            
            this.dbs = new DruidDataSource();

            strProperties = property.toString().replace("{", "").replace("}", "").split(",");
            
            mapProperties = new HashMap<String, String>();

            for (int i = 0; i < strProperties.length; i++) {
                mapProperties.put(StringUtils.substringBefore(strProperties[i].trim(),"="),StringUtils.substringAfter(strProperties[i].trim(), "="));
            }

            //配置详细信息
            this.dbs.setUrl(mapProperties.get("jdbc.url"));
            this.dbs.setUsername(mapProperties.get("jdbc.username"));
            this.dbs.setPassword(mapProperties.get("jdbc.password"));
            this.dbs.setDriverClassName(mapProperties.get("jdbc.driverClassName"));

            //配置初始化大小、最小、最大
            this.dbs.setMaxActive(Integer.parseInt(mapProperties.get("jdbc.maxActive")));
            this.dbs.setMinIdle(Integer.parseInt(mapProperties.get("jdbc.minIdle")));
            this.dbs.setInitialSize(Integer.parseInt(mapProperties.get("jdbc.initialSize")));

            //超时等待时间以毫秒为单位
            this.dbs.setMaxWait(Integer.parseInt(mapProperties.get("jdbc.maxWait")));
            //SQL查询,用来验证从连接池取出的连接,在将连接返回给调用者之前
            this.dbs.setValidationQuery(mapProperties.get("jdbc.validationQuery"));

            this.dbs.setTestWhileIdle(Boolean.getBoolean(mapProperties.get("jdbc.testWhileIdle")));
            this.dbs.setTestOnBorrow(Boolean.getBoolean(mapProperties.get("jdbc.testOnBorrow")));
            this.dbs.setTestOnReturn(Boolean.getBoolean(mapProperties.get("jdbc.testOnReturn")));

            //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
            this.dbs.setTimeBetweenEvictionRunsMillis(Integer.parseInt(mapProperties.get("jdbc.timeBetweenEvictionRunsMillis")));
            //连接在池中保持空闲而不被空闲连接回收器线程(如果有)回收的最小时间值，单位毫秒
            this.dbs.setMinEvictableIdleTimeMillis(Integer.parseInt(mapProperties.get("jdbc.minEvictableIdleTimeMillis")));
            //慢日志监控配置
            //this.dbs.setConnectionProperties(mapProperties.get("jdbc.connectionProperties"));
            try {
                this.dbs.setFilters(mapProperties.get("jdbc.filters"));
            } catch (SQLException e) {
                // System.err.println(e.toString());
                e.printStackTrace();
            }
        } catch (Exception e) {
            // System.err.println(e.toString()); 
             e.printStackTrace();
        } finally{
            if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
                    // System.err.println(e.toString());
                    e.printStackTrace();
				}
			}
			//及时释放
			inputStream = null;
			mapProperties = null;
			strProperties = null;
			property = null;
        }
    }
    
    /**
     * 
     */
    public void initDBPool() {
        try {
			if (this.dbs != null) {
				this.closeResourece(null, null, null,this.dbs.getConnection()) ;
			}
		} catch (SQLException e) {
			//this.log.error(ExceptionManager.DB_INIT_C3P0CONNPOOLERROR,e);
			e.printStackTrace();
		}
    }

   /**
    * 
    * @return
    */
    public static final BaseDBManager getDBManager() {
        if (instance == null) {
            try {
                instance = new BaseDBManager();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * 
     */
    public Connection getConnectionByPool(){
        try {
            if(this.dbs != null){
                return this.dbs.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭资源
     * @param result
     * @param psmt
     * @param pro
     * @param connection
     */
    public void closeResourece(ResultSet result, PreparedStatement psmt,CallableStatement pro,Connection connection) {
        try {
            if (result != null) {
                result.close();
                result = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            try {
                if(psmt != null){
                    psmt.close();
                    psmt = null;
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                try {
                    if(pro != null){
                        pro.close();
                        pro = null;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally{
                    try {
                        if(connection != null){
                            connection.close();
                            connection = null;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}