package com.bradleyramunas.quizzedv2;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bradleyramunas.quizzedv2.LearningMethods.ListTraditionalQuiz;

import java.util.ArrayList;
import java.util.Random;


public class FragmentHomeScreen extends Fragment {

    private LinearLayout homeScreenHolder;

    public FragmentHomeScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_home_screen, container, false);
        homeScreenHolder = (LinearLayout) view.findViewById(R.id.homeScreenHolder);
//        MyDBHandler db = new MyDBHandler(getContext(), null);
//        ArrayList<String> quizNames = db.getQuizNames();
//        int amountOfQuizzesToDisplay = (int) Math.ceil(quizNames.size()/4f);
//        Log.e("Amount", amountOfQuizzesToDisplay + "");
//        for(int i = 0; i<amountOfQuizzesToDisplay; i++){
//            Quiz quizToDisplay = db.getQuizFromDatabase(quizNames.get(new Random().nextInt(quizNames.size())));
//            addQuizCard(quizToDisplay);
//        }
        return view;
    }

    private void addQuizCard(final Quiz quiz){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.quiz_data_preferences_key), Context.MODE_PRIVATE);

        CardView cardView = new CardView(homeScreenHolder.getContext());
        CardView.LayoutParams layoutParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0,0,24);
        cardView.setLayoutParams(layoutParams);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchQuiz(quiz);
            }
        });

        LinearLayout cardLinearLayout = new LinearLayout(homeScreenHolder.getContext());
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cardLinearLayout.setPadding(8,8,8,8);
        cardLinearLayout.setLayoutParams(layoutParams1);

        TextView quizTitle = new TextView(homeScreenHolder.getContext());
        quizTitle.setTextSize(18);
        quizTitle.setTextColor(Color.BLACK);
        quizTitle.setText(quiz.getName());

        TextView questionAmount = new TextView(homeScreenHolder.getContext());
        questionAmount.setText(quiz.getQuestionList().size() + " questions");

        TextView lastReviewed = new TextView(homeScreenHolder.getContext());
        lastReviewed.setText("Last Taken: " + sharedPreferences.getString(quiz.getName() + "_lastCompleted", "Never"));


        cardLinearLayout.addView(quizTitle);
        cardLinearLayout.addView(questionAmount);
        cardLinearLayout.addView(lastReviewed);
        cardView.addView(cardLinearLayout);
        homeScreenHolder.addView(cardView);

    }

    private void launchQuiz(Quiz quiz){
        Intent i = new Intent(getActivity(), ListTraditionalQuiz.class);
        i.putExtra("quizName", quiz.getName().toString());
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
        getActivity().startActivityForResult(i, 10);
    }

}
