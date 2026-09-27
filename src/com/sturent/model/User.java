package com.sturent.model;

public class User {
    private String userid;
    private String name;
    private String passwordHash;
    private String passwordSalt;
    private String email;
    private String phone;
    private String role;


   public User(String userid,String name,String passwordHash,String passwordSalt,String email,String phone,String role  ){
       this.userid=userid;
       this.name=name;
       this.passwordHash=passwordHash;
       this.passwordSalt=passwordSalt;
       this.email=email;
       this.phone=phone;
       this.role=role;

   }

public String getUserid(){
        return userid;
}
public void setUserid(String userid){

        this.userid=userid;
}

    public String getName(){
        return name;

    }
    public void setName(String name) {

        this.name = name;
    }
    public String getPasswordHash(){
        return passwordHash;

    }
    public void setPasswordHash(String passwordHash) {

        this.passwordHash = passwordHash;
    }
    public String getEmail(){
        return email;

    }
    public void setEmail(String email ) {

        this.email = email;
    }
    public String getPhone(){
        return phone;

    }
    public void setPhone(String phone) {

        this.phone = phone;
    }
    public String getRole(){
        return role;

    }
    public void setRole(String role) {
        this.role = role;
    }

    public String getPasswordSalt(){
        return passwordSalt;
    }

    public void setPassworSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public int getNumericUserId() {
        if (userid == null || userid.isBlank()) return 1;
        try {
            return Integer.parseInt(userid);
        } catch (NumberFormatException e) {
            return Math.abs(userid.hashCode() % 1000) + 1;
        }
    }
}
