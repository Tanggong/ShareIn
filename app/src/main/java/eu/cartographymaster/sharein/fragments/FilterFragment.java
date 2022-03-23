package eu.cartographymaster.sharein.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.cartographymaster.sharein.FoodItem;
import eu.cartographymaster.sharein.R;
import eu.cartographymaster.sharein.TakeActivity;

/**
 * fragment representing the filter for items based on food categories
 */
public class FilterFragment extends DialogFragment {

    // to save the ID of selected items
    private List<Integer> selectedID = new ArrayList<>();

    private ItemViewModel viewModel;

    @NonNull
    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        selectedID.clear();

        String[] cats = getActivity().getResources().getStringArray(R.array.categories);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //get view model
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // here a list of checkboxes are use to filter the displayed result
        // while check the options, the selected Item will be saved in a list as selected categories Id
        builder.setMultiChoiceItems(cats, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {

                // add the checked items to selected ID list
                if (isChecked) {
                    selectedID.add(which);
                } else if (selectedID.contains(cats[which])) {
                    selectedID.remove(which);
                }

            }
        });

        builder.setTitle("Category:")
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        int numSelected = selectedID.size();
                        Toast.makeText(getActivity(), numSelected + " categories are selected", Toast.LENGTH_SHORT).show();
                        // save selected Item ID in Livedata
                        viewModel.selectItem(selectedID);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                });

        return builder.create();
    }
}