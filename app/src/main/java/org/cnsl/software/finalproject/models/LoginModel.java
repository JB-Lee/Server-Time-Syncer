package org.cnsl.software.finalproject.models;

public class LoginModel {

    public boolean doLogin(String id, String pw) {
        return id.equals(pw);
    }
}
