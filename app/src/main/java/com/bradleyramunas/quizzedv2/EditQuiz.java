package com.bradleyramunas.quizzedv2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class EditQuiz extends AppCompatActivity {



    private Fragment lastDeleted = null;
    private int position = 0;


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

    private ArrayList<Quiz> questions;
    private LinearLayout questionHolder;
    private EditText title;
    private FloatingActionButton fab;
    private CoordinatorLayout rl;
    private MyDBHandler db;
    private String originalQuizName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_quiz);

        questionHolder = (LinearLayout) findViewById(R.id.questionHolder);
        title = (EditText) findViewById(R.id.title);
        questions = new ArrayList<>();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        rl = (CoordinatorLayout) findViewById(R.id.forSnackbar);
        db = new MyDBHandler(this, null);

        Intent i = getIntent();
        int amount = i.getIntExtra("questionAmount", 0);
        title.setText(i.getStringExtra("originalQuizName"));
        originalQuizName = i.getStringExtra("originalQuizName");


        for(int z = 0; z<amount; z++){
            //mcq = false, frq = true
            Bundle question = i.getBundleExtra("bundle"+z);
            boolean type = question.getBoolean("questionType");
            if(type){
                FragmentCreateFRQ frq = FragmentCreateFRQ.newCustomInstance(question.getString("questionText"), question.getString("answerText"));
                FrameLayout fl = new FrameLayout(this);
                fl.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                int id = generateViewId();
                fl.setId(id);
                questionHolder.addView(fl);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
                ft.replace(id, frq);
                ft.commit();
            }else{
                FragmentCreateMCQ mcq = FragmentCreateMCQ.newCustomInstance(question.getString("questionText"), question.getString("answerText"),
                        question.getString("optionOne"), question.getString("optionTwo"), question.getString("optionThree"), question.getString("optionFour"));
                FrameLayout fl = new FrameLayout(this);
                fl.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                int id = generateViewId();
                //Log.e("TEST", "" + id);
                fl.setId(id);
                questionHolder.addView(fl);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
                ft.replace(id, mcq);
                ft.commit();
            }
        }

    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Cancel editing quiz")
                .setMessage("Are you sure you want to cancel? No changes will be made.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        setResult(QuizSelect.CANCELED);
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();

    }

    public void onFinish(View view){
        int children = questionHolder.getChildCount();
        //Log.e("HERE", children+"");
        if(children < 1){
            Toast.makeText(this, "There are no questions!", Toast.LENGTH_LONG).show();
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Finish editing quiz")
                    .setMessage("Are you sure you have finished editing your quiz?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            onFinishChecked();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();

        }
    }

    public void onFinishChecked(){
        int children = questionHolder.getChildCount();
        ArrayList<String> quizNames = db.getQuizNames();

        if(title.getText().toString().equals("")){
            Toast.makeText(this, "You have no title!", Toast.LENGTH_LONG).show();
            return;
        }

        if(quizNames.contains(title.getText().toString()) && !title.getText().toString().equals(originalQuizName)){
            Toast.makeText(this, "A quiz with that name already exists!", Toast.LENGTH_LONG).show();
            return;
        }

        for(int z = 0; z<children; z++){
            FrameLayout fl = (FrameLayout) questionHolder.getChildAt(z);

            Fragment f = getSupportFragmentManager().findFragmentById(fl.getId());

            if(f.getClass() == FragmentCreateFRQ.class){
                if(!((FragmentCreateFRQ)f).isProper()){
                    Toast.makeText(this, "One or more of your free response questions are missing answers.", Toast.LENGTH_LONG).show();
                    return;
                }
            }else{
                if(!((FragmentCreateMCQ)f).isProper()){
                    Toast.makeText(this, "One or more of your multiple choice questions are missing answers.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }


        //tfw u hate parceables so u just make bundles with a bunch of extras lol
        Intent i = new Intent();
        String tag = "bundle";
        for(int z = 0; z<children; z++){
            Bundle b = new Bundle();
            FrameLayout fl = (FrameLayout) questionHolder.getChildAt(z);

            Fragment f = getSupportFragmentManager().findFragmentById(fl.getId());
            //mcq = false, frq = true
            if(f.getClass().getName().equals("com.bradleyramunas.quizzedv2.FragmentCreateFRQ")){
                FragmentCreateFRQ frq = (FragmentCreateFRQ) f;
                b.putBoolean("questionType", true);
                b.putString("questionText", frq.getQuestionText());
                b.putString("answerText", frq.getAnswerText());
            }else{
                FragmentCreateMCQ mcq = (FragmentCreateMCQ) f;
                b.putBoolean("questionType", false);
                b.putString("questionText", mcq.getQuestionText());
                b.putString("answerText", mcq.getAnswerText());
                b.putString("optionOne", mcq.getOptionOne());
                b.putString("optionTwo", mcq.getOptionTwo());
                b.putString("optionThree", mcq.getOptionThree());
                b.putString("optionFour", mcq.getOptionFour());

            }
            i.putExtra(tag+z, b);
        }
        i.putExtra("questionAmount", children);
        i.putExtra("quizName", title.getText().toString());
        i.putExtra("originalQuizName", originalQuizName);
        setResult(QuizSelect.GOOD, i);
        finish();
    }

    public void onAddMultipleChoice(View view){
        FragmentCreateMCQ mcq = FragmentCreateMCQ.newCreationInstance();
        FrameLayout fl = new FrameLayout(this);
        fl.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        int id = generateViewId();
        //Log.e("TEST", "" + id);
        fl.setId(id);
        questionHolder.addView(fl);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
        ft.replace(id, mcq);
        ft.commit();
    }

    public void onAddFreeResponse(View view){
        FragmentCreateFRQ frq = FragmentCreateFRQ.newInstance();
        FrameLayout fl = new FrameLayout(this);
        fl.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        int id = generateViewId();
        //Log.e("TEST", "" + id);
        fl.setId(id);
        questionHolder.addView(fl);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
        ft.replace(id, frq);
        ft.commit();
    }

    public void onDeleteQuestionPress(View view){
        FrameLayout fl = (FrameLayout) view.getParent().getParent().getParent().getParent();

        lastDeleted = getSupportFragmentManager().findFragmentById(fl.getId());

        for(int i = 0; i<questionHolder.getChildCount(); i++){
            if(questionHolder.getChildAt(i).getId() == fl.getId()){
                position = i;
                break;
            }
        }

        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager()
                        .findFragmentById(fl.getId()))
                .commit();

        Snackbar snackbar = Snackbar
                .make(rl, "Question deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        FrameLayout fl = new FrameLayout(getBaseContext());
                        fl.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                        int id = generateViewId();
                        //Log.e("TEST", "" + id);
                        fl.setId(id);
                        questionHolder.addView(fl, position);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
                        ft.replace(id, lastDeleted);
                        ft.commit();

                        Snackbar snackbar1 = Snackbar.make(rl, "Question restored", Snackbar.LENGTH_SHORT);
                        snackbar1.show();

                    }
                });
        snackbar.show();

        questionHolder.removeViewAt(position);

    }
}
