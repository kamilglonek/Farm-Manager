package com.kamilglonek.farmmanager.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kamilglonek.farmmanager.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab4.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab4 extends Fragment {

    public String title;
    public String tabID;

    public Tab4() {
        // Empty constructor required
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab4, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment newInstance(String title, String tabID) {
        Fragment fragment = new Tab4();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("tabID", tabID);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
