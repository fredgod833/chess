package com.fgodard.chess.beans;

import java.io.Serializable;

/**
 * Created by crios on 18/11/23.
 */
public class PosStat implements Serializable {

    // nb de parties gagnée par blancs avec cette position
    private long whiteScore = 0;

    // nb de parties nulles avec cette position
    private long nullScore = 0;

    // nb de parties gagnée par noir avec cette position
    private long blackScore = 0;

    // nb total de parties avec cette position
    private long totalScore = 0;

    //année de la premiere partie avec cette position
    private int firstYear = 9999;

    //année de la derniere partie avec cette position
    private int lastYear = -9999;

    // elo moyen des joueurs pour cette position
    private double avgElo = 0;

    // nb moyen de demi-coups pour obtenir la position
    private double avgPly = 0;

    public long getWhiteScore() {
        return whiteScore;
    }

    public void setWhiteScore(long whiteScore) {
        this.whiteScore = whiteScore;
    }

    public long getNullScore() {
        return nullScore;
    }

    public void setNullScore(long nullScore) {
        this.nullScore = nullScore;
    }

    public long getBlackScore() {
        return blackScore;
    }

    public void setBlackScore(long blackScore) {
        this.blackScore = blackScore;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(long totalScore) {
        this.totalScore = totalScore;
    }

    public int getFirstYear() {
        return firstYear;
    }

    public void setFirstYear(int firstYear) {
        this.firstYear = firstYear;
    }

    public int getLastYear() {
        return lastYear;
    }

    public void setLastYear(int lastYear) {
        this.lastYear = lastYear;
    }

    public double getAvgElo() {
        return avgElo;
    }

    public void setAvgElo(double avgElo) {
        this.avgElo = avgElo;
    }

    public double getAvgPly() {
        return avgPly;
    }

    public void setAvgPly(double avgPly) {
        this.avgPly = avgPly;
    }

}
