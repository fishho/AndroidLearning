package com.cfish.mvpdemo1.model;

/**
 * Created by GKX100217 on 2016/4/14.
 */
public interface IUser {
    String getName();
    String getPasswd();
    int checkUserValidity(String name,String passwd);
}
