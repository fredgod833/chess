package com.fgodard.leitner;

import fr.fgodard.passivation.Parser;
import fr.fgodard.passivation.PassivationManager;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

public class LearnDao {

    private static int fibRt(int n, int a, int b){
        return (n == 1 ? a : fibRt(n-1, a+b, a));
    }

    private static int fib(int n){
        return (n == 0 ? 0 : fibRt(n, 1, 0));
    }

    private PassivationManager passivationManager = new PassivationManager();

    private String questionType;

    private String answerType;


    public LearnDao(final String questionType, final String answerType) {
        this.questionType = questionType;
        this.answerType = answerType;
    }

    public int getQuestionDelay(final String userId, int level) {
        if (level == 0) {
            return 0;

        } else {
            int result = readQuestionDelay(userId, level);
            if (result == 0) {
                result = fib(level)*24*60;
                saveQuestionDelay(userId, level, result);
                return result;
            }

            return result;
        }
    }

    public int readQuestionDelay(final String userId, int level) {
        //TODO
        return 0;
    }

    public void saveQuestionDelay(final String userId, int level, int delay) {
        //TODO
    }

    public <Q,A> List<QnA<Q,A>> listQuestions(final String userId) {

        Parser<Q> qParser = passivationManager.getParser(questionType);
        Parser<A> aParser = passivationManager.getParser(answerType);
        List<QnA<Q,A>> result = new ArrayList<>();
        //TODO
        return result;

    }

    public <Q,A> void addOrUpdateQuestion(final String userId, QnA<Q,A> question) {
        //TODO
    }

}
