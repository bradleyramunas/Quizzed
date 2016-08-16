package com.bradleyramunas.quizzedv2.LearningMethods;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.bradleyramunas.quizzedv2.FragmentCreateFRQ;
import com.bradleyramunas.quizzedv2.FragmentCreateMCQ;
import com.bradleyramunas.quizzedv2.FragmentFRQ;
import com.bradleyramunas.quizzedv2.FragmentMCQ;
import com.bradleyramunas.quizzedv2.QuestionType;
import com.bradleyramunas.quizzedv2.R;

import java.util.ArrayList;

public class TraditionalQuiz extends AppCompatActivity {



    private ArrayList<Fragment> questions;
    private Button next;
    private Button back;
    private FrameLayout fl;
    private int currentIndex = 0;
    private int maxIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traditional_quiz);

        fl = (FrameLayout) findViewById(R.id.fragmentHolder);
        questions = new ArrayList<>();
        next = (Button) findViewById(R.id.buttonNext);
        back = (Button) findViewById(R.id.buttonBack);

        back.setEnabled(false);

        Intent i = getIntent();
        int amount = i.getIntExtra("questionAmount", 0);

        Log.e("TEST", amount+"");

        for(int z = 0; z<amount; z++){
            //mcq = false, frq = true
            Bundle question = i.getBundleExtra("bundle"+z);
            boolean type = question.getBoolean("questionType");
            if(type){
                Fragment frq = FragmentFRQ.newInstance(question.getString("questionText"), question.getString("answerText"));
                questions.add(frq);
            }else{
                Fragment mcq = FragmentMCQ.newInstance(question.getString("questionText"), question.getString("answerText"),
                        question.getString("optionOne"), question.getString("optionTwo"), question.getString("optionThree"), question.getString("optionFour"));
                questions.add(mcq);
            }
        }

        maxIndex = questions.size()-1;

        getSupportFragmentManager().beginTransaction().replace(fl.getId(), questions.get(0)).commit();


    }

    public void onNextPress(View view){
        currentIndex++;
        updateButtons();
        changeFragment(currentIndex);
    }

    public void onBackPress(View view){
        currentIndex--;
        updateButtons();
        changeFragment(currentIndex);
    }

    private void updateButtons(){
        if(currentIndex == 0){
            back.setEnabled(false);
        }else{
            back.setEnabled(true);
        }

        if(currentIndex == maxIndex){
            next.setEnabled(false);
        }else{
            next.setEnabled(true);
        }
    }


    //TODO: Fix crashing from switching between questions too often
    private void changeFragment(int index){
        Fragment f = questions.get(index);

        if(f.getClass() == FragmentFRQ.class){
            Fragment old = getSupportFragmentManager().findFragmentById(fl.getId());
            if(old != null && !old.isRemoving()){
                FragmentFRQ t = (FragmentFRQ) f;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
                ft.replace(fl.getId(), t);
                ft.commit();
            }
        }else{
            Fragment old = getSupportFragmentManager().findFragmentById(fl.getId());
            if(old != null && !old.isRemoving()){
                FragmentMCQ t = (FragmentMCQ) f;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
                ft.replace(fl.getId(), t);
                ft.commit();
            }

        }

    }


}
