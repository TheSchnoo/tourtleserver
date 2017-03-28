package com.tourtle.web.services;

import com.tourtle.web.dao.LoginDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginServiceTest {

    private final static String USERNAME = "username";
    private final static String PW = "pass";

    @Mock
    private LoginDao loginDao;
    @InjectMocks
    private LoginService loginService;

    @Before
    public void setUp() {
        when(loginDao.loginMobile(USERNAME, PW)).thenReturn(true);
        when(loginDao.loginWeb(USERNAME, PW)).thenReturn(true);

    }

    @Test
    public void verifyLoginMobile() throws Exception {
        assertThat(loginService.loginMobile(USERNAME, PW)).isTrue();
    }

    @Test
    public void verifyLoginWeb() throws Exception {
        assertThat(loginService.loginWeb(USERNAME, PW)).isTrue();
    }

}