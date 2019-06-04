package com.yuzu.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String user;
    private String date;
    private String title;
    private String message;
    private String hash;

    public Task() {
    }

    public Task(String user, String date,String title, String message, String hash) {
    	this.user = user;
    	this.date = date;
    	this.title = title;
        this.message = message;
        this.hash = hash;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}