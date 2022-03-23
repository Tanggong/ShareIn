package eu.cartographymaster.sharein;

/*
* Activity to reserve a food item
* */

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.cartographymaster.sharein.fragments.FilterFragment;
import eu.cartographymaster.sharein.fragments.ListFragment;
import eu.cartographymaster.sharein.fragments.MapsFragment;

public class TakeActivity extends AppCompatActivity {

    public static List<FoodItem> foodItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take);


        // Layout Elements: initial Fragments and Buttons
        ListFragment listFragment = new ListFragment();
        MapsFragment mapFragment = new MapsFragment();
        FilterFragment filterFragment = new FilterFragment();
        Button bnt_filter = findViewById(R.id.btn_filter);
        Button bnt_showMap = findViewById(R.id.btn_showMap);
        Button bnt_showList = findViewById(R.id.btn_showList);
        TextView tName = findViewById(R.id.txt_username);

        // get username
        String name = LogIn.name;
        tName.setText(name);

        // Transaction to change the fragment inside the frame layout
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // initial page: map view
        transaction.replace(R.id.flfragment, mapFragment, null);
        transaction.commit();

        //get all available food items from database
        foodItem = MainActivity.foodItemDao.getAvailableFoodItems();

        // create filter button click event to active filter dialog
        bnt_filter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                filterFragment.show(
                       getSupportFragmentManager(),"FilterFragment"
               );
            }
        });

        // create map button click event to view map
        bnt_showMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentTransaction trs_map = getSupportFragmentManager().beginTransaction();
                trs_map.replace(R.id.flfragment, mapFragment, null);
                trs_map.setReorderingAllowed(true);
                trs_map.addToBackStack(null);
                trs_map.commit();
            }
        });

        // create map button click event to view list
        bnt_showList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                FragmentTransaction trs_list = getSupportFragmentManager().beginTransaction();
                trs_list.replace(R.id.flfragment, listFragment, null);
                trs_list.setReorderingAllowed(true);
                trs_list.addToBackStack(null);
                trs_list.commit();

            }
        });
    }
}