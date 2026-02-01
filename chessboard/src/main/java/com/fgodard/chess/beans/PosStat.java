package com.fgodard.chess.beans;

import java.io.Serializable;

/**
 * Statistiques associées à une position d'échecs.
 *
 * <p>Cette classe contient les données statistiques collectées
 * sur une position à partir d'une base de parties :
 * <ul>
 *   <li>Scores des blancs, nulles et noirs</li>
 *   <li>Plage temporelle (première et dernière année)</li>
 *   <li>Classement Elo moyen des joueurs</li>
 *   <li>Nombre moyen de coups pour atteindre la position</li>
 * </ul>
 *
 * @author crios
 * @see Position
 */
public class PosStat implements Serializable {

    /** Nombre de parties gagnées par les blancs avec cette position */
    private long whiteScore = 0;

    /** Nombre de parties nulles avec cette position */
    private long nullScore = 0;

    /** Nombre de parties gagnées par les noirs avec cette position */
    private long blackScore = 0;

    /** Nombre total de parties avec cette position */
    private long totalScore = 0;

    /** Année de la première partie avec cette position */
    private int firstYear = 9999;

    /** Année de la dernière partie avec cette position */
    private int lastYear = -9999;

    /** Classement Elo moyen des joueurs pour cette position */
    private double avgElo = 0;

    /** Nombre moyen de demi-coups pour obtenir la position */
    private double avgPly = 0;

    /**
     * Retourne le nombre de victoires des blancs.
     *
     * @return le nombre de parties gagnées par les blancs
     */
    public long getWhiteScore() {
        return whiteScore;
    }

    /**
     * Définit le nombre de victoires des blancs.
     *
     * @param whiteScore le nombre de victoires
     */
    public void setWhiteScore(long whiteScore) {
        this.whiteScore = whiteScore;
    }

    /**
     * Retourne le nombre de parties nulles.
     *
     * @return le nombre de nulles
     */
    public long getNullScore() {
        return nullScore;
    }

    /**
     * Définit le nombre de parties nulles.
     *
     * @param nullScore le nombre de nulles
     */
    public void setNullScore(long nullScore) {
        this.nullScore = nullScore;
    }

    /**
     * Retourne le nombre de victoires des noirs.
     *
     * @return le nombre de parties gagnées par les noirs
     */
    public long getBlackScore() {
        return blackScore;
    }

    /**
     * Définit le nombre de victoires des noirs.
     *
     * @param blackScore le nombre de victoires
     */
    public void setBlackScore(long blackScore) {
        this.blackScore = blackScore;
    }

    /**
     * Retourne le nombre total de parties.
     *
     * @return le nombre total de parties
     */
    public long getTotalScore() {
        return totalScore;
    }

    /**
     * Définit le nombre total de parties.
     *
     * @param totalScore le nombre total
     */
    public void setTotalScore(long totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * Retourne l'année de la première partie.
     *
     * @return l'année de la première occurrence
     */
    public int getFirstYear() {
        return firstYear;
    }

    /**
     * Définit l'année de la première partie.
     *
     * @param firstYear l'année
     */
    public void setFirstYear(int firstYear) {
        this.firstYear = firstYear;
    }

    /**
     * Retourne l'année de la dernière partie.
     *
     * @return l'année de la dernière occurrence
     */
    public int getLastYear() {
        return lastYear;
    }

    /**
     * Définit l'année de la dernière partie.
     *
     * @param lastYear l'année
     */
    public void setLastYear(int lastYear) {
        this.lastYear = lastYear;
    }

    /**
     * Retourne le classement Elo moyen des joueurs.
     *
     * @return l'Elo moyen
     */
    public double getAvgElo() {
        return avgElo;
    }

    /**
     * Définit le classement Elo moyen des joueurs.
     *
     * @param avgElo l'Elo moyen
     */
    public void setAvgElo(double avgElo) {
        this.avgElo = avgElo;
    }

    /**
     * Retourne le nombre moyen de demi-coups pour atteindre la position.
     *
     * @return le nombre moyen de demi-coups
     */
    public double getAvgPly() {
        return avgPly;
    }

    /**
     * Définit le nombre moyen de demi-coups pour atteindre la position.
     *
     * @param avgPly le nombre moyen de demi-coups
     */
    public void setAvgPly(double avgPly) {
        this.avgPly = avgPly;
    }

}
