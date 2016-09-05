package com.bradleyramunas.quizzedv2;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bradleyramunas.quizzedv2.MiscActivities.AboutScreen;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;



//TODO: Add MIT copyright
//TODO: Update github website thingie

public class QuizSelect extends AppCompatActivity {

    private Drawer drawer = null;
    private MyDBHandler db;
    private FrameLayout fragmentHolder;

    public static final AtomicInteger identifier = new AtomicInteger(0);

    private long id = -1;

    //RESULTCODES
    public static final int GOOD = 1000;
    public static final int CANCELED = 1001;
    public static final int EMPTY = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentHolder = (FrameLayout) findViewById(R.id.fragmentHolder);
        final QuizSelect quiz = this;

        final Intent intent = new Intent(this, NewQuiz.class);
        db = new MyDBHandler(this, null);
        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withCompactStyle(true)
                .build();
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(header)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        PrimaryDrawerItem i = (PrimaryDrawerItem) drawerItem;
                        if(i.getTag() != null && i.getTag().equals("quiz")){
                            drawer.closeDrawer();
                            String quizName = i.getName().toString();
                            Quiz quiz = db.getQuizFromDatabase(quizName);
                            FragmentDisplayQuiz fdq = FragmentDisplayQuiz.createInstance(quizName, quiz.getQuestionList().size(), "Never");
                            changeFragment(fdq);
                        }else if(i.getTag() != null && i.getTag().equals("add")){
                            drawer.closeDrawer();
                            startActivityForResult(intent, 1);
                        }else if(i.getTag() != null && i.getTag().equals("home")){
                            drawer.closeDrawer();
                            FragmentHomeScreen homeScreen = new FragmentHomeScreen();
                            changeFragment(homeScreen);
                        }


                        return true;
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(View view, int position, IDrawerItem drawerItem) {
                        PrimaryDrawerItem i = (PrimaryDrawerItem) drawerItem;
                        if(i.getTag() != null && i.getTag().equals("quiz")){
                            id = drawerItem.getIdentifier();
                            QuizSelect.this.registerForContextMenu(view);
                            openContextMenu(view);

                            return true;
                        }else {

                        }
                        return false;
                    }
                })
                .build();
        populateDrawer();
        FragmentHomeScreen homeScreen = new FragmentHomeScreen();
        changeFragment(homeScreen);

    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen()) drawer.closeDrawer();
        else super.onBackPressed();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.e("HERE", "test");
        this.getMenuInflater().inflate(R.menu.quiz_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        String title = item.getTitle().toString();
        switch (title){
            case "Delete":
                new AlertDialog.Builder(this)
                        .setTitle("Delete this quiz")
                        .setMessage("Are you sure you want to delete this quiz?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PrimaryDrawerItem pdi = (PrimaryDrawerItem) drawer.getDrawerItem(id);
                                if(pdi.isSelected()){
                                    FragmentHomeScreen homeScreen = new FragmentHomeScreen();
                                    changeFragment(homeScreen);
                                }
                                deleteSharedPrefs(pdi.getName().toString());
                                db.deleteQuiz(pdi.getName().toString());
                                drawer.removeItem(id);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                break;
            case "Edit":
                Intent i = new Intent(this, EditQuiz.class);
                int tagnum = 0;
                PrimaryDrawerItem nameOfQuiz = (PrimaryDrawerItem) drawer.getDrawerItem(id);
                Quiz selected = db.getQuizFromDatabase(nameOfQuiz.getName().toString());
                i.putExtra("originalQuizName", selected.getName());
                i.putExtra("questionAmount", selected.getQuestionList().size());
                //mcq = false, frq = true
                for(Question q : selected.getQuestionList()){
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
                startActivityForResult(i, 2);
                break;
        }
        return true;
    }

    public void populateDrawer(){
        drawer.removeAllItems();
        drawer.removeAllStickyFooterItems();

        drawer.addItem(new PrimaryDrawerItem().withName("Home").withIcon(R.drawable.ic_home_black_24dp).withTag("home"));
        drawer.addItem(new SectionDrawerItem().withName("My Quizzes"));
        ArrayList<String> names = db.getQuizNames();
        for(String s : names){
            final String name = s;
            drawer.addItem(new PrimaryDrawerItem().withName(s).withIcon(R.drawable.ic_library_books_black_24dp).withTag("quiz").withIdentifier(identifier.incrementAndGet()));
        }
        drawer.addStickyFooterItem(new PrimaryDrawerItem().withName("Add Quiz").withIcon(R.drawable.ic_add_box_black_24dp).withSelectable(false).withTag("add"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz_select, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:
                Intent i = new Intent(this, AboutScreen.class);
                startActivity(i);
                return true;
//            case -300:
//                Dialog dialog;
//                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Choose a color scheme");
//                final CharSequence[] items = {"Red", "Light Blue", "Dark Blue", "Dark Green", "Gray"};
//                int inputSelection = 0;
//                builder.setSingleChoiceItems(items, inputSelection, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which){
//                            case 0:
//                                changeTitleBarColor(0);
//                                break;
//                            case 1:
//                                changeTitleBarColor(1);
//                                break;
//                            case 2:
//                                changeTitleBarColor(2);
//                                break;
//                            case 3:
//                                changeTitleBarColor(3);
//                                break;
//                            case 4:
//                                changeTitleBarColor(4);
//                                break;
//                        }
//                        dialog.dismiss();
//                    }
//                });
//                dialog = builder.create();
//                dialog.show();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == 1){

            if(resultCode == GOOD){

                //Toast.makeText(this, "GOOD", Toast.LENGTH_LONG).show();

                ArrayList<Question> forQuiz = new ArrayList<>();
                Bundle b = intent.getExtras();
                int amount = b.getInt("questionAmount");
                for(int i = 0; i<amount; i++){
                    Bundle b2 = b.getBundle("bundle"+i);
                    boolean type = b2.getBoolean("questionType");
                    //mcq = false, frq = true
                    if(type){
                        String questionText = b2.getString("questionText");
                        String answerText   = b2.getString("answerText");
                        forQuiz.add(new QuestionFRQ(questionText, answerText));
                    }else{
                        String questionText = b2.getString("questionText");
                        String answerText   = b2.getString("answerText");
                        String optionOne    = b2.getString("optionOne");
                        String optionTwo    = b2.getString("optionTwo");
                        String optionThree  = b2.getString("optionThree");
                        String optionFour   = b2.getString("optionFour");
                        forQuiz.add(new QuestionMCQ(questionText, optionOne, optionTwo, optionThree, optionFour, answerText));
                    }
                }


                Quiz quiz = new Quiz(b.getString("quizName"));
                quiz.setQuestionList(forQuiz);
                db.addNewQuiz(quiz);
                populateDrawer();

            }else if(resultCode == EMPTY){

            }else if(resultCode == CANCELED){

            }else{

            }
        }
        if(requestCode == 2){
            if(resultCode == GOOD){

                ArrayList<Question> forQuiz = new ArrayList<>();
                Bundle b = intent.getExtras();
                int amount = b.getInt("questionAmount");
                for(int i = 0; i<amount; i++){
                    Bundle b2 = b.getBundle("bundle"+i);
                    boolean type = b2.getBoolean("questionType");
                    if(type){
                        String questionText = b2.getString("questionText");
                        String answerText   = b2.getString("answerText");
                        forQuiz.add(new QuestionFRQ(questionText, answerText));
                    }else{
                        String questionText = b2.getString("questionText");
                        String answerText   = b2.getString("answerText");
                        String optionOne    = b2.getString("optionOne");
                        String optionTwo    = b2.getString("optionTwo");
                        String optionThree  = b2.getString("optionThree");
                        String optionFour   = b2.getString("optionFour");
                        forQuiz.add(new QuestionMCQ(questionText, optionOne, optionTwo, optionThree, optionFour, answerText));
                    }
                }
                Quiz quiz = new Quiz(b.getString("quizName"));
                quiz.setQuestionList(forQuiz);
                db.deleteQuiz(b.getString("originalQuizName"));
                db.addNewQuiz(quiz);
                updateSharedPrefs(b.getString("originalQuizName"), b.getString("quizName"));
                populateDrawer();

                FragmentDisplayQuiz fragmentDisplayQuiz = (FragmentDisplayQuiz) getSupportFragmentManager().findFragmentById(fragmentHolder.getId());
                fragmentDisplayQuiz.updateEntireQuiz(b.get("quizName").toString(), amount);


            }
        }
        if(requestCode == 10){
            if(resultCode == GOOD){
                FragmentDisplayQuiz fragmentDisplayQuiz = (FragmentDisplayQuiz) getSupportFragmentManager().findFragmentById(fragmentHolder.getId());
                fragmentDisplayQuiz.updateLastTaken();
            }
        }
    }

    public void changeFragment(Fragment f){
        getSupportFragmentManager().beginTransaction().replace(fragmentHolder.getId(), f).setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout).commit();
    }

    public void deleteSharedPrefs(String quizName){
        Context context = this;
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.quiz_data_preferences_key), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(quizName + "_lastCompleted").apply();
        editor.remove(quizName + "_amountCorrect").apply();
        editor.remove(quizName + "_totalQuestions").apply();
    }

    public void updateSharedPrefs(String quizName, String newQuizName){
        if(!quizName.equals(newQuizName)){
            Context context = this;
            SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.quiz_data_preferences_key), context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            long lastCompleted = sharedPreferences.getLong(quizName + "_lastCompleted", 0);
            int amountCorrect = sharedPreferences.getInt(quizName + "_amountCorrect", 0);
            int totalQuestions = sharedPreferences.getInt(quizName + "_totalQuestions", 0);
            editor.remove(quizName + "_lastCompleted").apply();
            editor.remove(quizName + "_amountCorrect").apply();
            editor.remove(quizName + "_totalQuestions").apply();
            editor.putLong(newQuizName + "_lastCompleted", lastCompleted).apply();
            editor.putInt(newQuizName + "_amountCorrect", amountCorrect).apply();
            editor.putInt(newQuizName + "_totalQuestions", totalQuestions).apply();
        }
    }

    public void changeTitleBarColor(int colorChoice){
        switch (colorChoice){
            case 0:
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E53935")));
                break;
            case 1:
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196F3")));
                break;
            case 2:
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#283593")));
                break;
            case 3:
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E7D32")));
                break;
            case 4:
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#757575")));
                break;
        }
    }

    public void onViewQuizzesSelect(View view){
        drawer.openDrawer();
    }

    public void onAddQuizSelect(View view){
        Intent i = new Intent(this, NewQuiz.class);
        startActivityForResult(i, 1);
    }
}
