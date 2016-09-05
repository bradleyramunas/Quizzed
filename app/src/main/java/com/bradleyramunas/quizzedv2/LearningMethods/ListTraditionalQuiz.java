package com.bradleyramunas.quizzedv2.LearningMethods;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bradleyramunas.quizzedv2.FragmentFRQ;
import com.bradleyramunas.quizzedv2.FragmentMCQ;
import com.bradleyramunas.quizzedv2.Question;
import com.bradleyramunas.quizzedv2.QuestionFRQ;
import com.bradleyramunas.quizzedv2.QuestionMCQ;
import com.bradleyramunas.quizzedv2.QuestionType;
import com.bradleyramunas.quizzedv2.Quiz;
import com.bradleyramunas.quizzedv2.QuizSelect;
import com.bradleyramunas.quizzedv2.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class ListTraditionalQuiz extends AppCompatActivity {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    private LinearLayout ll;
    private String quizName;
    private Button finishQuiz;
    private boolean hasCompletedQuiz = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_traditional_quiz);

        Intent i = getIntent();
        ll = (LinearLayout) findViewById(R.id.fragmentListHolder);
        quizName = i.getStringExtra("quizName");
        finishQuiz = (Button) findViewById(R.id.checkAnswers);


        ArrayList<Fragment> questionList = new ArrayList<>();

        Quiz quiz = i.getParcelableExtra("quiz");
        for(Question q : quiz.getQuestionList()){
            if(q instanceof QuestionFRQ){
                Fragment fragment = FragmentFRQ.newInstance((QuestionFRQ) q);
                questionList.add(fragment);
            }else if(q instanceof QuestionMCQ){
                Fragment fragment = FragmentMCQ.newInstance((QuestionMCQ) q);
                questionList.add(fragment);
            }
        }

        getSupportActionBar().setTitle(quizName);

        for(Fragment f : questionList){
            if(f.getClass() == FragmentFRQ.class){
                FragmentFRQ ffrq = (FragmentFRQ) f;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                FrameLayout fl = new FrameLayout(this);
                fl.setLayoutParams(params);
                ll.addView(fl, ll.getChildCount()-1);
                fl.setId(generateViewId());
                getSupportFragmentManager().beginTransaction().replace(fl.getId(), ffrq).commit();
            }else if(f.getClass() == FragmentMCQ.class){
                FragmentMCQ fmcq = (FragmentMCQ) f;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                FrameLayout fl = new FrameLayout(this);
                fl.setLayoutParams(params);
                ll.addView(fl, ll.getChildCount()-1);
                fl.setId(generateViewId());
                getSupportFragmentManager().beginTransaction().replace(fl.getId(), fmcq).commit();
            }
        }

    }

    @Override
    public void onBackPressed(){
        if(hasCompletedQuiz){
            Intent i = new Intent();
            i.putExtra("quizName", quizName);
            setResult(QuizSelect.GOOD, i);
            finish();
        }else{
            setResult(QuizSelect.CANCELED);
            finish();
        }
        super.onBackPressed();
    }

    public void onCheckQuizPress(View view){
        int amountCorrect = 0;
        int questionAmount = ll.getChildCount()-1;
        hasCompletedQuiz = true;
        int childCount = ll.getChildCount()-1;
        for(int i = 0; i<childCount; i++){
            FrameLayout fl = (FrameLayout) ll.getChildAt(i);
            Fragment f = getSupportFragmentManager().findFragmentById(fl.getId());
            if(f.getClass() == FragmentMCQ.class){
                FragmentMCQ fragmentMCQ = (FragmentMCQ) f;
                if(fragmentMCQ.checkAnswer()) amountCorrect++;
            }else if(f.getClass() == FragmentFRQ.class){
                FragmentFRQ fragmentFRQ = (FragmentFRQ) f;
                if(fragmentFRQ.checkAnswer()) amountCorrect++;
            }
        }
        Date date = new Date();
        Context context = this;
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.quiz_data_preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(quizName + "_lastCompleted", date.getTime()).apply();
        editor.putInt(quizName + "_amountCorrect", amountCorrect).apply();
        editor.putInt(quizName + "_totalQuestions", questionAmount).apply();

        ll.removeView(finishQuiz);

        new AlertDialog.Builder(this)
                .setTitle("Quiz Completed!")
                .setMessage("You scored " + amountCorrect + " out of " + questionAmount + " questions correct!\nWould you like to review your quiz?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent i = new Intent();
                        i.putExtra("quizName", quizName);
                        setResult(QuizSelect.GOOD, i);
                        finish();
                    }
                })
                .show();
    }

}
