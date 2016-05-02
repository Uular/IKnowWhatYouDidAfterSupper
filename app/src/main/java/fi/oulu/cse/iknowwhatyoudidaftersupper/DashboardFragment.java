package fi.oulu.cse.iknowwhatyoudidaftersupper;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


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
        if (!mealList.isEmpty()) {
            Log.d("DashDFrag", mealList.toString());
            long nextDinner = Long.parseLong(mealList.get(0));
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(nextDinner);
            String time = String.format("%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
            return time;
        } else {
            return "N/A";
        }

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

        EditText msg = (EditText) view.findViewById(R.id.ChatInput);
        EditText nick = (EditText) view.findViewById(R.id.ChatNick);
        msg.setOnFocusChangeListener(mHideListViewOnFocusListener);
        nick.setOnFocusChangeListener(mHideListViewOnFocusListener);


        Button chatButton = (Button) view.findViewById(R.id.ChatBtn);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View vw = getView();
                if (vw == null) return;

                EditText msg = (EditText) vw.findViewById(R.id.ChatInput);
                EditText nick = (EditText) vw.findViewById(R.id.ChatNick);

                DBHelper db = new DBHelper(getActivity());
                if (nick.length() > 0 && msg.length() > 0) {
                    db.insertMessage(msg.getText().toString().trim(), nick.getText().toString().trim());
                    msg.setText("");
                    updateChat(getView());
                }
                msg.clearFocus();
                nick.clearFocus();
            }
        });


        updateChat(view);

        return view;
    }

    View.OnFocusChangeListener mHideListViewOnFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            View view = getView();
            if (view == null) return;
            ListView listView=(ListView)view.findViewById(R.id.DashboardList);
            if (hasFocus) {
                listView.setVisibility(View.GONE);
            } else {
                listView.setVisibility(View.VISIBLE);
            }
        }
    };

    public void updateChat(View view) {
        if (view == null) return;

        DBHelper db = new DBHelper(getActivity());

        List<String> msgs = db.getMessages();
        StringBuilder chat = new StringBuilder();
        for (String msg: msgs) {
            chat.append(msg);
        }

        TextView chatView = (TextView) view.findViewById(R.id.ChatList);
        chatView.setMovementMethod(new ScrollingMovementMethod());
        chatView.setText(chat.toString());
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
