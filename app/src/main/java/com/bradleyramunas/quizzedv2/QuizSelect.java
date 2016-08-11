package com.bradleyramunas.quizzedv2;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;

public class QuizSelect extends AppCompatActivity {

    public Drawer drawer = null;
    public MyDBHandler db;

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

        final QuizSelect quiz = this;

        final Intent intent = new Intent(this, NewQuiz.class);
        db = new MyDBHandler(this, null);
        AccountHeader header = new AccountHeaderBuilder().withActivity(this).withHeaderBackground(R.drawable.header).build();
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
                            String quizName = i.getName().toString();
                        }else if(i.getTag() != null && i.getTag().equals("add")){
                            startActivityForResult(intent, 1);
                        }


                        return true;
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(View view, int position, IDrawerItem drawerItem) {
                        LinearLayout ll = (LinearLayout) view;
                        LinearLayout ll2 = (LinearLayout) ll.getChildAt(1);
                        AppCompatTextView tv = (AppCompatTextView) ll2.getChildAt(0);

                        //LAYOUT: View = LinearLayout -> 2 LinearLayoutChildren -> 1st LinearLayoutChildren has Child AppCompatTextView (0th) -> Child = name of quiz!!!
                        return true;
                    }
                })
                .build();
        populateDrawer();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.e("HERE", "test");
        this.getMenuInflater().inflate(R.menu.quiz_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public void populateDrawer(){
        drawer.removeAllItems();
        drawer.addItem(new PrimaryDrawerItem().withName("Home").withIcon(R.drawable.ic_home_black_24dp));
        drawer.addItem(new SectionDrawerItem().withName("My Quizzes"));
        ArrayList<String> names = db.getQuizNames();
        for(String s : names){
            final String name = s;
            drawer.addItem(new PrimaryDrawerItem().withName(s).withIcon(R.drawable.ic_library_books_black_24dp).withTag("quiz"));
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
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == 1){
            if(resultCode == GOOD){

                Toast.makeText(this, "GOOD", Toast.LENGTH_LONG).show();
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


            }else if(resultCode == EMPTY){
                Toast.makeText(this, "EMPTY", Toast.LENGTH_LONG).show();
            }else if(resultCode == CANCELED){
                Toast.makeText(this, "CANCELED", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }
    }

}
