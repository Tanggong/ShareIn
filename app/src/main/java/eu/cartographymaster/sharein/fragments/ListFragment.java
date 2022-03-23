package eu.cartographymaster.sharein.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.cartographymaster.sharein.Confirmation;
import eu.cartographymaster.sharein.MainActivity;
import eu.cartographymaster.sharein.R;
import eu.cartographymaster.sharein.FoodItem;
import eu.cartographymaster.sharein.TakeActivity;

/**
 * fragment representing the list of Items.
 */
public class ListFragment extends Fragment {


    private ItemViewModel viewModel;
    List<FoodItem> selectedItem;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    private SimpleAdapter simpleAdapter;
    Button btnReserve;
    private ListView listView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);

        //get viewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // create the observer
        final androidx.lifecycle.Observer<List<FoodItem>> itemsObserver = new Observer<List<FoodItem>>() {

            // update UI when Items are changed
            @Override
            public void onChanged(List<FoodItem> selectedItems) {

                selectedItem = viewModel.getSelectedItem().getValue();

                list.clear();
                HashMap<String,String> item = null;

                // Get attributes for each food item to put into list
                for(int i=0;i<selectedItem.size();i++){
                    item = new HashMap<String,String>();
                    item.put( "line1", selectedItem.get(i).getTitle());
                    item.put( "line2",selectedItem.get(i).getDescription());
                    item.put( "line3", selectedItem.get(i).getPickUpPeriod() );
                    list.add( item );
                }

                setSimpleAdapter(list);
            }
        };

        //get liveData
        viewModel.getSelectedItem().observe(getViewLifecycleOwner(), itemsObserver);
    }

    public void setSimpleAdapter(ArrayList<HashMap<String,String>> list){

        // Set adapter to put attributes in three lines
        simpleAdapter = new SimpleAdapter(getContext(), list,
                R.layout.three_lines_item,
                new String[] { "line1","line2", "line3" },
                new int[] {R.id.line_title, R.id.line_description, R.id.line_pickup});

        //Link the Adapter to the list
        listView.setAdapter(simpleAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        listView = (ListView) view.findViewById(R.id.view_list);

        setSimpleAdapter(list);

        btnReserve = view.findViewById(R.id.btn_reserve);

        //set Reserve Button visible if item is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Show button RESERVE after clicking on the list item and the after clicking RESERVE
                btnReserve.setEnabled(true);
                btnReserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String foodItemId = selectedItem.get(position).getItemID().toString();
                        Intent intent = new Intent(getActivity(), Confirmation.class);
                        intent.putExtra(getActivity().getPackageName(), foodItemId);
                        startActivity(intent);
                    }
                });
            }
        });

        return view;
    }


}
