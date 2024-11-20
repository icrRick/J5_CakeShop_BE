package com.fpoly.java5.utils;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.java5.entities.User;
import com.fpoly.java5.jpa.UserJPA;
@Service
public class UserUtil {
    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    private UserJPA userJPA;

    public Optional<User> getUserCookie() {
        String userIdValue = cookieUtil.getValue(Contants.COOKIE_USER_ID);

        try {
            int userId = Integer.parseInt(userIdValue);
            return userJPA.findById(userId);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
