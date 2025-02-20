package com.example.flowershop_mobileapp.models;

public class ChangePassword {
    private String curpass;
    private String newpass;

    public ChangePassword(String curpass, String newpass) {
        this.curpass = curpass;
        this.newpass = newpass;
    }

    public String getCurpass() {
        return curpass;
    }

    public void setCurpass(String curpass) {
        this.curpass = curpass;
    }

    public String getNewpass() {
        return newpass;
    }

    public void setNewpass(String newpass) {
        this.newpass = newpass;
    }
}
