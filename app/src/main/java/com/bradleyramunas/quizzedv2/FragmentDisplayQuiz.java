package com.bradleyramunas.quizzedv2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bradleyramunas.quizzedv2.LearningMethods.TraditionalQuiz;


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
        quizLastTaken.setText(getArguments().getString("quizLastTaken"));

        methodOneTitle.setText("Quiz");
        methodOneDescription.setText("Take a traditional quiz");
        //TODO: Setup sharedprefs and insert date strings of last taken...
        //remember to setup sharedprefs on creation of each quiz and deletion after deleting
        //methodOneLastTaken.setText(this.getActivity().getSharedPreferences());


        quizCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TraditionalQuiz.class);
                i.putExtra("quizName", quizTitle.getText().toString());
                i.putExtra("questionAmount", quiz.getQuestionList().size());
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
                startActivityForResult(i, 10);

            }
        });

        return view;
    }

}
