package eu.cartographymaster.sharein;

/*
* Food Item DAO
* */

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FoodItemDao {

    //Insert a food item
    @Insert
    void insertFoodItem ( FoodItem foodItem );

    //List all available food items
    @Query("SELECT * FROM food_item  where is_bkd = 0")
    public List<FoodItem> getAvailableFoodItems();

    //Get food item by id
    @Query("SELECT * FROM food_item where itemID = (:foodItemId) ")
    public FoodItem getFoodItemById (String foodItemId);

    //Updating bookedID and isAvl by foodItem id
    @Query("UPDATE food_item SET is_bkd = 1, bookedID = (:bookedID) WHERE itemID = (:foodItemId)")
    void update(Integer foodItemId, Integer bookedID);

    //Filter available food items by category
    @Query("SELECT * FROM food_item  where is_bkd = 0 AND catID IN (:selectedID)")
    public List<FoodItem> getSelectedFoodItems(List<Integer> selectedID);

}
