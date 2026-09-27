package com.sturent.dao;
import com.sturent.db.DBConnection;
import com.sturent.model.User;

import java.sql.*;

public class UserDAO{
    public boolean saveUser(User user){
        String sql="INSERT INTO users"+
                "(user_id,name,email,password_hash,password_salt,phone,role)"+
                "VALUES(?,?,?,?,?,?,?)";

          try(Connection connection= DBConnection.getConnection();
                PreparedStatement statement=connection.prepareStatement(sql)){

              statement.setString(1, user.getUserid());

              statement.setString(2, user.getName());

              statement.setString(3, user.getEmail());

              statement.setString(4,user.getPasswordHash());

              statement.setString(5,user.getPasswordSalt());

              statement.setString(6,user.getPhone());

              statement.setString(7, user.getRole());

              statement.executeUpdate();
              return true;
          } catch(SQLException e){
              e.printStackTrace();
              return false;


          }
    }
    public User findUserByEmail(String email){
        String sql="""
                SELECT user_id,name,email,password_hash,
                password_salt,phone,role
                FROM defaultdb.users WHERE email=?
                """;
        try(Connection connection=DBConnection.getConnection();
                  PreparedStatement statement=connection.prepareStatement(sql)){

            statement.setString(1,email);
            try(ResultSet resultSet=statement.executeQuery()){
                if(resultSet.next()){
                    return new User(
                            resultSet.getString("user_id"),
                            resultSet.getString("name"),
                            resultSet.getString("password_hash"),
                            resultSet.getString("password_salt"),
                            resultSet.getString("email"),
                            resultSet.getString("phone"),
                            resultSet.getString("role")

                    );
                }

            }
        } catch (SQLException e){
            e.printStackTrace();
        }
         return null;
    }

}
