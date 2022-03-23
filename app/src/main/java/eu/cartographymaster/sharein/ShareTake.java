package eu.cartographymaster.sharein;

/*
* Activity to go to either Share Activity or Take Activity
* */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShareTake extends AppCompatActivity {

    //Layout elements
    TextView tName;
    Button btnTake, btnShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_take);

        //Get user name from LogIn activity to show in the upper right corner
        tName = findViewById(R.id.username);
        String name = LogIn.name;
        tName.setText(name);

        btnTake = findViewById(R.id.btn_take);
        btnShare = findViewById(R.id.btn_share);

        //Go to the map activity to take things
        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShareTake.this, TakeActivity.class));
            }
        });

        //Go to the Share activity to share items
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShareTake.this, ShareActivity.class));
            }
        });

    }
}