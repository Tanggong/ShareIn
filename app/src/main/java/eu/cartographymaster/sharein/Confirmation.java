package eu.cartographymaster.sharein;

/*
conformation activity: provides conformation message when a user takes an item
*/

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Confirmation extends AppCompatActivity {

    TextView tName, confText;
    Button btnHome;
    String intentExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        //Get user name to show in the upper right corner
        tName = findViewById(R.id.username);
        String name = LogIn.name;
        tName.setText(name);

        //Receive intent from List/Map activity
        Intent intent = getIntent();
        intentExtra = intent.getStringExtra(getPackageName());


        FoodItem foodItem = MainActivity.foodItemDao.getFoodItemById(intentExtra);

        confText = findViewById(R.id.conf_text);

        //Obtaining the data related to the selected food item
        String itemTitle = foodItem.getTitle();
        String address = foodItem.getAddress();
        String pickUpTime = foodItem.getPickUpPeriod();

        //Obtaining owner data to potential user who books an item can see them (i.e email)
        Integer userID = foodItem.getUserID();
        User user = MainActivity.userDao.getUserById(userID);
        String email =  user.getEmail();

        //Text to display after a user reserves an item
        confText.setText("Congratulations! \n We booked " + itemTitle + " for you. \n"
                + "You can pick it up at " + address + " at " + pickUpTime +".\n" +
                "For more information you can send email to your buddy " + email );

        //Change data in the database
        //1) Set isbkd -> 1;
        //2) Set bookediD (who reserved) -> current app user;
        //Get itemID
        //Perform query to update item by id;
        Integer foodItemId = foodItem.getItemID();
        MainActivity.foodItemDao.update(foodItemId,LogIn.userID);


        //Go back to share-take activity
        btnHome = findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Confirmation.this, ShareTake.class));
            }
        });
    }
}