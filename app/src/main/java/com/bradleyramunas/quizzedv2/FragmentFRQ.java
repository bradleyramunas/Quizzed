package com.bradleyramunas.quizzedv2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFRQ extends Fragment implements QuestionType{

    private String questionText;
    private String answerText;

    private TextView questionT;
    private EditText answerT;

    public FragmentFRQ() {
        // Required empty public constructor
    }

    public static FragmentFRQ newInstance(String questionText, String answerText) {
        FragmentFRQ fragment = new FragmentFRQ();
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
        View view = inflater.inflate(R.layout.fragment_fragment_frq, container, false);

        questionT = (TextView) view.findViewById(R.id.questionText);
        answerT   = (EditText) view.findViewById(R.id.userAnswer);

        questionT.setText(questionText);

        return view;
    }

    @Override
    public String getQuestionText() {
        return questionText;
    }

    @Override
    public String getAnswerText() {
        return answerText;
    }

    public boolean checkAnswer(){
        return answerT.getText().toString().equals(answerText);
    }
}
