package com.bradleyramunas.quizzedv2;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FragmentMCQ extends Fragment implements QuestionType{

    private String questionText;
    private String answerText;
    private String optionOne;
    private String optionTwo;
    private String optionThree;
    private String optionFour;

    private TextView questionT;
    private RadioButton op1;
    private RadioButton op2;
    private RadioButton op3;
    private RadioButton op4;
    private RadioGroup group;

    private ImageView checkResult;

    private CardView questionCardView;

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

        questionT = (TextView) view.findViewById(R.id.questionText);
        group = (RadioGroup) view.findViewById(R.id.group);
        op1 = (RadioButton) view.findViewById(R.id.optionOne);
        op2 = (RadioButton) view.findViewById(R.id.optionTwo);
        op3 = (RadioButton) view.findViewById(R.id.optionThree);
        op4 = (RadioButton) view.findViewById(R.id.optionFour);
        questionCardView = (CardView) view.findViewById(R.id.questionCardView);

        checkResult = (ImageView) view.findViewById(R.id.checkResult);

        questionT.setText(questionText);
        op1.setText(optionOne);
        op2.setText(optionTwo);
        op3.setText(optionThree);
        op4.setText(optionFour);

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
        boolean check;
        try {
             check = ((RadioButton)group.findViewById(group.getCheckedRadioButtonId())).getText().toString().equals(answerText);
            Log.e("WTF", ((RadioButton)group.findViewById(group.getCheckedRadioButtonId())).getText().toString());
            Log.e("WTF", answerText);

            if(check){
                //questionCardView.setBackgroundColor(Color.parseColor("#43A047"));
                checkResult.setImageResource(R.drawable.ic_done_black_24dp);
            }else{
                //questionCardView.setBackgroundColor(Color.parseColor("#E53935"));
                checkResult.setImageResource(R.drawable.ic_clear_20dp);
            }

            return check;

        } catch (NullPointerException e){
            //questionCardView.setBackgroundColor(Color.parseColor("#E53935"));
            checkResult.setImageResource(R.drawable.ic_clear_20dp);

        } finally {
        }

        return false;
    }

}
