package com.tlwl.demo.dao;

import java.util.Map;
import com.tlwl.demo.entity.*;

@SuppressWarnings("unchecked")
public interface BookDao {
    public Map searchBooksList(Book book);
}