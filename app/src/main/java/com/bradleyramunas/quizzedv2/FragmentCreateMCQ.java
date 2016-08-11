package com.bradleyramunas.quizzedv2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCreateMCQ extends Fragment {


    //TODO: Fix this, get option texts to return from EditTexts


    private String questionText;
    private String answerText;
    private String optionOne;
    private String optionTwo;
    private String optionThree;
    private String optionFour;
    private RadioGroup radioGroup;
    private LinearLayout optionHolder;


    public FragmentCreateMCQ() {
        // Required empty public constructor
    }


    public static FragmentCreateMCQ newCreationInstance(){
        FragmentCreateMCQ fragment = new FragmentCreateMCQ();
        Bundle args = new Bundle();
        args.putString("questionText", "Question");
        args.putString("answerText", "");
        args.putString("optionOne", "Answer 1");
        args.putString("optionTwo", "Answer 2");
        args.putString("optionThree", "Answer 3");
        args.putString("optionFour", "Answer 4");
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
        View view = inflater.inflate(R.layout.fragment_fragment_create_mcq, container, false);

        EditText questionTextET = (EditText) view.findViewById(R.id.questionText);
        questionTextET.setHint(questionText);
        EditText optionOneET = (EditText) view.findViewById(R.id.optionOneText);
        optionOneET.setHint(optionOne);
        EditText optionTwoET = (EditText) view.findViewById(R.id.optionTwoText);
        optionTwoET.setHint(optionTwo);
        EditText optionThreeET = (EditText) view.findViewById(R.id.optionThreeText);
        optionThreeET.setHint(optionThree);
        EditText optionFourET = (EditText) view.findViewById(R.id.optionFourText);
        optionFourET.setHint(optionFour);

        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        optionHolder = (LinearLayout) view.findViewById(R.id.optionHolder);

        return view;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getAnswerText() {
        return answerText;
    }

    public String getOptionOne() {
        return optionOne;
    }

    public String getOptionTwo() {
        return optionTwo;
    }

    public String getOptionThree() {
        return optionThree;
    }

    public String getOptionFour() {
        return optionFour;
    }

    public String getSelectedOption(){
        int loc = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));
        EditText et = (EditText) optionHolder.getChildAt(loc);
        return et.getText().toString();
    }
}
