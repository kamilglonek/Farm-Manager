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

import com.kamilglonek.farmmanager.R;
import com.kamilglonek.farmmanager.Structures.ListItem;

import java.util.ArrayList;

public class PersonalList extends AppCompatActivity {

    ListView personalList;
    //private ArrayAdapter<String> adapter;
    FloatingActionButton addTaskButton;
    Button bAddTask;
    ArrayList<ListItem> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_list);

        personalList = (ListView) findViewById(R.id.personalList);

        list.add(new ListItem("zastrzyk", "3"));
        list.add(new ListItem("miszenie", "34"));

        MyListAdapter listAdapter = new MyListAdapter();
        personalList.setAdapter(listAdapter);

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
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

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

            task.setText(list.get(position).taskName);
            dayNumber.setText(list.get(position).dayNumber);
            return convertView;
        }
    }
}
