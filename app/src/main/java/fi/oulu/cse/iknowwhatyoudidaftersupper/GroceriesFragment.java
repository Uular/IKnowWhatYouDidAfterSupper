package fi.oulu.cse.iknowwhatyoudidaftersupper;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        final DBHelper db = new DBHelper(getActivity());
        View view = inflater.inflate(R.layout.fragment_groceries, container, false);
        ListView listView=(ListView)view.findViewById(R.id.GroceryList);
        groceryList = db.getAllGroceries();
        groceryAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.txtitem, groceryList);
        listView.setAdapter(groceryAdapter);
        GroceryInput = (EditText)view.findViewById(R.id.GroceryInput);
        Button AddBtn = (Button)view.findViewById(R.id.GroceryAddBtn);
        AddBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newItem = GroceryInput.getText().toString();
                groceryList.add(newItem);
                db.insertGroceries(newItem);
                groceryAdapter.notifyDataSetChanged();
                GroceryInput.setText("");
                GroceryInput.setHint("Input new grocery...");
            }
        });
        Button ClearBtn = (Button)view.findViewById(R.id.GroceryClrBtn);
        ClearBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                groceryList.clear();
                db.deleteAllGrocery();
                //bundle.putInt("notEmpty", groceryList.size());
                groceryAdapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                //To change body of implemented methods use File | Settings | File Templates.
                Log.d("DEBUG", "onItemClick:");
                String s = groceryAdapter.getItem(i);
                groceryAdapter.remove(s);
                db.deleteGrocery(s);
                //bundle.putInt("notEmpty", groceryList.size());
                groceryAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

}
