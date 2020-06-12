package org.cnsl.software.finalproject.models;

public class LoginModel {

    public boolean doLogin(String id, String pw) {
        if (id.equals(pw)) {
            return true;
        } else {
            return false;
        }
    }
}
