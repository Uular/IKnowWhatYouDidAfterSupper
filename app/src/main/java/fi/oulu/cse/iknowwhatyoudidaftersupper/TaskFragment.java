package fi.oulu.cse.iknowwhatyoudidaftersupper;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import fi.oulu.cse.iknowwhatyoudidaftersupper.dummy.DummyContent;
import fi.oulu.cse.iknowwhatyoudidaftersupper.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TaskFragment extends Fragment{
/*
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).

    public TaskFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TaskFragment newInstance(int columnCount) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyTaskRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}

*/

    private ArrayList<String> openTaskList;
    private ArrayList<String> myTaskList;
    private ArrayAdapter<String> taskAdapter;
    private ArrayAdapter<String> taskAdapter1;
    private EditText taskInput;
    private final String FILL_KEY = "notEmpty";


    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance() {
        return new TaskFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final DBHelper db = new DBHelper(getActivity());
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        ListView listView=(ListView)view.findViewById(R.id.openTaskList);
        ListView listView2=(ListView)view.findViewById(R.id.myTaskList);
        String[] items = {};
        String[] items2 = {};
        //openTaskList = new ArrayList<>(Arrays.asList(items));
        openTaskList = db.getAllOpenTasks();
        //If there's items on groverylist!
        //openTaskList.add("Groceries");

        //myTaskList = new ArrayList<>(Arrays.asList(items2));
        myTaskList = db.getAllMyTasks();

        taskAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.txtitem, openTaskList);
        listView.setAdapter(taskAdapter);
        taskAdapter1 = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.txtitem, myTaskList);
        listView2.setAdapter(taskAdapter1);

        if (!db.isFilled() && db.numberOfGroceryRows() > 0) {
            openTaskList.add("Groceries");
            db.insertOpenTask("Groceries");
            taskAdapter.notifyDataSetChanged();
            Log.d("DEBUG", "KAKKONEN");
        }
        listView.setClickable(true);
        listView2.setClickable(true);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                //To change body of implemented methods use File | Settings | File Templates.
                Log.d("DEBUG", "onItemClick:");
                String s = taskAdapter.getItem(i);
                taskAdapter1.add(s);
                db.insertMyTask(s);
                taskAdapter.remove(s);
                db.deleteOpenTask(s);
                taskAdapter1.notifyDataSetChanged();
                taskAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Task picked!", Toast.LENGTH_SHORT).show();
            }
        });

        listView2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                //To change body of implemented methods use File | Settings | File Templates.
                Log.d("DEBUG", "onItemClick:2");
                String s = taskAdapter1.getItem(i);
                taskAdapter1.remove(s);
                db.deleteMyTask(s);
                taskAdapter1.notifyDataSetChanged();
                taskAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Task completed!", Toast.LENGTH_SHORT).show();
            }
        });

        Button TaskAddBtn = (Button)view.findViewById(R.id.TaskAddBtn);
        taskInput = (EditText)view.findViewById(R.id.TaskInput);
        TaskAddBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newItem = taskInput.getText().toString();
                openTaskList.add(newItem);
                db.insertOpenTask(newItem);
                taskAdapter.notifyDataSetChanged();
                taskInput.setText("");
                taskInput.setHint("Input new task...");
            }
        });




        return view;
    }

}