package com.tlwl.demo.base;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

public class BaseUtils extends BaseDao {
    
    private static Logger log = Logger.getLogger(BaseUtils.class);
    private static BaseUtils baseUtils = new BaseUtils();
    
    private static List<Map<String, Object>> parList = null;

	/**
	 * 获取实例对象
	 * 
	 * @return
	 */
	public static BaseUtils getInstance() {
		return baseUtils;
	}

	/**
	 * 将数组转换为字符串 返回格式：字段1,字段2,字段3,字段4
	 * 
	 * @param srcArray  数组
	 * @param srcType   数组类型
	 * @return
	 */
	public static String transformArrayToString(Object[] srcArray, Class<?> srcType) {

		if (srcArray == null && srcArray.length <= 0) {
			return null;
		}

		StringBuffer stringBuffer = new StringBuffer("");

		for (Object object : srcArray) {
			if (object != null && !"".equals(object)) {
				if (srcType.equals(String.class)) {
					stringBuffer.append("'" + object + "'");
				}
				if (srcType.equals(Long.class) || srcType.equals(Integer.class)) {
					stringBuffer.append(object);
				}
				stringBuffer.append(",");
			}
		}
		stringBuffer.deleteCharAt(stringBuffer.length() - 1);

		return stringBuffer.toString();
	}

	/**
	 * 将数组数据全部取出
	 * 
	 * @param array
	 * @return
	 */
	public static String getArrayString(String[] array) {
		StringBuffer stringBuffer = new StringBuffer("");
		if (array != null) {
			for (String string : array) {
				stringBuffer.append(string).append("\n");
				stringBuffer.append("<br>");
			}
		}
		return stringBuffer.toString();
	}

    /**
     * 判断变量是否不为空
     * @param string
     * @return Boolean true|false
     */
	public static boolean isNotEmpty(String string) {

		if (string != null && !"".equals(string.trim())) {
			return true;
		}
		return false;
	}

    /**
     * 判断变量是否不为NULL
     * @param object
     * @return Boolean  true|false
     */
	public static boolean isNotNull(Object object) {

		if (object != null) {
			return true;
		}
		return false;
	}

}