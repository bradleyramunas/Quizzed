package com.bradleyramunas.quizzedv2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class FragmentCreateFRQ extends Fragment {


    private String questionText;
    private String answerText;

    EditText q;
    EditText a;

    public FragmentCreateFRQ() {
        // Required empty public constructor
    }


    public static FragmentCreateFRQ newInstance() {
        FragmentCreateFRQ fragment = new FragmentCreateFRQ();
        Bundle args = new Bundle();
        args.putString("questionText", "Question");
        args.putString("answerText", "Answer");
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
        View view = inflater.inflate(R.layout.fragment_fragment_create_frq, container, false);

        q = (EditText) view.findViewById(R.id.questionText);
        a = (EditText) view.findViewById(R.id.answerText);
        q.setHint(questionText);
        a.setHint(answerText);

        return view;
    }

    public String getQuestionText(){
        return q.getText().toString();
    }

    public String getAnswerText(){
        return a.getText().toString();
    }


    //used to check if question has an answer
    public boolean isProper(){
        if(getQuestionText().equals("") || getAnswerText().equals("")){
            return false;
        }
        return true;
    }

}
