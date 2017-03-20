package com.tourtle.web.services;

import com.tourtle.web.dao.LoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Handles Logins
 */
@Service
public class LoginService {
    
    @Autowired
    LoginDao loginDao;

    public boolean loginMobile(String username, String password) {
        return loginDao.loginMobile(username, password);
    }

    public boolean loginWeb(String username, String password) {
        return loginDao.loginWeb(username, password);
    }
}
