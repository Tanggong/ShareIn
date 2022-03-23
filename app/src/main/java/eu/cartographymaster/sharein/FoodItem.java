package eu.cartographymaster.sharein;

/*
* Food Item Class: Defines Food Item
* */

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

@Entity(tableName = "food_item", foreignKeys = {
        @ForeignKey(
                entity = User.class,
                childColumns = "userID",
                parentColumns = "userID",
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = User.class,
                childColumns = "bookedID",
                parentColumns = "userID",
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.SET_NULL
        )
}, indices = {
        @Index("userID"),
        @Index("bookedID")
})

public class FoodItem implements ClusterItem{

    @PrimaryKey(autoGenerate = true)
    private Integer itemID;

    @NonNull
    private Integer userID;

    private Integer bookedID;

    @NonNull
    private String title;

    @NonNull
    @ColumnInfo(name = "pickup_period")
    private String pickUpPeriod;

    @ColumnInfo(name = "descript")
    private String description;

    @NonNull
    @ColumnInfo(name = "lat")
    private double latitude;

    @NonNull
    @ColumnInfo(name = "long")
    private double longitude;

    @NonNull
    private String address;

    @NonNull
    @ColumnInfo(defaultValue = "13")
    private Integer catID;

    @NonNull
    @ColumnInfo(name = "is_avl", defaultValue = "1")
    private boolean isAvailable;

    @NonNull
    @ColumnInfo(name = "is_bkd", defaultValue = "0")
    private boolean isBooked;


    // constructor
    public FoodItem(Integer userID, Integer bookedID, @NonNull String title, @NonNull String pickUpPeriod, String description, double latitude, double longitude, @NonNull String address, Integer catID, boolean isAvailable, boolean isBooked) {
        this.userID = userID;
        this.bookedID = bookedID;
        this.title = title;
        this.pickUpPeriod = pickUpPeriod;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.catID = catID;
        this.isAvailable = isAvailable;
        this.isBooked = isBooked;
    }

    // alternative constructor with the default values
    @Ignore
    public FoodItem(@NonNull Integer userID, @NonNull String title, @NonNull String pickUpPeriod, String description, double latitude, double longitude, @NonNull String address, @NonNull Integer catID) {
        this.userID = userID;
        this.bookedID = null;
        this.title = title;
        this.pickUpPeriod = pickUpPeriod;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.catID = catID;
        this.isAvailable = true;
        this.isBooked = false;
    }

    //  setter for the primary key
    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    // getters
    public Integer getItemID() {
        return itemID;
    }

    public Integer getUserID() {
        return userID;
    }

    public Integer getBookedID() {
        return bookedID;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return new LatLng(getLatitude(),getLongitude());
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return description;
    }

    @NonNull
    public String getPickUpPeriod() {
        return pickUpPeriod;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public Integer getCatID() {
        return catID;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean isBooked() {
        return isBooked;
    }


}
