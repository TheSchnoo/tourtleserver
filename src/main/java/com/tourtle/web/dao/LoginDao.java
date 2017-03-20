package com.tourtle.web.dao;

/**
 * DAO responsible for checking if profiles exist
 */
public interface LoginDao {
    
    boolean loginMobile(String name, String password);

    boolean loginWeb(String username, String password);

}
