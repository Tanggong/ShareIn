package eu.cartographymaster.sharein;

/*
* Main Activity
*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    //DB related
    static String db_name = "AppDatabase.sqlite";
    static UserDao userDao;
    public static FoodItemDao foodItemDao;

    //Layout elements
    Button btnSignUp;
    Button btnLogIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Check if db already exists and if not, copy from the file
        final File dbFile = this.getDatabasePath(db_name);
        if (!dbFile.exists()) {
            try {
                copyDatabaseFile(dbFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Building database
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, db_name)
                .allowMainThreadQueries().build();

        //Getting dao from database
        userDao = database.userDao();
        foodItemDao = database.foodItemDao();

        //Setting up layout elements
        btnSignUp = findViewById(R.id.btn_signup);
        btnLogIn = findViewById(R.id.btn_login);

        //Register button: Go to the registration page
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });

        //Login button: Go to the login page
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LogIn.class));
            }
        });
    }

    //Copy database
    private void copyDatabaseFile(String destinationPath) throws IOException {
        InputStream assetsDB = this.getAssets().open(db_name);
        OutputStream dbOut = new FileOutputStream(destinationPath);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = assetsDB.read(buffer)) > 0) {
            dbOut.write(buffer, 0, length);
        }
        dbOut.flush();
        dbOut.close();
    }
}