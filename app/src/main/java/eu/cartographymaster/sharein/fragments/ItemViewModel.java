package eu.cartographymaster.sharein.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import eu.cartographymaster.sharein.FoodItem;
import eu.cartographymaster.sharein.MainActivity;
import eu.cartographymaster.sharein.TakeActivity;

public class ItemViewModel extends ViewModel {

    // create a livedata List of all available food items
    private final MutableLiveData<List<FoodItem>> selectedItem = new MutableLiveData<>(TakeActivity.foodItem);

    private List<FoodItem> itemList;


    // get selected Item from Database
    public void selectItem(List<Integer> selectedID) {
        itemList = MainActivity.foodItemDao.getSelectedFoodItems(selectedID);
        selectedItem.setValue(itemList);
    }

    public MutableLiveData<List<FoodItem>> getSelectedItem() {
        return selectedItem;
    }

}
