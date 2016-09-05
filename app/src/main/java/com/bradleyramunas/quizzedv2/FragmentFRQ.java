package com.bradleyramunas.quizzedv2;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private ImageView checkResult;

    private LinearLayout cardLayout;

    private CardView questionCardView;

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

    public static FragmentFRQ newInstance(QuestionFRQ questionFRQ){
        FragmentFRQ fragment = new FragmentFRQ();
        Bundle args = new Bundle();
        args.putString("questionText", questionFRQ.get_questionText());
        args.putString("answerText", questionFRQ.get_answerText());
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
        questionCardView = (CardView) view.findViewById(R.id.questionCardView);
        cardLayout = (LinearLayout) view.findViewById(R.id.cardLayout);
        checkResult = (ImageView) view.findViewById(R.id.checkResult);

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
        boolean check = answerT.getText().toString().equals(answerText);
        answerT.setClickable(false);
        answerT.setFocusable(false);
        TextView textView = new TextView(cardLayout.getContext());
        textView.setText("Correct answer: " + answerText);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(16);
        //textView.setTypeface(null, Typeface.BOLD);
        cardLayout.addView(textView, 1);

        if(check){
            //questionCardView.setBackgroundColor(Color.parseColor("#43A047"));
            checkResult.setImageResource(R.drawable.ic_done_black_24dp);
        }
        else{
           //questionCardView.setBackgroundColor(Color.parseColor("#E53935"));
            checkResult.setImageResource(R.drawable.ic_clear_black_24dp);
        }
        return check;
    }
}
