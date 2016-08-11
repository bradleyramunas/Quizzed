package com.bradleyramunas.quizzedv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Bradley on 8/4/2016.
 */
public class MyDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "QUIZZES.db";
    public static final String TABLE_SAMPLE_QUIZ = "SampleQuiz";
    public static final String COLUMN_QUESTION_TEXT = "questionText";
    public static final String COLUMN_QUESTION_TYPE = "questionType";
    public static final String COLUMN_QUESTION_ANSWER = "questionAnswer";
    public static final String COLUMN_QUESTION_CHOICE_ONE = "questionAnswerChoiceOne";
    public static final String COLUMN_QUESTION_CHOICE_TWO = "questionAnswerChoiceTwo";
    public static final String COLUMN_QUESTION_CHOICE_THREE = "questionAnswerChoiceThree";
    public static final String COLUMN_QUESTION_CHOICE_FOUR = "questionAnswerChoiceFour";


    public static final String QUESTION_TYPE_MCQ = "mcqQuestion";
    public static final String QUESTION_TYPE_FRQ = "frqQuestion";

    public MyDBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SQLiteStatement query = db.compileStatement("CREATE TABLE '?' (? TEXT, ? TEXT, ? TEXT, ? TEXT, ? TEXT, ? TEXT, ? TEXT)");
        query.bindString(1, TABLE_SAMPLE_QUIZ);
        query.bindString(2, COLUMN_QUESTION_TEXT);
        query.bindString(3, COLUMN_QUESTION_TYPE);
        query.bindString(4, COLUMN_QUESTION_ANSWER);
        query.bindString(5, COLUMN_QUESTION_CHOICE_ONE);
        query.bindString(6, COLUMN_QUESTION_CHOICE_TWO);
        query.bindString(7, COLUMN_QUESTION_CHOICE_THREE);
        query.bindString(8, COLUMN_QUESTION_CHOICE_FOUR);
        query.execute();
        db.setTransactionSuccessful();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addNewQuiz(Quiz q){
        //TODO: Fix this database code, not creating columns correctly
        SQLiteDatabase db = getWritableDatabase();
        SQLiteStatement query = db.compileStatement("CREATE TABLE '?' (? TEXT, ? TEXT, ? TEXT, ? TEXT, ? TEXT, ? TEXT, ? TEXT)");
        query.bindString(1, q.getName());
        query.bindString(2, COLUMN_QUESTION_TEXT);
        query.bindString(3, COLUMN_QUESTION_TYPE);
        query.bindString(4, COLUMN_QUESTION_ANSWER);
        query.bindString(5, COLUMN_QUESTION_CHOICE_ONE);
        query.bindString(6, COLUMN_QUESTION_CHOICE_TWO);
        query.bindString(7, COLUMN_QUESTION_CHOICE_THREE);
        query.bindString(8, COLUMN_QUESTION_CHOICE_FOUR);
        query.execute();
        ArrayList<Question> questions = q.getQuestionList();

        String sql = "INSERT INTO '?' (questionText, questionType, questionAnswer, questionAnswerChoiceOne, questionAnswerChoiceTwo, questionAnswerChoiceThree, questionAnswerChoiceFour)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = db.compileStatement(sql);

        for(Question i : questions){
            if(i.getClass() == QuestionMCQ.class){

                QuestionMCQ mcq = (QuestionMCQ) i;

                statement.clearBindings();
                statement.bindString(1, q.getName());
                statement.bindString(2, mcq.get_questionText());
                statement.bindString(3, QUESTION_TYPE_MCQ);
                statement.bindString(4, mcq.get_answerText());
                statement.bindString(5, mcq.get_optionOne());
                statement.bindString(6, mcq.get_optionTwo());
                statement.bindString(7, mcq.get_optionThree());
                statement.bindString(8, mcq.get_optionFour());
                statement.executeInsert();

            }else{

                QuestionFRQ frq = (QuestionFRQ) i;

                statement.clearBindings();
                statement.bindString(1, q.getName());
                statement.bindString(2, frq.get_questionText());
                statement.bindString(3, QUESTION_TYPE_FRQ);
                statement.bindString(4, frq.get_answerText());
                statement.bindString(5, " ");
                statement.bindString(6, " ");
                statement.bindString(7, " ");
                statement.bindString(8, " ");
                statement.executeInsert();

            }
        }

        db.setTransactionSuccessful();
        db.close();
    }

    public void deleteQuiz(String name){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DROP TABLE IF EXISTS '?'";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1, name);
        statement.execute();
        db.setTransactionSuccessful();
        db.close();
    }

    public Quiz getQuizFromDatabase(String name){
        SQLiteDatabase db = getWritableDatabase();

        Quiz quiz = new Quiz(name);

        String query = "SELECT * FROM '" + name + "' WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(0).equals("android_metadata")){

            }else{
                String type = c.getString(c.getColumnIndex(COLUMN_QUESTION_TYPE));
                if(type.equals(QUESTION_TYPE_MCQ)){

                    String questionText = c.getString(c.getColumnIndex(COLUMN_QUESTION_TEXT));
                    String answerText = c.getString(c.getColumnIndex(COLUMN_QUESTION_ANSWER));
                    String optionOne = c.getString(c.getColumnIndex(COLUMN_QUESTION_CHOICE_ONE));
                    String optionTwo = c.getString(c.getColumnIndex(COLUMN_QUESTION_CHOICE_TWO));
                    String optionThree = c.getString(c.getColumnIndex(COLUMN_QUESTION_CHOICE_THREE));
                    String optionFour = c.getString(c.getColumnIndex(COLUMN_QUESTION_CHOICE_FOUR));

                    QuestionMCQ mcq = new QuestionMCQ(questionText, optionOne, optionTwo, optionThree, optionFour, answerText);
                    quiz.addQuestion(mcq);

                }else{

                    String questionText = c.getString(c.getColumnIndex(COLUMN_QUESTION_TEXT));
                    String answerText = c.getString(c.getColumnIndex(COLUMN_QUESTION_ANSWER));

                    QuestionFRQ frq = new QuestionFRQ(questionText, answerText);
                    quiz.addQuestion(frq);

                }
                c.moveToNext();
            }
        }
        c.close();
        db.close();
        return quiz;
    }

    public ArrayList<String> getQuizNames(){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<String> names = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        c.moveToFirst();
        c.moveToNext();
        while(!c.isAfterLast()){
            names.add(c.getString(0));
            c.moveToNext();
        }
        c.close();
        db.close();
        return names;
    }
}
