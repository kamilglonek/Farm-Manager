package com.kamilglonek.farmmanager.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kamilglonek.farmmanager.Modules.Litter;
import com.kamilglonek.farmmanager.Modules.Sow;
import com.kamilglonek.farmmanager.R;

import java.util.ArrayList;
import java.util.Date;


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

    public Tab2() {
        // Empty constructor required
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

        Date date = new Date();

        litters.add(new Litter(1,1,date,10));

        Tab2.MyLitterListAdapter myLitterListAdapter = new Tab2.MyLitterListAdapter();
        litterList.setAdapter(myLitterListAdapter);
    }
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    public String title;
//    public int tabID;
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    public Tab2() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param title Parameter 1.
//     * @param tabID Parameter 2.
//     * @return A new instance of fragment Tab1.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static Tab2 newInstance(String title, int tabID) {
//        Tab2 fragment = new Tab2();
//        Bundle args = new Bundle();
//        args.putString("title", title);
//        args.putInt("tabID", tabID);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view =  inflater.inflate(R.layout.fragment_tab2, container, false);
//        TextView tvLabel = (TextView) view.findViewById(R.id.tv2);
//        tvLabel.setText(title + " " + tabID);
//        return view;
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

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


            animalID.setText("Litter after Mother nr:");
            sowID.setText("Amount");
            return convertView;
        }
    }
}
