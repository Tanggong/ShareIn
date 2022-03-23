package eu.cartographymaster.sharein;

/*
* Login Activity: Simple user login with username and password
*/

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn extends AppCompatActivity {

    static Integer userID;
    static String name;
    EditText etUserName, etPassword;
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //Setting up layout elements
        etUserName = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        //Login when the login button is clicked
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameText = etUserName.getText().toString().trim();
                String passwordText = etPassword.getText().toString().trim();

                //validate user inputs
                if (userNameText.isEmpty() || passwordText.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please, fill all fields!", Toast.LENGTH_SHORT).show();}
                else {
                    //If the user inputs are correct --> validate username and PW
                    User user = MainActivity.userDao.checkUserCredentials(userNameText, passwordText);
                    if (user == null){
                        Toast.makeText(getApplicationContext(), "Invalid credentials!", Toast.LENGTH_SHORT).show();
                    } else {
                        userID = user.getUserID();
                        name = user.getUserName();
                        //If the username and PW matches --> perform login
                        startActivity(new Intent(LogIn.this, ShareTake.class));
                    }
                }
            }
        });
    }
}