package com.tlwl.demo.dao.impl;

import java.util.List;

import com.tlwl.demo.base.BaseDao;
import com.tlwl.demo.entity.Book;

public class BookDaoImpl extends BaseDao {

    static BookDaoImpl bookDao = null;
    static{
        bookDao = new BookDaoImpl();
    }

    public static BookDaoImpl getInstance(){
        return bookDao;
    }

    /**
     * 
     * @param book
     * @return
     */
    public String searchBooksList(Book book){
        String resultJson = null;
        String sql = "SELECT id,isbn,title FROM books";
        try {
            resultJson = this.executeQueryListToJson(sql,null);
        } catch (Exception e) {
            resultJson = this.resultFailure(e.toString());
        }
        return resultJson;
    }


}