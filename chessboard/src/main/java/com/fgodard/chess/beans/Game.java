package com.fgodard.chess.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une partie d'échecs avec ses métadonnées et ses coups.
 * <p>
 * Cette classe stocke les informations d'une partie au format PGN :
 * <ul>
 *     <li>Informations sur l'événement (nom, lieu, date)</li>
 *     <li>Informations sur les joueurs (noms, classements Elo)</li>
 *     <li>Position de départ en notation FEN</li>
 *     <li>Liste des coups joués ({@link Ply})</li>
 *     <li>Résultat de la partie</li>
 * </ul>
 * </p>
 *
 * @author crios
 * @since 29/04/23
 * @see Ply
 * @see Position
 */
public class Game implements Serializable {

    public static final String FEN_STD_START_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private String eventName;
    private String site;
    private String whitePlayerName;
    private Integer whiteElo;
    private String blackPlayerName;
    private Integer blackElo;
    private Integer whiteEloRange;
    private Integer blackEloRange;
    private Integer year;
    private Integer yearRange;
    private Integer month;
    private Integer day;
    private Integer result;
    private String ecoCode;
    private String startFENPosition = FEN_STD_START_POSITION;
    private String pgnHeaders;
    private String pgnBody;
    private ArrayList<String> posList = new ArrayList<>();
    private ArrayList<Ply> plies = new ArrayList<>();

    public String getStartFENPosition() {
        return startFENPosition;
    }

    public void setStartFENPosition(String startFENPosition) {
        this.startFENPosition = startFENPosition;
    }

    public String getEcoCode() {
        return ecoCode;
    }

    public void setEcoCode(String ecoCode) {
        this.ecoCode = ecoCode;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getWhitePlayerName() {
        return whitePlayerName;
    }

    public void setWhitePlayerName(String whitePlayerName) {
        this.whitePlayerName = whitePlayerName;
    }

    public Integer getWhiteElo() {
        return whiteElo;
    }

    public void setWhiteElo(Integer whiteElo) {
        this.whiteElo = whiteElo;
        if (whiteElo == null) {
            whiteEloRange = null;
        } else {
            whiteEloRange = Math.round(whiteElo/100.0f) - 10;
        }
    }

    public String getBlackPlayerName() {
        return blackPlayerName;
    }

    public void setBlackPlayerName(String blackPlayerName) {
        this.blackPlayerName = blackPlayerName;
    }

    public Integer getBlackElo() {
        return blackElo;
    }

    public void setBlackElo(Integer blackElo) {
        this.blackElo = blackElo;
        if (blackElo == null) {
            blackEloRange = null;
        } else {
            blackEloRange = Math.round(blackElo/100.0f) - 10;
        }
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
        if (this.year == null) {
            yearRange = null;
        } else {
            if (year<1800) {
                yearRange = 8;
            } else if (year<1900) {
                yearRange = 9;
            } else if (year<1940) {
                yearRange = 10;
            } else if (year<1965) {
                yearRange = 11;
            } else if (year<1980) {
                yearRange = 12;
            } else if (year<1990) {
                yearRange = 13;
            } else if (year<1995) {
                yearRange = 14;
            } else if (year<2000) {
                yearRange = 15;
            } else if (year<2004) {
                yearRange = 16;
            } else if (year<2006) {
                yearRange = 17;
            } else if (year<2008) {
                yearRange = 18;
            } else if (year<2010) {
                yearRange = 19;
            } else {
                yearRange = year - 1990;
            }
        }
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getPgnHeaders() {
        return pgnHeaders;
    }

    public void setPgnHeaders(String pgnHeaders) {
        this.pgnHeaders = pgnHeaders;
    }

    public String getPgnBody() {
        return pgnBody;
    }

    public void setPgnBody(String pgnBody) {
        this.pgnBody = pgnBody;
    }

    /**
     * Ajoute un nouveau coup à la partie.
     * <p>
     * Met à jour la liste des coups et la liste des positions.
     * Si c'est le premier coup, la position initiale est également ajoutée.
     * </p>
     *
     * @param ply le coup à ajouter (ignoré si null)
     */
    public void addNewMove(Ply ply) {

        if (ply == null) {
            return;
        }

        plies.add(ply);

        if (posList.isEmpty()) {
            posList.add(ply.getInitialPosition().getPosition());
        }

        posList.add(ply.getFinalPosition().getPosition());

    }

    /**
     * Retourne la liste des coups de la partie.
     *
     * @return la liste des coups ({@link Ply})
     */
    public List<Ply> getPlies(){
        return plies;
    }

    /**
     * Réinitialise la liste des coups de la partie.
     */
    public void resetMoves() {
        plies.clear();
    }

    public Integer getWhiteEloRange() {
        return whiteEloRange;
    }

    public Integer getBlackEloRange() {
        return blackEloRange;
    }

    public Integer getYearRange() {
        return yearRange;
    }

    public List<String> getPosList() {
        return posList;
    }

    public void setPosList(List<String> posList) {
        this.posList.clear();;
        this.posList.addAll(posList);
    }

}
