package com.accenture.accpenture.database;

public class UserHelperClassFirebase {
    private String username, password, email, fName, lName, phone;

    public UserHelperClassFirebase() {
    }

    public UserHelperClassFirebase(String username, String password, String email, String fName, String lName, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() { return email; }

    public String getFName() { return fName; }

    public String getLName() { return lName; }

    public String getPhone() { return phone; }
}
