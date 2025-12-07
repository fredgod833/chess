package com.fgodard.leitner;

public class QnA<Q,A> {

    private long lastTestTime = 0;

    private int level = 0;

    private Q question;

    private A answer;

    public QnA(Q question, A answer) {
        this.question = question;
        this.answer = answer;
    }

    Q getQuestion() {
        return question;
    }

    A getAnswer() {
        return answer;
    }

    long getLastTestTime() {
        return lastTestTime;
    }

    void setLastTestTime(long lastTestTime) {
        this.lastTestTime = lastTestTime;
    }

    int getLevel() {
        return level;
    }

    void setLevel(int level) {
        this.level = level;
    }
}
