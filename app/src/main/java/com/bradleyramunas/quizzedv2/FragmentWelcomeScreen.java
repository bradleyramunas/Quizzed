package com.bradleyramunas.quizzedv2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bradleyramunas.quizzedv2.R;

public class FragmentWelcomeScreen extends Fragment {


    public FragmentWelcomeScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_welcome_screen, container, false);
    }

}
