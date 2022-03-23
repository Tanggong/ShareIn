package eu.cartographymaster.sharein.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import eu.cartographymaster.sharein.Confirmation;
import eu.cartographymaster.sharein.FoodItem;
import eu.cartographymaster.sharein.MainActivity;
import eu.cartographymaster.sharein.R;

/**
 * fragment to show food items on the map
 */
public class MapsFragment extends Fragment {

    private GoogleMap mMap;
    private ItemViewModel viewModel;
    List<FoodItem>  selectedItem;
    static Button btnShowNear, btnReserve;

    //Define variables needed for accessing location
    LocationManager locationManager;
    LocationListener locationListener;
    double lat;
    double lon;



    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         */

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1340);
            }

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));

            //get viewModel
            viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

            //create the observer to observe the update item list
            final androidx.lifecycle.Observer<List<FoodItem>> itemsObserver = new Observer<List<FoodItem>>() {
                @Override
                public void onChanged(List<FoodItem> foodItems) {
                    selectedItem =  viewModel.getSelectedItem().getValue();
                    addMarkers(selectedItem);
                }};

            //get liveData
            viewModel.getSelectedItem().observe(getViewLifecycleOwner(), itemsObserver);

            CameraPosition cam_pos =
                    new CameraPosition.Builder()
                            .target(new LatLng(51.051877, 13.741517))
                            .zoom(12)
                            .build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cam_pos));



            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                   //Get data which were put
                    String foodItemId = (String) marker.getTag();
                    //Toast.makeText(getContext(), foodItemId, Toast.LENGTH_LONG).show();
                    btnReserve.setVisibility(View.VISIBLE);
                    btnReserve.setOnClickListener(new View.OnClickListener() {
                        @Override
                       public void onClick(View view) {
                            Intent intent = new Intent(getContext(), Confirmation.class);
                            intent.putExtra(getActivity().getPackageName(), foodItemId);
                            startActivity(intent);
                        }
                    });

                    return false;
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //inflate the layout for map fragment
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }


        //Implement location manager to get user coordinates and show objects which are around user
        locationManager =
                (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if(location!=null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                }
            }
            public void onStatusChanged(String provider, int status, Bundle
                    extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}

        };

        //Ask about user permission to get their location
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,10000, 10, locationListener);
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1340);
        }

        //Initialize two buttons from map fragment
        btnReserve = (Button)rootView.findViewById(R.id.btn_reserve);
        btnShowNear = (Button)rootView.findViewById(R.id.show_near);


        //Show all food items which are near user
        btnShowNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraPosition cam_pos =
                        new CameraPosition.Builder()
                                .target(new LatLng(lat, lon))
                                .zoom(14)
                                .build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cam_pos));

                btnShowNear.setVisibility(View.GONE);  }
        });

        return rootView;
    }


    //for loop to add available food items on the map
    public void addMarkers(List<FoodItem> selectedItem){
        mMap.clear();
        for (int i = 0; i < selectedItem.size(); i++){
            mMap.addMarker(new MarkerOptions()
                    .position(selectedItem.get(i).getPosition())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                    .title(selectedItem.get(i).getTitle())
                    .snippet(selectedItem.get(i).getDescription()))
                    //Put information about foodItemId in tag to connect data with marker
                    .setTag(selectedItem.get(i).getItemID().toString());

        }
    }


}