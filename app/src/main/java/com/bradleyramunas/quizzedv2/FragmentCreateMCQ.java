package com.bradleyramunas.quizzedv2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCreateMCQ extends Fragment {




    private String questionText;
    private String answerText;
    private String optionOne;
    private String optionTwo;
    private String optionThree;
    private String optionFour;
    private RadioGroup radioGroup;
    private LinearLayout optionHolder;

    private EditText questionTextET;
    private EditText optionOneET;
    private EditText optionTwoET;
    private EditText optionThreeET;
    private EditText optionFourET;

    //if type = false => default, if type = true => custom
    private boolean creationType;




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
        args.putBoolean("isCustom", false);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentCreateMCQ newCustomInstance(String question, String answer, String op1, String op2, String op3, String op4){
        FragmentCreateMCQ fragment = new FragmentCreateMCQ();
        Bundle args = new Bundle();
        args.putString("questionText", question);
        args.putString("answerText", answer);
        args.putString("optionOne", op1);
        args.putString("optionTwo", op2);
        args.putString("optionThree", op3);
        args.putString("optionFour", op4);
        args.putBoolean("isCustom", true);
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
            creationType = getArguments().getBoolean("isCustom");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_create_mcq, container, false);

        questionTextET = (EditText) view.findViewById(R.id.questionText);
        optionOneET = (EditText) view.findViewById(R.id.optionOneText);
        optionTwoET = (EditText) view.findViewById(R.id.optionTwoText);
        optionThreeET = (EditText) view.findViewById(R.id.optionThreeText);
        optionFourET = (EditText) view.findViewById(R.id.optionFourText);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        optionHolder = (LinearLayout) view.findViewById(R.id.optionHolder);

        if(creationType){
            questionTextET.setHint("Question");
            optionOneET.setHint("Answer 1");
            optionTwoET.setHint("Answer 2");
            optionThreeET.setHint("Answer 3");
            optionFourET.setHint("Answer 4");
            questionTextET.setText(questionText);
            optionOneET.setText(optionOne);
            optionTwoET.setText(optionTwo);
            optionThreeET.setText(optionThree);
            optionFourET.setText(optionFour);
            if(answerText.equals(optionOne)){
                RadioButton rb = (RadioButton) radioGroup.getChildAt(0);
                rb.setChecked(true);
            }else if(answerText.equals(optionTwo)){
                RadioButton rb = (RadioButton) radioGroup.getChildAt(1);
                rb.setChecked(true);
            }else if(answerText.equals(optionThree)){
                RadioButton rb = (RadioButton) radioGroup.getChildAt(2);
                rb.setChecked(true);
            }else if(answerText.equals(optionFour)){
                RadioButton rb = (RadioButton) radioGroup.getChildAt(3);
                rb.setChecked(true);
            }
        }else{
            questionTextET.setHint(questionText);
            optionOneET.setHint(optionOne);
            optionTwoET.setHint(optionTwo);
            optionThreeET.setHint(optionThree);
            optionFourET.setHint(optionFour);
        }






        return view;
    }

    public String getQuestionText() {
        return questionTextET.getText().toString();
    }

    public String getAnswerText() {
        int loc = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));
        EditText et = (EditText) optionHolder.getChildAt(loc);
        return et.getText().toString();
    }

    public String getOptionOne() {
        return optionOneET.getText().toString();
    }

    public String getOptionTwo() {
        return optionTwoET.getText().toString();
    }

    public String getOptionThree() {
        return optionThreeET.getText().toString();
    }

    public String getOptionFour() {
        return optionFourET.getText().toString();
    }

    //Function is used to check if an answer choice has been selected.
    public boolean isProper(){
        if(radioGroup.getCheckedRadioButtonId() != -1 && !getQuestionText().equals("")){
            return true;
        }
        return false;
    }

    public QuestionMCQ getQuestion(){
        return new QuestionMCQ(getQuestionText(), getOptionOne(), getOptionTwo(), getOptionThree(), getOptionFour(), getAnswerText());
    }
}
