package com.sturent.service;
import com.sturent.util.PasswordUtil;


import java.util.Base64;
import com.sturent.model.User;
import com.sturent.dao.UserDAO;

public class AuthService {

    public boolean validateUserInput(String userid,String name,String email,String password,String phone,String role ){

        if(userid==null||userid.isBlank()
            ||name==null||name.isBlank()
            ||email==null||email.isBlank()
            ||password==null||password.isBlank()
            ||phone==null||phone.isBlank()
             ||role==null||role.isBlank()){
                return false;
        }
        if(!userid.matches("[A-Za-z0-9]+")){
            return false;
        }
        if(!name.matches("[A-Za-z]+")){
            return false;
        }
        if(!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+{2,}$")){
            return false;
        }
        if(password.length()<6){
            return false;
        }
        if(!phone.matches("\\d{10}")){
            return false;
        }
        if(!role.equalsIgnoreCase("STUDENT")&&!role.equalsIgnoreCase("ADMIN")){
            return false;
        }
         return true;
        }



    private UserDAO userDAO;
    public AuthService(UserDAO userDAO){
        this.userDAO=userDAO;
    }

    public User prepareUserForRegistration(String userid,String name,String email,String password,String phone,String role)
                    throws Exception{

                 if(!validateUserInput(userid,
                         name,email,password,phone,role)){
                     return null;
                 }

                 byte[] salt= PasswordUtil.generateSalt();
                 String passwordHash=PasswordUtil.hashPassword(password,salt);
                 String passwordSalt= Base64.getEncoder().encodeToString(salt);

                 return new User(
                         userid,
                         name,
                         passwordHash,
                         passwordSalt,
                         email,
                         phone,
                         role
                 );






    }
    public boolean registerUser(User user){
        return userDAO.saveUser(user);
    }
    public User login(String email,String password) throws Exception {
        User user = userDAO.findUserByEmail(email);
        if (user == null) {
            return null;
        }
        boolean passwordCorrect = PasswordUtil.verifyPassword(password, user.getPasswordHash(), Base64.getDecoder().decode(user.getPasswordSalt()));


        if (!passwordCorrect) {
            return null;
        }
        return user;
    }
    public String getUserInterface(User user){

       if(user==null){
           return "LOGIN";
       }
       if ("ADMIN".equalsIgnoreCase(user.getRole())){
           return "ADMIN";
       }
       if("STUDENT".equalsIgnoreCase(user.getRole())){
           return "STUDENT";
       }
       return "LOGIN";
        }



    }


