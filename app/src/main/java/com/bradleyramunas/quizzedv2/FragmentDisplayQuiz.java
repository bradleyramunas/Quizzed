package com.bradleyramunas.quizzedv2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentDisplayQuiz extends Fragment {


    private TextView quizTitle, questionAmount, quizLastTaken, methodOneTitle, methodOneDescription, methodOneLastTaken;
    private String qtText, qaText, qltText;

    public FragmentDisplayQuiz() {
        // Required empty public constructor
    }


    public static FragmentDisplayQuiz createInstance(String quizTitleText, int questionAmountText, String quizLastTakenText){
        FragmentDisplayQuiz fragment = new FragmentDisplayQuiz();
        Bundle args = new Bundle();
        args.putString("quizTitle", quizTitleText);
        args.putInt("questionAmount", questionAmountText);
        args.putString("quizLastTaken", quizLastTakenText);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_display_quiz, container, false);

        quizTitle               = (TextView) view.findViewById(R.id.quizTitle);
        questionAmount          = (TextView) view.findViewById(R.id.questionAmount);
        quizLastTaken           = (TextView) view.findViewById(R.id.quizLastTaken);
        methodOneTitle          = (TextView) view.findViewById(R.id.methodOneTitle);
        methodOneDescription    = (TextView) view.findViewById(R.id.methodOneDescription);
        methodOneLastTaken      = (TextView) view.findViewById(R.id.methodOneLastTaken);

        quizTitle.setText(getArguments().getString("quizTitle"));
        questionAmount.setText(getArguments().getInt("questionAmount") + " Questions");
        quizLastTaken.setText(getArguments().getString("quizLastTaken"));

        methodOneTitle.setText("Quiz");
        methodOneDescription.setText("Take a traditional quiz");
        //TODO: Setup sharedprefs and insert date strings of last taken...
        //remember to setup sharedprefs on creation of each quiz and deletion after deleting
        //methodOneLastTaken.setText(this.getActivity().getSharedPreferences());


        return view;
    }

}
