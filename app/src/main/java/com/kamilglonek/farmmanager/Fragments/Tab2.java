package com.kamilglonek.farmmanager.Fragments;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kamilglonek.farmmanager.Downloaders.LitterListDownloader;
import com.kamilglonek.farmmanager.Downloaders.PersonalListDownloader;
import com.kamilglonek.farmmanager.Modules.Litter;
import com.kamilglonek.farmmanager.Modules.ToDo;
import com.kamilglonek.farmmanager.R;
import com.kamilglonek.farmmanager.Structures.ListItem;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab2 extends Fragment {

    public String title;
    public String tabID;
    ArrayList<Litter> litters = new ArrayList<Litter>();
    ListView litterList;
    Boolean isLitterListCreated = false;
    MyLitterListAdapter myLitterListAdapter = null;
    MyToDoListAdapter myToDoListAdapter = null;
    ArrayList<ListItem> toDoList = new ArrayList<>();
    ArrayList<ToDo> toDoTasks = new ArrayList<>();

    public Tab2() {
        // Empty constructor required
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LitterListDownloader litterListDownloader = new LitterListDownloader();
        String objectID = null;
        try {
            ParseQuery<ParseObject> objectIDQuery = ParseQuery.getQuery("litterList").whereMatches("owner", ParseUser.getCurrentUser().getUsername().toString());
            objectID = objectIDQuery.getFirst().getObjectId().toString();
            isLitterListCreated = true;
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
                toDoList = personalListDownloader.loadList(objectID_2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        litterList = (ListView) view.findViewById(R.id.litterList);

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment newInstance(String title, String tabID) {
        Fragment fragment = new Tab2();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("tabID", tabID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        myLitterListAdapter = new MyLitterListAdapter();
        litterList.setAdapter(myLitterListAdapter);
        litterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.to_do_listview, null);
                TextView tvTask = (TextView) mView.findViewById(R.id.tvTaskLitter);
                TextView tvDay = (TextView) mView.findViewById(R.id.tvDayLitter);
                ListView lvToDoList = (ListView) mView.findViewById(R.id.lvToDoListLitter);
                myToDoListAdapter = new MyToDoListAdapter();
                lvToDoList.setAdapter(myToDoListAdapter);

                Litter litter = litters.get(position);

                for(int i = 0; i < toDoList.size(); i++) {
                    ListItem listItem = toDoList.get(i);
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

                    //String taskDate = String.format(myFormat, cal.getTime());
                    //String taskDate = cal.getTime().toString();
                    toDoTasks.add(new ToDo(listItem.taskName, taskDate));
                }
                myToDoListAdapter.notifyDataSetChanged();
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        litterList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.delete_dialog, null);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                Button bDelete = (Button) mView.findViewById(R.id.bDelete);
                bDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            uploadLitterList(null, null, 0, true, position);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        myLitterListAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class MyLitterListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return litters.size();
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
            convertView = getLayoutInflater().inflate(R.layout.litter_listview, null);
            TextView animalID = (TextView) convertView.findViewById(R.id.tvSowID_2);
            TextView sowID = (TextView) convertView.findViewById(R.id.tvAnimalAmount);
            TextView birthdate = (TextView) convertView.findViewById(R.id.tvBirthdate);

            animalID.setText("Litter after Mother nr: "+litters.get(position).parentID.toString());
            sowID.setText("Amount: "+litters.get(position).amount);
            birthdate.setText("Birthdate: "+ litters.get(position).birthdate);
            return convertView;
        }
    }

    class MyToDoListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return toDoList.size();
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
            convertView = getLayoutInflater().inflate(R.layout.to_do_listview_litter, null);
            TextView taskName = (TextView) convertView.findViewById(R.id.tvTaskLitter);
            TextView dayNumber = (TextView) convertView.findViewById(R.id.tvDayLitter);

            taskName.setText(toDoTasks.get(position).taskName);
            dayNumber.setText(" at: "+toDoTasks.get(position).taskDate);
            return convertView;
        }
    }

    public void checkoutList(){
        isLitterListCreated = false;

        String objectIDTest = null;
        try {
            ParseQuery<ParseObject> objectIDQuery = ParseQuery.getQuery("litterList").whereMatches("owner", ParseUser.getCurrentUser().getUsername().toString());
            objectIDTest = objectIDQuery.getFirst().getObjectId().toString();
            isLitterListCreated = true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void uploadLitterList(String parentID, String birthdate, int amount, boolean ifDelete, int positionToDelete) throws ParseException {

        if (parentID != null && birthdate != null && amount >= 0) {
            litters.add(new Litter(parentID, birthdate, amount));
        }

        if (ifDelete) {
            litters.remove(positionToDelete);
        }

        checkoutList();

        if(!isLitterListCreated) {
            // create litterList for the first time
            ParseObject parseObjcet = new ParseObject("litterList");
            parseObjcet.put("owner", ParseUser.getCurrentUser().getUsername().toString());
            Gson gson = new Gson();
            String json = gson.toJson(litters);
            parseObjcet.put("litters", json);
            parseObjcet.saveInBackground();
        }
        else {
            //update already existing list
            String objectID;
            ParseQuery<ParseObject> objectIDQuery = ParseQuery.getQuery("litterList").whereMatches("owner", ParseUser.getCurrentUser().getUsername().toString());
            objectID = objectIDQuery.getFirst().getObjectId().toString();
            ParseObject parseObject = objectIDQuery.get(objectID);
            Gson gson = new Gson();
            String json = gson.toJson(litters);
            parseObject.put("litters", json);
            parseObject.saveInBackground();
        }
        myLitterListAdapter.notifyDataSetChanged();
    }
}
