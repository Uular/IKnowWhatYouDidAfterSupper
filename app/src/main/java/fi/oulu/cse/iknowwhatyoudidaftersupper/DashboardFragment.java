package fi.oulu.cse.iknowwhatyoudidaftersupper;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private OnFragmentInteractionListener mListener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    private String getNextDinner() {
        final DBHelper db = new DBHelper(getActivity());
        ArrayList<String> mealList;
        mealList = db.getAllMealTimes();
        Collections.sort(mealList);
        int nextDinner = Integer.parseInt(mealList.get(0));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String time = sdf.format(new Time(nextDinner*1000L));

        return time;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ArrayList<String> dashboardList;
    private ArrayAdapter<String> dashboardAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO: update dinnertime to TextView, from calender or from the database

        final DBHelper db = new DBHelper(getActivity());

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ListView listView=(ListView)view.findViewById(R.id.DashboardList);
        dashboardList = db.getAllMyTasks();
        dashboardAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.txtitem, dashboardList);
        listView.setAdapter(dashboardAdapter);
        final TextView mealText = (TextView)view.findViewById(R.id.dinnerTime);
        mealText.setText(getNextDinner());
        ImageButton AddBtn = (ImageButton)view.findViewById(R.id.newMealBtm);
        AddBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getActivity() != null) {
                    DialogFragment newFragment = new MealDialog();
                    newFragment.show(getActivity().getFragmentManager(), "MealTimePicker");
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
