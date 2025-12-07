package com.fgodard.leitner;

import java.util.*;

public class UserExamManager<Q,A> {

    private List<QnA<Q,A>> qnaList = null;

    private Map<Integer, Integer> levelDelays = new HashMap<>();

    private Set<Integer> reduceDelays = new HashSet<>();

    private String userName;

    private LearnDao learnDao;

    public UserExamManager(final String userName, final String questionType, final String answerType) {
        this.userName = userName;
        learnDao = new LearnDao(questionType,answerType);
    }

    private int getDelay(int level) {
        Integer result =  levelDelays.get(level);
        if (result != null) {
            return result;
        }
        result = learnDao.getQuestionDelay(userName, level);
        levelDelays.put(level, result);
        return result;
    }

    private void loadQnaList() {
        qnaList = learnDao.listQuestions(userName);
    }

    private void updateLevelDelay(int level, long qnaFailedDelay) {
        long delay0 = getDelay(level-1);
        long delay = getDelay(level);
        // si le délai est dépassé de plus de 40% on ne réajuste pas.
        long cutDelay = ((delay-delay0) * 130 / 100) + delay0;
        if (qnaFailedDelay < cutDelay) {
            reduceDelays.add(level);
        }
    }

    private void updateQnAResult(QnA<Q, A> qna, boolean result) {
        long lastTestTime = System.currentTimeMillis()/60000;
        if (result) {
            qna.setLevel(qna.getLevel()+1);
        } else if (qna.getLevel() > 0) {
            updateLevelDelay(qna.getLevel(), lastTestTime - qna.getLastTestTime());
            qna.setLevel(qna.getLevel()-1);
        }
        qna.setLastTestTime(lastTestTime);
    }

    public Optional<QnA<Q,A>> nextQuestion() {

        if (qnaList == null || qnaList.isEmpty()) {
            loadQnaList();
        }

        if (qnaList == null || qnaList.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(qnaList.remove(qnaList.size()-1));
    }

    public boolean checkAnswer(QnA<Q,A> qna, A answer) {
        boolean result;
        if (answer == null) {
            result = (qna.getAnswer() == null);

        } else {
            result = answer.equals(qna.getAnswer());
            updateQnAResult(qna, result);

        }
        return result;
    }

}
