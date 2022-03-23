package eu.cartographymaster.sharein;

/*
 * Get Food Item Location Activity
 * user selects the location of an food item which is to be shared --> lat, long, address (generated) is obtained
 */


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SelectItemLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_item_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1340);
        }
        //Starting page centered at Dresden
        LatLng dresden = new LatLng(51.051877, 13.741517);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

        CameraPosition cam_pos = new CameraPosition.Builder()
                .target(dresden)
                .zoom(13)
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cam_pos));

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {

            @Override
            public void onCameraIdle() {
                // Get the center coordinate of the map, if the overlay view is centered too
                CameraPosition cameraPosition = mMap.getCameraPosition();
                LatLng currentCenter = cameraPosition.target;

                TextView addressField = findViewById(R.id.editText_address);
                //Latitude and Lontitude are invisible, since we don't want to show directly the coordinates to the user
                TextView latitude = findViewById(R.id.textView_Lat_invisible);
                TextView longitude = findViewById(R.id.textView_Lon_invisible);

                // Initialize geocoder reading the address from the map center
                Geocoder geocoder =
                        new Geocoder(SelectItemLocationActivity.this, Locale.getDefault());
                try {
                    List<Address> address_list
                            = geocoder.getFromLocation(currentCenter.latitude, currentCenter.longitude,  1);
                    if (address_list.size() != 0) {
                        String street_name = address_list.get(0).getThoroughfare();
                        String street_number = address_list.get(0).getSubThoroughfare();
                        String city = address_list.get(0).getLocality();
                        String country = address_list.get(0).getCountryName();
                        String address_string = street_name+" "+street_number+", "+city+", "+country;
                        addressField.setText(address_string);

                        //Getting Coordinates from current center
                        double lat = currentCenter.latitude;
                        String stringDouble_lat= Double.toString(lat);

                        double lon = currentCenter.longitude;
                        String stringDouble_lon= Double.toString(lon);

                        latitude.setText(stringDouble_lat);
                        longitude.setText(stringDouble_lon);

                    }
                    else {
                        addressField.setText("\u2794 no address found");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(this, "Location cannot be obtained due to missing permission.", Toast.LENGTH_LONG).show();
        }
    }

    //Click to confirm address and go back to ShareActivity menu
    public void onClickBackShareActivity(View view) {
        Intent intent = new Intent();

        EditText editText = findViewById(R.id.editText_address);
        String text_loc = editText.getText().toString();

        TextView textView_lat = findViewById(R.id.textView_Lat_invisible);
        String text_lat = textView_lat.getText().toString();

        TextView textView_lon = findViewById(R.id.textView_Lon_invisible);
        String text_lon = textView_lon.getText().toString();

        //carry to the next intent - second activity
        intent.putExtra("text_location", text_loc);
        intent.putExtra("text_latitude", text_lat);
        intent.putExtra("text_longitude", text_lon);
        setResult(RESULT_OK, intent);
        finish();
    }
}

