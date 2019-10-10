package com.tlwl.demo.entity;

public class Book {

    private Integer id;
    private String isbn;
    private String title;
    private Integer renter_id;

    public void setId(Integer id){
        this.id = id;
    }

    public Integer getId(){
        return id;
    }

    public void setIsbn(String isbn){
        this.isbn = isbn;
    }

    public String getIsbn(){
        return isbn;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setRenterId(Integer renter_id) {
        this.renter_id = renter_id;
    }

    public Integer getRenterId(){
        return renter_id;
    }

}