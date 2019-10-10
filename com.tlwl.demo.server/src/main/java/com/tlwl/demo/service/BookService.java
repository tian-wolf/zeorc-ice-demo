package com.tlwl.demo.service;

import com.tlwl.demo.dao.impl.BookDaoImpl;
import com.tlwl.demo.entity.Book;
import com.tlwl.demo.uitls.GsonUtils;

import org.apache.log4j.Logger;

public class BookService {

    private static Logger log = Logger.getLogger(BookService.class);

    BookDaoImpl bookDao = BookDaoImpl.getInstance();

    public String bookCall(String method,String json){
        if (method.equals("book_list")) {
            return this.searchBookList(json);
        }else{
            throw new RuntimeException("forward request name is not exists.");
        }
    }

    /**
     * 
     * @param json
     * @return
     */
    public String searchBookList(String json){
        //System.out.println("php request :  " + json);
        log.info("php request :  " + json);

        Book book = GsonUtils.getInstance().fromJson(json,Book.class);
        String resultJson = this.bookDao.searchBooksList(book);
        log.info("java response : "+resultJson);
        //System.out.println("java response : "+resultJson);
        book = null;
        return resultJson;
    }


}