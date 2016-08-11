package com.bradleyramunas.quizzedv2;


import java.util.ArrayList;

/**
 * Created by Bradley on 8/4/2016.
 */
public class Quiz {

    private ArrayList<Question> questionList;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Quiz(String name) {
        this.name = name;
        questionList = new ArrayList<>();
    }

    public ArrayList<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(ArrayList<Question> questionList) {
        this.questionList = questionList;
    }

    public void addQuestion(Question q){
        questionList.add(q);
    }

    public boolean removeQuestion(Question q){
        if(questionList.contains(q)){
            questionList.remove(q);
            return true;
        }
        return false;
    }

}
