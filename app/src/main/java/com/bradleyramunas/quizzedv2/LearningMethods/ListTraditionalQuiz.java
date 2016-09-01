package com.bradleyramunas.quizzedv2.LearningMethods;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bradleyramunas.quizzedv2.FragmentFRQ;
import com.bradleyramunas.quizzedv2.FragmentMCQ;
import com.bradleyramunas.quizzedv2.QuestionType;
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

    LinearLayout ll;
    String quizName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_traditional_quiz);

        Intent i = getIntent();
        ll = (LinearLayout) findViewById(R.id.fragmentListHolder);
        quizName = i.getStringExtra("quizName");

        int amount = i.getIntExtra("questionAmount", 0);

        ArrayList<Fragment> questionList = new ArrayList<>();

        for(int z = 0; z<amount; z++){
            //mcq = false, frq = true
            Bundle question = i.getBundleExtra("bundle"+z);
            boolean type = question.getBoolean("questionType");
            if(type){
                Fragment frq = FragmentFRQ.newInstance(question.getString("questionText"), question.getString("answerText"));
                questionList.add(frq);
            }else{
                Fragment mcq = FragmentMCQ.newInstance(question.getString("questionText"), question.getString("answerText"),
                        question.getString("optionOne"), question.getString("optionTwo"), question.getString("optionThree"), question.getString("optionFour"));
                questionList.add(mcq);
            }
        }

        getSupportActionBar().setTitle(i.getStringExtra(quizName));

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

    public void onCheckQuizPress(View view){
        int amountCorrect = 0;
        int questionAmount = ll.getChildCount()-1;

        int childCount = ll.getChildCount()-1;
        for(int i = 0; i<childCount; i++){
            FrameLayout fl = (FrameLayout) ll.getChildAt(i);
            Fragment f = getSupportFragmentManager().findFragmentById(fl.getId());
            if(f.getClass() == FragmentMCQ.class){

            }else if(f.getClass() == FragmentFRQ.class){

            }
        }
        Date date = new Date();
        Context context = this;
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.quiz_data_preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(quizName + "_lastCompleted", date.getTime()).apply();
        editor.putInt(quizName + "_amountCorrect", amountCorrect).apply();
        editor.putInt(quizName + "_totalQuestions", questionAmount).apply();

        setResult(QuizSelect.GOOD);
        finish();
    }
}
