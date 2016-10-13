package com.liudong.weibo.entity.response;

import com.liudong.weibo.entity.User;

import java.util.List;

/**
 * Created by liudong on 2016/6/11.
 */
public class FansResponse {
    private List<User> users;
    private int total_number;
    private int next_cursor;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public int getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(int next_cursor) {
        this.next_cursor = next_cursor;
    }
}
