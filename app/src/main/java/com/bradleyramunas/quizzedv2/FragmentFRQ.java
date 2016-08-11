package com.bradleyramunas.quizzedv2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFRQ extends Fragment {

    private String questionText;
    private String answerText;

    public FragmentFRQ() {
        // Required empty public constructor
    }

    public static FragmentMCQ newInstance(String questionText, String answerText) {
        FragmentMCQ fragment = new FragmentMCQ();
        Bundle args = new Bundle();
        args.putString("questionText", questionText);
        args.putString("answerText", answerText);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionText = getArguments().getString("questionText");
            answerText = getArguments().getString("answerText");

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_frq, container, false);
    }

}
