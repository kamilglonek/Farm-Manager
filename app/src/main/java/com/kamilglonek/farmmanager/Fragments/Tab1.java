package com.kamilglonek.farmmanager.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.kamilglonek.farmmanager.Downloaders.SowListDownloader;
import com.kamilglonek.farmmanager.Modules.Family;
import com.kamilglonek.farmmanager.Modules.Litter;
import com.kamilglonek.farmmanager.Modules.Sow;
import com.kamilglonek.farmmanager.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab1 extends Fragment {

    public String title;
    public String tabID;
    ArrayList<Sow> sows = new ArrayList<>();
    ArrayList<Litter> litters = new ArrayList<>();
    ListView sowList;
    Boolean isSowListCreated = false;
    MySowListAdapter mySowListAdapter = null;
    ArrayList<Family> families = new ArrayList<>();

    public Tab1() {
        // Empty constructor required
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment newInstance(String title, String tabID) {
        Fragment fragment = new Tab1();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("tabID", tabID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SowListDownloader sowListDownloader = new SowListDownloader();
        String objectID = null;
        try {
            ParseQuery<ParseObject> objectIDQuery = ParseQuery.getQuery("sowList").whereMatches("owner", ParseUser.getCurrentUser().getUsername().toString());
            objectID = objectIDQuery.getFirst().getObjectId().toString();
            isSowListCreated = true;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(objectID != null) {
            try {
                sows = sowListDownloader.loadSowList(objectID);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ///// finding litters that belong to sows

        LitterListDownloader litterListDownloader = new LitterListDownloader();
        String objectID_2 = null;
        try {
            ParseQuery<ParseObject> objectIDQuery = ParseQuery.getQuery("litterList").whereMatches("owner", ParseUser.getCurrentUser().getUsername().toString());
            objectID_2 = objectIDQuery.getFirst().getObjectId().toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(objectID_2 != null) {
            try {
                litters = litterListDownloader.loadLitterList(objectID_2);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < sows.size(); i++) {
            Family family = new Family(sows.get(i));
            for (int j = 0; j < litters.size(); j++) {
                if (litters.get(j).parentID.equals(sows.get(i).sowID)){
                    family.litters.add(litters.get(j));
                }
            }

            //add family to array list
            families.add(family);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        sowList = (ListView) view.findViewById(R.id.sowList);


        // Inflate the layout for this fragment
        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        mySowListAdapter = new MySowListAdapter();
        sowList.setAdapter(mySowListAdapter);

        sowList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                            uploadSowList(null, null, true, position);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        mySowListAdapter.notifyDataSetChanged();
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

    class MySowListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return families.size();
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
            convertView = getLayoutInflater().inflate(R.layout.sow_listview, null);
            TextView animalID = (TextView) convertView.findViewById(R.id.tvSowID);
            TextView sowID = (TextView) convertView.findViewById(R.id.tvAnimalID);


            animalID.setText("Mother Nr: "+families.get(position).sow.sowID.toString());
            sowID.setText("Litters: "+families.get(position).litters.size());
            return convertView;
        }
    }

    public void checkoutList(){
        isSowListCreated = false;

        String objectIDTest = null;
        try {
            ParseQuery<ParseObject> objectIDQuery = ParseQuery.getQuery("sowList").whereMatches("owner", ParseUser.getCurrentUser().getUsername().toString());
            objectIDTest = objectIDQuery.getFirst().getObjectId().toString();
            isSowListCreated = true;
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void uploadSowList(String sowID, String info, boolean ifDelete, int position) throws ParseException {

        if (sowID != null) {
            sows.add(new Sow(sowID));
        }

        if (ifDelete) {
            sows.remove(position);
        }

        checkoutList();

        if(!isSowListCreated) {
            // create sowList for the first time
            ParseObject parseObjcet = new ParseObject("sowList");
            parseObjcet.put("owner", ParseUser.getCurrentUser().getUsername().toString());
            Gson gson = new Gson();
            String json = gson.toJson(sows);
            parseObjcet.put("sows", json);
            parseObjcet.saveInBackground();
            //System.out.println("=-=------------------1-1-1-");
        }
        else {
            //update already existing list
            String objectID;
            ParseQuery<ParseObject> objectIDQuery = ParseQuery.getQuery("sowList").whereMatches("owner", ParseUser.getCurrentUser().getUsername().toString());
            objectID = objectIDQuery.getFirst().getObjectId().toString();
            ParseObject parseObject = objectIDQuery.get(objectID);
            Gson gson = new Gson();
            String json = gson.toJson(sows);
            parseObject.put("sows", json);
            parseObject.saveInBackground();
        }
        mySowListAdapter.notifyDataSetChanged();
    }
}
