package com.tlwl.demo.ice.main;

import com.tlwl.demo.service.*;

import com.zeroc.Ice.Current;
import org.apache.log4j.Logger;
import com.tlwl.demo.ice.IRequest.*;
public class RequestI implements Request{

    private static Logger log = Logger.getLogger(RequestI.class);

    public String setRequest(String method, String json, Current current) {
        String resultString = null;
        try {
			if (method.startsWith("user_")) {
				UserService userService = new UserService();
				resultString = userService.userCall(method, json);
				userService = null;
				
				//return new UserService().userCall(method, json);
            }else if(method.startsWith("book_")){
                BookService bookService = new BookService();
                resultString = bookService.bookCall(method,json);
                bookService = null;
            }else{
                return "{\"message\":\"m=" + method + ",\"request name is not exists\"\",\"code\":2}";
            }
            return resultString;
        } catch (Exception e) {
            log.error("response error,m:" + method ,e);
        }
        return "{\"message\":\"response err\",\"code\":3}";
    }
}