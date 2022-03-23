package eu.cartographymaster.sharein;

/*
* Activity to SignUp a user
* */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    //Layout elements
    EditText etFirstName, etLastName, etUserName, etEmail, etPhone, etPassword;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etFirstName = findViewById(R.id.et_firstname);
        etLastName = findViewById(R.id.et_lastname);
        etUserName = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);

        btnSignup = findViewById(R.id.btn_signup);

        // Register a new user
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get the inputs of the user user
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String userName = etUserName.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                String password = etPassword.getText().toString();

                //Create a new user using constructor
                User user = new User(firstName, lastName, userName, password,email, phone);

                //Check if user filled all necessary fields
                if (firstName.isEmpty() || lastName.isEmpty() || userName.isEmpty()
                        || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please, fill all fields!", Toast.LENGTH_SHORT).show();
                } else {

                    //Check if user exists
                    User newUser = MainActivity.userDao.checkIfUserExists(userName,email);
                    if (newUser != null) {

                        Toast.makeText(getApplicationContext(), "User already exists!", Toast.LENGTH_SHORT).show();

                    } else {

                    //Register user
                    MainActivity.userDao.insertUser(user);
                    Toast.makeText(getApplicationContext(), "User registered!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUp.this, LogIn.class)); }
                }
        }

    });
}}