package com.bradleyramunas.quizzedv2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMCQ extends Fragment {

    private String questionText;
    private String answerText;
    private String optionOne;
    private String optionTwo;
    private String optionThree;
    private String optionFour;

    public FragmentMCQ() {
        // Required empty public constructor
    }

    public static FragmentMCQ newInstance(String questionText, String answerText, String optionOne, String optionTwo, String optionThree, String optionFour) {
        FragmentMCQ fragment = new FragmentMCQ();
        Bundle args = new Bundle();
        args.putString("questionText", questionText);
        args.putString("answerText", answerText);
        args.putString("optionOne", optionOne);
        args.putString("optionTwo", optionTwo);
        args.putString("optionThree", optionThree);
        args.putString("optionFour", optionFour);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionText = getArguments().getString("questionText");
            answerText = getArguments().getString("answerText");
            optionOne = getArguments().getString("optionOne");
            optionTwo = getArguments().getString("optionTwo");
            optionThree = getArguments().getString("optionThree");
            optionFour = getArguments().getString("optionFour");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_mcq, container, false);


        return view;
    }

}
