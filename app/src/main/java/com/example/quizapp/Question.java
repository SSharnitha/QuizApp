package com.example.quizapp;

public class Question {
    String question_number="";
    String question="";
    String option_a="";
    String option_b="";
    String option_c="";
    String option_d="";
    String correct_option="";
    String solution="";
    String correct_answer="";

    public Question(String question_number, String question, String option_a, String option_b, String option_c, String option_d, String correct_option, String solution, String correct_answer) {
        this.question_number = question_number;
        this.question = question;
        this.option_a = option_a;
        this.option_b = option_b;
        this.option_c = option_c;
        this.option_d = option_d;
        this.correct_option = correct_option;
        this.solution = solution;
        this.correct_answer = correct_answer;
    }

    public Question() {

    }

    public String getQuestion_number() {
        return question_number;
    }

    public void setQuestion_number(String question_number) {
        this.question_number = question_number;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption_a() {
        return option_a;
    }

    public void setOption_a(String option_a) {
        this.option_a = option_a;
    }

    public String getOption_b() {
        return option_b;
    }

    public void setOption_b(String option_b) {
        this.option_b = option_b;
    }

    public String getOption_c() {
        return option_c;
    }

    public void setOption_c(String option_c) {
        this.option_c = option_c;
    }

    public String getOption_d() {
        return option_d;
    }

    public void setOption_d(String option_d) {
        this.option_d = option_d;
    }

    public String getCorrect_option() {
        return correct_option;
    }

    public void setCorrect_option(String correct_option) {
        this.correct_option = correct_option;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question_number='" + question_number + '\'' +
                ", question='" + question + '\'' +
                ", option_a='" + option_a + '\'' +
                ", option_b='" + option_b + '\'' +
                ", option_c='" + option_c + '\'' +
                ", option_d='" + option_d + '\'' +
                ", correct_option='" + correct_option + '\'' +
                ", solution='" + solution + '\'' +
                ", correct_answer='" + correct_answer + '\'' +
                '}';
    }
}
