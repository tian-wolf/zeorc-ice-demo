package com.tlwl.demo.uitls;

import com.google.gson.Gson;

public class GsonUtils {

    private static Gson gson = null;

    /**
     * 静态代码块(初始化GSON)
     */
    static{
        gson = new Gson();
    }

    /**
	 * 获取PHP请求JSON串(填充Bean对象)
	 * @param <T>
	 * @param json
	 * @param classOfT
	 * @return <T> T
	 */
	public static <T> T getJson(String json,Class<T> classOfT){
		return  gson.fromJson(json, classOfT);
	}
	
	/**
	 * 将对象转换为JSON字符串
	 * @param obj
	 * @return String
	 */
	public static String toJson(Object obj){
		return gson.toJson(obj);
	}
	
	/**
     * 
     * @return
     */
	public static Gson getInstance() {
		return gson;
	}

}