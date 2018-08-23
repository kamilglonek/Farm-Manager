package com.kamilglonek.farmmanager.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kamilglonek.farmmanager.Downloaders.PersonalListDownloader;
import com.kamilglonek.farmmanager.R;
import com.kamilglonek.farmmanager.Structures.ListItem;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.ArrayList;

public class PersonalList extends AppCompatActivity {

    ListView personalList;
    FloatingActionButton addTaskButton;
    Button bAddTask;
    public ArrayList<ListItem> list = new ArrayList<>();
    Boolean isListCreated = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_list);

        personalList = (ListView) findViewById(R.id.personalList);

        ///// PersonalListDownloader
        PersonalListDownloader personalListDownloader = new PersonalListDownloader();
        String objectID = null;
        try {
            ParseQuery<ParseObject> objectIDQuery = ParseQuery.getQuery("personalList").whereMatches("owner", ParseUser.getCurrentUser().getUsername().toString());
            objectID = objectIDQuery.getFirst().getObjectId().toString();
            isListCreated = true;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (objectID != null){
            try {
                list = personalListDownloader.loadList(objectID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        MyListAdapter listAdapter = new MyListAdapter();
        personalList.setAdapter(listAdapter);

        // adding task to list with add task dialog
        addTaskButton = (FloatingActionButton) findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PersonalList.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_item, null);
                final EditText etTaskName = (EditText) mView.findViewById(R.id.etTaskName);
                final EditText etDayNumber = (EditText) mView.findViewById(R.id.etDayNumber);
                bAddTask = (Button) mView.findViewById(R.id.bAddTask);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                bAddTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etTaskName != null && etDayNumber != null) {
                            ListItem newListItem = new ListItem(etTaskName.getText().toString(), etDayNumber.getText().toString());
                            list.add(newListItem);
                            listAdapter.notifyDataSetChanged();
                            if(!isListCreated){
                                uploadListFirstTime();
                                isListCreated = true;
                            }
                            else {
                                try {
                                    uploadList();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    public void uploadListFirstTime() {
        ParseObject parseObject = new ParseObject("personalList");
        parseObject.put("owner", ParseUser.getCurrentUser().getUsername().toString());
        Gson gson = new Gson();
        String json = gson.toJson(list);
        parseObject.put("list", json);
        parseObject.saveInBackground();
    }

    public void uploadList() throws ParseException {
        String objectID;
        ParseQuery<ParseObject> objectIDQuery = ParseQuery.getQuery("personalList").whereMatches("owner", ParseUser.getCurrentUser().getUsername().toString());
        objectID = objectIDQuery.getFirst().getObjectId().toString();
        ParseObject parseObject = objectIDQuery.get(objectID);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        parseObject.put("list", json);
        parseObject.saveInBackground();

    }

    class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
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
            convertView = getLayoutInflater().inflate(R.layout.listview, null);
            TextView task = (TextView) convertView.findViewById(R.id.tvTask);
            TextView dayNumber = (TextView) convertView.findViewById(R.id.tvDay);

            task.setText("Task: " + list.get(position).taskName.toString());
            dayNumber.setText("Day: "+list.get(position).dayNumber.toString());
            return convertView;
        }
    }
}
