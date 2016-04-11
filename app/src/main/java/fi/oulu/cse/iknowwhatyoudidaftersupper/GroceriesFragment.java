package fi.oulu.cse.iknowwhatyoudidaftersupper;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroceriesFragment extends Fragment {

    private ArrayList<String> groceryList;
    private ArrayAdapter<String> groceryAdapter;
    private EditText GroceryInput;

    public GroceriesFragment() {
        // Required empty public constructor
    }

    public static GroceriesFragment newInstance() {
        return new GroceriesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groceries, container, false);
        ListView listView=(ListView)view.findViewById(R.id.GroceryList);
        String[] items = {};
        groceryList = new ArrayList<>(Arrays.asList(items));
        groceryAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.txtitem, groceryList);
        listView.setAdapter(groceryAdapter);
        GroceryInput = (EditText)view.findViewById(R.id.GroceryInput);
        Button AddBtn = (Button)view.findViewById(R.id.GroceryAddBtn);
        AddBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newItem = GroceryInput.getText().toString();
                groceryList.add(newItem);
                groceryAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

}
