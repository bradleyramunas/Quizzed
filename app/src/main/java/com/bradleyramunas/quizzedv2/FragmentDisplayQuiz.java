package com.bradleyramunas.quizzedv2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bradleyramunas.quizzedv2.LearningMethods.ListTraditionalQuiz;
import com.bradleyramunas.quizzedv2.LearningMethods.TraditionalQuiz;

import java.util.Date;


public class FragmentDisplayQuiz extends Fragment {


    private TextView quizTitle, questionAmount, quizLastTaken, methodOneTitle, methodOneDescription, methodOneLastTaken;
    private String qtText, qaText, qltText;
    private CardView quizCard;
    private Quiz quiz;
    private MyDBHandler db;


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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.quiz_data_preferences_key), getContext().MODE_PRIVATE);
        quizTitle               = (TextView) view.findViewById(R.id.quizTitle);
        questionAmount          = (TextView) view.findViewById(R.id.questionAmount);
        quizLastTaken           = (TextView) view.findViewById(R.id.quizLastTaken);
        methodOneTitle          = (TextView) view.findViewById(R.id.methodOneTitle);
        methodOneDescription    = (TextView) view.findViewById(R.id.methodOneDescription);
        methodOneLastTaken      = (TextView) view.findViewById(R.id.methodOneLastTaken);
        quizCard                = (CardView) view.findViewById(R.id.traditionalQuizCard);
        db = new MyDBHandler(getActivity(), null);
        quiz = db.getQuizFromDatabase(getArguments().getString("quizTitle"));

        quizTitle.setText(getArguments().getString("quizTitle"));
        questionAmount.setText(getArguments().getInt("questionAmount") + " Questions");


        methodOneTitle.setText("Quiz");
        methodOneDescription.setText("Take a traditional quiz");
        String time = new Date(sharedPreferences.getLong(getArguments().getString("quizTitle") + "_lastCompleted", 0)).toString();
        if(time.contains("1969") || time.contains("1970")){
            time = "Never";
        }
        methodOneLastTaken.setText("Last Taken On: " + time);
        quizLastTaken.setText("Last Reviewed On: " + time);



        quizCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ListTraditionalQuiz.class);
                i.putExtra("quizName", getArguments().getString("quizTitle"));
                i.putExtra("questionAmount", getArguments().getInt("questionAmount"));
                int tagnum = 0;
                for(Question q : quiz.getQuestionList()){
                    Bundle b = new Bundle();
                    if(q.getClass() == QuestionFRQ.class){
                        QuestionFRQ frq = (QuestionFRQ) q;
                        b.putString("questionText", frq.get_questionText());
                        b.putString("answerText", frq.get_answerText());
                        b.putBoolean("questionType", true);
                    }else{
                        QuestionMCQ mcq = (QuestionMCQ) q;
                        b.putString("questionText", mcq.get_questionText());
                        b.putString("answerText", mcq.get_answerText());
                        b.putString("optionOne", mcq.get_optionOne());
                        b.putString("optionTwo", mcq.get_optionTwo());
                        b.putString("optionThree", mcq.get_optionThree());
                        b.putString("optionFour", mcq.get_optionFour());
                        b.putBoolean("questionType", false);
                    }
                    i.putExtra("bundle"+tagnum, b);
                    tagnum++;
                }
                getActivity().startActivityForResult(i, 10);

            }
        });

        return view;
    }

    public void updateLastTaken(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.quiz_data_preferences_key), getContext().MODE_PRIVATE);
        String time = new Date(sharedPreferences.getLong(getArguments().getString("quizTitle") + "_lastCompleted", 0)).toString();
        if(time.contains("1969")){
            time = "Never";
        }
        methodOneLastTaken.setText("Last Taken On: " + time);
        quizLastTaken.setText("Last Reviewed On: " + time);
    }

    public void updateEntireQuiz(String newTitle, int newQuestionAmount){
        getArguments().putString("quizTitle", newTitle);
        getArguments().putInt("questionAmount", newQuestionAmount);
        quizTitle.setText(getArguments().getString("quizTitle"));
        questionAmount.setText(getArguments().getInt("questionAmount") + " Questions");
        quiz = db.getQuizFromDatabase(getArguments().getString("quizTitle"));
        updateLastTaken();

    }


}
