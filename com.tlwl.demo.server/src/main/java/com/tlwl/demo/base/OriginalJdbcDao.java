package com.tlwl.demo.base;

import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public abstract class OriginalJdbcDao {

    /**
     * 返回结果只有一个值(对象)
     * @author 杨鹏 <yangpeng1@dgg.net>
     * @param sql
     * @param values
     * @return
     * @throws SQLException
     */
    protected Object executeQuerySingle(String sql, Object[] values) throws SQLException {
        return this.resultObject(sql, values, Object.class);
    }

    /**
     * 返回查询数量
     * @author 杨鹏 <yangpeng1@dgg.net>
     * @param sql
     * @param values
     * @return
     * @throws SQLException
     */
    protected Integer executeQueryCount(String sql, Object[] values) throws SQLException {
        return this.resultObject(sql, values, Integer.class);
    }

    /**
     * 返回结果是一个Long型
     * @author 杨鹏 <yangpeng1@dgg.net>
     * @param sql
     * @param values
     * @return Long
     * @throws SQLException
     */
    protected Long executeQuerySingleLong(String sql, Object[] values) throws SQLException {
        return this.resultObject(sql, values, Long.class);
    }

    /**
     * 返回结果是一个Integer型
     * @author 杨鹏 <yangpeng1@dgg.net>
     * @param sql
     * @param values
     * @return Long
     * @throws SQLException
     */
    protected Integer executeQuerySingleInteger(String sql, Object[] values) throws SQLException {
        return this.resultObject(sql, values, Integer.class);
    }

    /**
     * 返回结果是一个Double型
     * @author 杨鹏 <yangpeng1@dgg.net>
     * @param sql
     * @param values
     * @return Long
     * @throws SQLException
     */
    protected Double executeQuerySingleDouble(String sql, Object[] values) throws SQLException {
        return this.resultObject(sql, values, Double.class);
    }

    /**
     * 返回结果是一个Date型
     * @author 杨鹏 <yangpeng1@dgg.net>
     * @param sql
     * @param values
     * @return Long
     * @throws SQLException
     */
    protected Date executeQuerySingleDate(String sql, Object[] values) throws SQLException {
        return this.resultObject(sql, values, Date.class);
    }

    /**
     * 返回结果是一个String型
     * @author 杨鹏 <yangpeng1@dgg.net>
     * @param sql
     * @param values
     * @return String
     * @throws SQLException
     */
    protected String executeQuerySingleString(String sql, Object[] values) throws SQLException {
        return this.resultObject(sql, values, String.class);
    }

    /**
     * @function 查询单个实体对象
     * @description 封装约定：结果中列的名字必须等于实体中属性的名字
     * @example
     * @param sql    = "SELECT * FROM B2B_MEMBER A WHERE A.USERID = ?"
     * @param values = new Object[]{408}
     * @param obj    = new User()
     * @return = {"username":"xxxx","password":"xxx","company":"xxxxx"}
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    protected Map<String, Object> executeQueryDomain(String sql, Object[] values)
            throws SQLException, IllegalAccessException, InvocationTargetException {

        // 日志记录
        // log.info("sql : " + sql);
        // if (log.isInfoEnabled()) {
        //     if (values != null && values.length > 0) {
        //         for (int i = 0; i < values.length; i++) {
        //             log.info("param" + i + " : " + values[i]);
        //         }
        //     }
        // }

        ResultSet           resultSet = null;
        Connection          connection = null;
        PreparedStatement   preparedStatement = null;

        Map<String, Object> mapData = new HashMap<String, Object>();

        try {
            connection = BaseDBManager.getDBManager().getConnectionByPool();
            preparedStatement = connection.prepareStatement(sql);
            this.setParameters(preparedStatement, values);
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            if (resultSet.next()) {
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String colunmName = metaData.getColumnName(i).toLowerCase();
                    Object colunmValue = resultSet.getObject(colunmName);
                    mapData.put(colunmName, colunmValue);
                }
            }
        } finally {
            BaseDBManager.getDBManager().closeResourece(resultSet, preparedStatement, null, connection);
        }

        return mapData;
    }

    /**
     * @function 查询单个实体对象
     * @description 封装约定：结果中列的名字必须等于实体中属性的名字
     * @example
     * @param sql    = "SELECT * FROM B2B_MEMBER A WHERE A.USERID = ?"
     * @param values = new Object[]{408}
     * @param obj    = new User()
     * @return = {"username":"xxxx","password":"xxx","company":"xxxxx"}
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    protected Map<String, Object> executeTransQueryDomain(String sql, Object[] values, Connection conn)
            throws SQLException, IllegalAccessException, InvocationTargetException {

        // 日志记录
        // log.info("sql : " + sql);
        // if (log.isInfoEnabled()) {
        //     if (values != null && values.length > 0) {
        //         for (int i = 0; i < values.length; i++) {
        //             log.info("param" + i + " : " + values[i]);
        //         }
        //     }
        // }

        // Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Map<String, Object> mapData = new HashMap<String, Object>();

        try {
            // conn = BaseDBManager.getDBManager().getConectionByPool();
            ps = conn.prepareStatement(sql);
            this.setParameters(ps, values);
            rs = ps.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            if (rs.next()) {
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String colunmName = metaData.getColumnName(i).toLowerCase();
                    Object colunmValue = rs.getObject(colunmName);
                    mapData.put(colunmName, colunmValue);
                }
            }
        } finally {
            BaseDBManager.getDBManager().closeResourece(rs, ps, null, null);
        }

        return mapData;
    }

    /**
     * 
     * 结果集返回多行
     * 
     * @param sql
     * @param values
     * @return List<Map<String,Object>> : [{name=admin, password=123456},
     *         {name=admin, password=123456}.......]
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    protected List<Map<String, Object>> executeQueryList(String sql, Object[] values)
            throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException {

        // 日志记录
        // log.info("sql : " + sql);
        // if (log.isInfoEnabled()) {
        //     if (values != null && values.length > 0) {
        //         for (int i = 0; i < values.length; i++) {
        //             log.info("param" + i + " : " + values[i]);
        //         }
        //     }
        // }

        ResultSet           resultSet = null;
        Connection          connection = null;
        PreparedStatement   preparedStatement = null;
        
        List<Map<String, Object>> resultList = null;

        try {
            connection = BaseDBManager.getDBManager().getConnectionByPool();
            preparedStatement = connection.prepareStatement(sql);
            this.setParameters(preparedStatement, values);
            resultSet = preparedStatement.executeQuery();
            resultList = this.resultCursorToList(resultSet);
        } finally {
            BaseDBManager.getDBManager().closeResourece(resultSet, preparedStatement, null, connection);
        }

        return resultList;
    }

    /**
     * 
     * 结果集返回多行
     * 
     * @param sql
     * @param values
     * @return List<Map<String,Object>> : [{name=admin, password=123456},
     *         {name=admin, password=123456}.......]
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    protected List<Map<String, Object>> executeQueryList(String sql, Object[] names, Object[] values)
            throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException {

        // 日志记录
        // log.info("sql : " + sql);
        // if (log.isInfoEnabled()) {
        //     if (values != null && values.length > 0) {
        //         for (int i = 0; i < values.length; i++) {
        //             log.info("param" + i + " : " + values[i]);
        //         }
        //     }
        // }

        ResultSet           resultSet = null;
        Connection          connection = null;
        PreparedStatement   preparedStatement = null;
        
        List<Map<String, Object>> resultList = null;

        try {
            connection = BaseDBManager.getDBManager().getConnectionByPool();
            preparedStatement = connection.prepareStatement(sql);
            this.setParameters(preparedStatement, values);
            resultSet = preparedStatement.executeQuery();
            resultList = this.resultCursorToList(resultSet, names);
        } finally {
            BaseDBManager.getDBManager().closeResourece(resultSet, preparedStatement, null, connection);
        }

        return resultList;
    }

    /*******************************************************************************/
    /**************************** 辅助处理方法 *****************************************/
    /*******************************************************************************/

    /**
     * 返回单个值处理
     * 
     * @param <T>
     * @param sql
     * @param values
     * @param entityClass
     * @return
     * @throws SQLException
     */
    private <T> T resultObject(String sql, Object[] values, Class<?> entityClass) throws SQLException {

        // 日志记录
        // log.info("sql : "+sql);
        // if(log.isInfoEnabled()){
        // if(values != null && values.length > 0){
        // for(int i=0;i<values.length;i++){
        // log.info("param"+i+" : "+values[i]);
        // }
        // }
        // }

        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = BaseDBManager.getDBManager().getConnectionByPool();
            preparedStatement = connection.prepareStatement(sql);
            this.setParameters(preparedStatement, values);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (entityClass.equals(String.class)) {
                    return (T) resultSet.getString(1);
                } else if (entityClass.equals(Long.class)) {
                    return (T) new Long(resultSet.getLong(1));
                } else if (entityClass.equals(Integer.class)) {
                    return (T) new Integer(resultSet.getInt(1));
                } else if (entityClass.equals(Double.class)) {
                    return (T) new Double(resultSet.getDouble(1));
                } else if (entityClass.equals(Date.class)) {
                    return (T) resultSet.getDate(1);
                }else{
					return (T)resultSet.getObject(1);
				}
			}
		} finally{
			BaseDBManager.getDBManager().closeResourece(resultSet, preparedStatement,null, connection);
		}

		return null;
	}

	/**
	 * 将游标的结果封装为List<Map<String,Object>>
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private List<Map<String,Object>> resultCursorToList(ResultSet resultSet) throws SQLException{
		
		ResultSetMetaData metaData = null;
		String colunmName = null;
		Object colunmValue = null;
		Map<String,Object> mapData = null;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		metaData = resultSet.getMetaData();
		while(resultSet.next()) {
			mapData = new ConcurrentHashMap<String,Object>();
			for (int i = 1; i<=metaData.getColumnCount(); i++) {
				colunmName = metaData.getColumnName(i).toLowerCase();
				colunmValue = resultSet.getObject(colunmName);
				if(colunmValue != null){
					mapData.put(colunmName, colunmValue);
				}
			}
			list.add(mapData);
		}
		return list;
		
	}
	
	/**
	 * 将游标的结果封装为List<Map<String,Object>>
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private List<Map<String,Object>> resultCursorToList(ResultSet resultSet, Object[] names) throws SQLException{
		
		ResultSetMetaData metaData = null;
		String colunmName = null;
		Object colunmValue = null;
		Map<String,Object> mapData = null;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		metaData = resultSet.getMetaData();
		while(resultSet.next()) {
			mapData = new ConcurrentHashMap<String,Object>();
			for (int i = 1; i<=metaData.getColumnCount(); i++) {
				colunmName = names[i-1].toString();
				colunmValue = resultSet.getObject(colunmName.toUpperCase());
				
				if(colunmValue != null){
					mapData.put(colunmName, colunmValue);
				}
			}
			list.add(mapData);
		}
		return list;
	}
	

	
	/**
	 * 传递参数处理
	 * @param pstmt
	 * @param params
	 * @throws SQLException
	 */
	protected void setParameters(PreparedStatement pstmt, Object[] params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
            }
        }
    }
}