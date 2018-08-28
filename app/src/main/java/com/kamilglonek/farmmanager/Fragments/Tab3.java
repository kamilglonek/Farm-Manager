package com.kamilglonek.farmmanager.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kamilglonek.farmmanager.Downloaders.LitterListDownloader;
import com.kamilglonek.farmmanager.Downloaders.PersonalListDownloader;
import com.kamilglonek.farmmanager.Modules.Litter;
import com.kamilglonek.farmmanager.Modules.Task;
import com.kamilglonek.farmmanager.Modules.ToDo;
import com.kamilglonek.farmmanager.R;
import com.kamilglonek.farmmanager.Structures.ListItem;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab3 extends Fragment {

    public String title;
    public String tabID;
    ArrayList<Litter> litters = new ArrayList<>();
    ArrayList<ToDo> tasksToDo = new ArrayList<>();
    ListView lvTask;
    MyTaskListAdapter myTaskListAdapter = null;
    LitterListDownloader litterListDownloader = null;
    ArrayList<ListItem> personalList = new ArrayList<>();
    ArrayList<Task> tasksCalculated = new ArrayList<>();

    public Tab3() {
        // Empty constructor required
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment newInstance(String title, String tabID) {
        Fragment fragment = new Tab3();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("tabID", tabID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LitterListDownloader litterListDownloader = new LitterListDownloader();
        String objectID = null;
        try {
            ParseQuery<ParseObject> objectIDQuery = ParseQuery.getQuery("litterList").whereMatches("owner", ParseUser.getCurrentUser().getUsername().toString());
            objectID = objectIDQuery.getFirst().getObjectId().toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(objectID != null) {
            try {
                litters = litterListDownloader.loadLitterList(objectID);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ///// PersonalListDownloader
        PersonalListDownloader personalListDownloader = new PersonalListDownloader();
        String objectID_2 = null;
        try {
            ParseQuery<ParseObject> objectIDQuery = ParseQuery.getQuery("personalList").whereMatches("owner", ParseUser.getCurrentUser().getUsername().toString());
            objectID_2 = objectIDQuery.getFirst().getObjectId().toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (objectID_2 != null){
            try {
                personalList = personalListDownloader.loadList(objectID_2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        Fragment tab2 = (Tab2) getActivity().getSupportFragmentManager().getFragments().get(2);
//        Tab2 frag = (Tab2) tab2;
//        tasksToDo = frag.toDoTasks;
        //tasksCalculated.add(new Task(tasksToDo.get(i).taskDate,tasksToDo.get(i).taskName,"1"));

        ArrayList<Integer> daysLeft = new ArrayList<>();

        for(int i = 0; i < litters.size(); i++) {
            Litter litter = litters.get(i); //current litter
                for(int j = 0; j < personalList.size(); j++) {
                    ListItem listItem = personalList.get(j);
                    String birthdate = litter.birthdate;
                    String myFormat = "dd/MM/yy";

                    DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yy", Locale.UK);
                    Date date = null;
                    try {
                        date = sourceFormat.parse(birthdate);
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.DATE, Integer.valueOf(listItem.dayNumber));

                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                    String taskDate = sdf.format(cal.getTime());
                    tasksToDo.add(new ToDo(listItem.taskName, taskDate, litter.parentID));
                    Calendar today = Calendar.getInstance();

                    long diff = cal.getTimeInMillis() - today.getTimeInMillis();
                    int days = (int) (diff/(24*60*60*1000))+1;
                    tasksCalculated.add(new Task(new ToDo(listItem.taskName, taskDate, litter.parentID), days));
                }
        }
        Collections.sort(tasksCalculated, new daysComparator());
    }

    public class daysComparator implements Comparator<Task> {

        @Override
        public int compare(Task task1, Task task2) {
            if(task1.daysLeft >= task2.daysLeft) return 1;
            else return -1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab3, container, false);  // Inflate the layout for this fragment
        lvTask = (ListView) view.findViewById(R.id.lvTask);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        myTaskListAdapter = new MyTaskListAdapter();
        lvTask.setAdapter(myTaskListAdapter);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    class MyTaskListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return tasksCalculated.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.to_do, null);
            TextView tvDateToDo = (TextView) convertView.findViewById(R.id.tvDateToDo);
            TextView tvDaysLeft = (TextView) convertView.findViewById(R.id.tvDaysLeft);
            TextView tvTaskToDo = (TextView) convertView.findViewById(R.id.tvTaskToDo);
            TextView tvLitterToDo = (TextView) convertView.findViewById(R.id.tvLitterToDo);

            String daysLeft = Integer.toString(tasksCalculated.get(position).daysLeft);
            tvDateToDo.setText(tasksCalculated.get(position).toDo.taskDate);
            tvDaysLeft.setText(daysLeft);
            tvTaskToDo.setText(tasksCalculated.get(position).toDo.taskName);
            tvLitterToDo.setText(tasksCalculated.get(position).toDo.parentID);
            return convertView;
        }
    }
}
