package eu.cartographymaster.sharein;

/*
* User Class
* */

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (tableName = "user_data"
)
public class User {

    @PrimaryKey (autoGenerate = true)
    private Integer userID;

    @NonNull
    @ColumnInfo (name = "first_name")
    private String firstName;

    @NonNull
    @ColumnInfo (name = "last_name")
    private String lastName;

    @NonNull
    @ColumnInfo (name = "user_name")
    private String userName;

    @NonNull
    @ColumnInfo (name = "pw_hash")
    private String pwHash;

    @NonNull
    private String email;

    @ColumnInfo (name = "phone_num")
    private String phoneNumber;


    // constructor
    public User(@NonNull String firstName, @NonNull String lastName, @NonNull String userName, @NonNull String pwHash, @NonNull String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.pwHash = pwHash;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // setter for the primary key
    @NonNull
    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    // Getters
    @NonNull
    public Integer getUserID() {
        return userID;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    @NonNull
    public String getPwHash() {
        return pwHash;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
