package eu.cartographymaster.sharein;

/*
* Activity to share a food item
* user inputs data about the food item to be shared and inserted to the DB
* */


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ShareActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //layout elements
    EditText etTitle, etDescription, etPickUpDT, etPickUpLoc;
    TextView latInShare, lonInShare;
    Spinner spFoodCat;
    ImageButton btnAddLoc;
    Button btnShareThis;

    //variables
    private int userID, selectedFoodCatID;

    //Call back to retrieve pre-existing data entered by the user once the intent for picking address has run
    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result != null && result.getResultCode()==RESULT_OK) {
                        if (result.getData() != null && result.getData().getStringExtra("text_location") != null) {
                            etPickUpLoc.setText(result.getData().getStringExtra("text_location"));

                        }

                        if (result.getData() != null && result.getData().getStringExtra("text_latitude") != null) {
                            latInShare.setText(result.getData().getStringExtra("text_latitude"));

                        }

                        if (result.getData() != null && result.getData().getStringExtra("text_longitude") != null) {
                            lonInShare.setText(result.getData().getStringExtra("text_longitude"));

                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        //setting up layout elements
        etTitle = findViewById(R.id.et_title);

        spFoodCat = findViewById(R.id.sp_food_cat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.food_categories,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFoodCat.setAdapter(adapter);
        spFoodCat.setOnItemSelectedListener(this);

        etDescription = findViewById(R.id.et_description);
        etPickUpDT = findViewById(R.id.et_pickup_dt);
        etPickUpLoc = findViewById(R.id.et_pickup_loc);
        btnAddLoc = findViewById(R.id.btn_add_location);
        btnShareThis = findViewById(R.id.btn_share_this);
        latInShare = findViewById(R.id.textView_Lat_inShare_invisible);
        lonInShare = findViewById(R.id.textView_Lon_inShare_invisible);

        //take the current logged-in userID from the LogIn activity
        userID = LogIn.userID;

        //go to select item location activity, to get the pickup location of the sharing item
        btnAddLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareActivity.this, SelectItemLocationActivity.class);
                startForResult.launch(intent);
            }

        });

        //Bring address from SelectItemLocationActivity
        Intent intent = getIntent();
        String text_loc = intent.getStringExtra("text_location");
        String text_lat = intent.getStringExtra("text_latitude");
        String text_lon = intent.getStringExtra("text_longitude");


        //display information by connection
        EditText editText = etPickUpLoc;
        editText.setText(text_loc);

        TextView textView_lat = latInShare;
        textView_lat.setText(text_lat);

        TextView textView_lon = lonInShare;
        textView_lon.setText(text_lon);



        //verifying and adding food item to the database
        btnShareThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting the inputs of the user to insert a new food item
                String title = etTitle.getText().toString().trim();
                String pickUpPeriod = etPickUpDT.getText().toString().trim();
                String description = etDescription.getText().toString().trim();

                String str_lat = latInShare.getText().toString().trim();
                double latitude = Double.parseDouble(str_lat);

                String str_lon = lonInShare.getText().toString().trim();
                double longitude = Double.parseDouble(str_lon);


                String address = etPickUpLoc.getText().toString().trim();

                //note: bookedID, isAvailable and isBooked fields of foodItem is set by the alternative constructor. refer FoodItem.java

                //check all the input fields are filled by the user
                if (title.isEmpty() || pickUpPeriod.isEmpty() || description.isEmpty() || address.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                //insert the food item to the database if the verification status is ok
                else
                {
                    FoodItem foodItem = new FoodItem(userID, title, pickUpPeriod, description, latitude, longitude, address, selectedFoodCatID);
                    MainActivity.foodItemDao.insertFoodItem(foodItem);
                    Toast.makeText(getApplicationContext(), "Your food is being shared ! :)", Toast.LENGTH_SHORT).show();
                }

                //Go back to Welcome activity (share & take)
                Intent intent = new Intent(ShareActivity.this, ShareTake.class);
                startForResult.launch(intent);


            }
        });
    }

    //override functions of spinner: spFoodCat
    //obtaining the selected food category id
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedFoodCatID = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}