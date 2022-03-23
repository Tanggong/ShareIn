package eu.cartographymaster.sharein;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;

@Dao
public interface UserDao {

    @Insert
    void insertUser (User user);

    @Delete
    void deleteUser (User user);

    @Query("SELECT * FROM user_data where user_name = (:userName) and pw_hash = (:password)")
    User checkUserCredentials(String userName, String password);

    @Query("SELECT * FROM user_data where user_name = (:userName) and email = (:email)")
    User checkIfUserExists(String userName, String email);

    //Get food item by id
    @Query("SELECT * FROM user_data where userID = (:userId) ")
    public User getUserById (Integer userId);

}
