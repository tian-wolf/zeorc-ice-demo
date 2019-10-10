package com.tlwl.demo.base;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.util.Map;

import com.tlwl.demo.uitls.GsonUtils;

import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import java.util.ListIterator;


public class BaseDao extends OriginalJdbcDao {

    /**
	 * 返回查询数量
	 * @param sql
	 * @param values
	 * @param successMessage
	 * @return JSON:{"code":0,"data":{"count":100},"desc":"返回成功信息"}
	 * @throws SQLException
	 */
	protected String executeQueryCountToJson(String sql ,Object[] values,String successMessage) throws SQLException{
		return  new StringBuffer("{\"code\":0,\"data\":{\"total\":").append(super.executeQueryCount(sql, values)).append("},\"msg\":\"").append(successMessage).append("\"}").toString();
    }

    /**
     * 
     * @author 杨鹏 <yangpeng1@dgg.net>
     * @param sql
     * @param values
     * @return
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public String executeQueryListToJson(String sql, Object[] values) throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException {
		return this.resultSuccess(executeQueryList(sql, values),null);
	}

    
    /**
     * 
     * @param executeQueryList
     * @param object
     * @return  JSON : {"code":0,"data":[{},{},{}....]}
     */
    private String resultSuccess(List<Map<String, Object>> executeQueryList,String successMessage) {
        String resultJson = "";
		String data = null;
		if(executeQueryList != null && executeQueryList.size() > 0){
			data = GsonUtils.toJson(executeQueryList);
		}
		resultJson = setResultJson(0,data,null,null,successMessage);
		this.FreeMapInList(executeQueryList);
		return resultJson;
    }

    /**
     * 
     * @param executeQueryList
     * @param records
     * @param successMessage
     * @return
     */
    public String resultSuccess(List<Map<String,Object>> executeQueryList,Integer records,String successMessage) {
        String resultJson = "";
		String data = null;
		if(executeQueryList != null && executeQueryList.size() > 0){
			data = GsonUtils.toJson(executeQueryList);
		}
		resultJson = setResultJson(0,data,null,String.valueOf(records),successMessage);
		this.FreeMapInList(executeQueryList);
		return resultJson;
    }

    /**
     * 返回成功(无返回数据集)
     * 
     * @param successMessage
     * @return JSON : {"code":0,"msg":sucMsg}
     */
	public String resultSuccess(String successMessage){
		if(successMessage == null){
			successMessage = "exec success";
		}
		return this.setResultJson(0,null,null,null,successMessage);
    }

    /**
     * 
     */
    public String resultFailure(String errorMessage){
        if (errorMessage == null) {
            errorMessage = "exec failure";
        }
        return this.setResultJson(1,null,null,null,errorMessage);
    }

    /**
     * 
     */
    protected String resultFailure(String result,String resultValue,String errorMessage){
		if(errorMessage == null){
			errorMessage = "";
		}
		return new StringBuffer("{\"code\":1,\"data\":{\"").append(result).append("\":\"").append(resultValue).append("\"},\"message\":\"").append(errorMessage.replaceAll("\"", "")).append("\"}").toString();
    }
    
    /**
     * 
     */
    protected String retFailure(int code,String result,String resultValue,String errorMessage){
		if(errorMessage == null){
			errorMessage = "";
		}
		return new StringBuffer("{\"code\":").append(code).append(",\"data\":{\"").append(result).append("\":\"").append(resultValue).append("\"},\"message\":\"").append(errorMessage.replaceAll("\"", "")).append("\"}").toString();
    }
    

    
    /**
     * 
     */
    protected String resultSuccess(String result,String resultValue,String successMessage){
        if(successMessage == null){
            successMessage = "";
        }
        return new StringBuffer("{\"code\":0,\"data\":{\"").append(result).append("\":\"").append(resultValue).append("\"},\"message\":\"").append(successMessage).append("\"}").toString();
    }
    
    /**
     * 
     * @param code
     * @param data
     * @param others
     * @param totals
     * @param message
     * @return JSON:{"code":0,data:"","others":"","totals":0,"message":"xxxxx"}
     */
    private String setResultJson(Integer code,String data,String others,String totals,String message){
        StringBuffer stringBuffer = new StringBuffer("");

        stringBuffer.append("{");
        stringBuffer.append("\"code\":");
        stringBuffer.append(code.toString());

        if(data !=null && !"".equals(data)){
            stringBuffer.append(",\"data\":");
            stringBuffer.append(data);
        }else{
            stringBuffer.append(",\"data\":\"\"");
        }

        if(others !=null && !"".equals(others)){
            stringBuffer.append(",\"others\":");
            stringBuffer.append(others);
        }

        if(totals !=null && !"".equals(totals)){
            stringBuffer.append(",\"totals\":");
            stringBuffer.append(totals);
        }

        if (message !=null && !"".equals(message)) {
            stringBuffer.append(",\"message\":\"");
            stringBuffer.append(message);
            stringBuffer.append("\"");
        }
        stringBuffer.append("}");
        return stringBuffer.toString();
    }

    /**
     * 
     * @param outList
     */
    protected void FreeMapInList(List outList){
		
		if(outList != null && outList.size() > 0){			
			ListIterator iterator = outList.listIterator();
			while(iterator.hasNext()){
				Map inMap  = (Map) iterator.next();
				if(inMap != null){
					inMap.clear();
					inMap = null;				
				}
			}
			outList.clear();
			outList = null;
		}
		
	}

}