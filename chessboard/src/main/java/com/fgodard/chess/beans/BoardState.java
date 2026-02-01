package com.fgodard.chess.beans;

import java.io.Serializable;

/**
 * Représente l'état contextuel d'une position d'échecs.
 *
 * <p>Cette classe stocke les informations qui ne sont pas déductibles
 * de la seule position des pièces :
 * <ul>
 *   <li>La case de prise en passant possible</li>
 *   <li>Les droits de roque pour chaque camp</li>
 * </ul>
 *
 * <p>Ces informations sont nécessaires pour la notation FEN complète
 * et pour la validation des coups légaux.
 *
 * @author crios
 * @see Position
 */
public class BoardState implements Serializable {

    /** Case de prise en passant (notation algébrique), ou null si aucune */
    private String epCell;

    /** Indique si les blancs peuvent faire le petit roque */
    private Boolean whiteCanCastleKingSide;

    /** Indique si les blancs peuvent faire le grand roque */
    private Boolean whiteCanCastleQueenSide;

    /** Indique si les noirs peuvent faire le petit roque */
    private Boolean blackCanCastleKingSide;

    /** Indique si les noirs peuvent faire le grand roque */
    private Boolean blackCanCastleQueenSide;

    /**
     * Retourne la case de prise en passant.
     *
     * @return la notation algébrique de la case (ex: "e3"), ou {@code null} si aucune
     */
    public String getEpCell() {
        return epCell;
    }

    /**
     * Définit la case de prise en passant.
     *
     * @param epCell la notation algébrique de la case
     */
    public void setEpCell(final String epCell) {
        this.epCell = epCell;
    }

    /**
     * Indique si les blancs peuvent faire le petit roque.
     *
     * @return {@code true} si le petit roque blanc est possible
     */
    public Boolean getWhiteCanCastleKingSide() {
        return whiteCanCastleKingSide;
    }

    /**
     * Définit si les blancs peuvent faire le petit roque.
     *
     * @param whiteCanCastleKingSide {@code true} si possible
     */
    public void setWhiteCanCastleKingSide(Boolean whiteCanCastleKingSide) {
        this.whiteCanCastleKingSide = whiteCanCastleKingSide;
    }

    /**
     * Indique si les blancs peuvent faire le grand roque.
     *
     * @return {@code true} si le grand roque blanc est possible
     */
    public Boolean getWhiteCanCastleQueenSide() {
        return whiteCanCastleQueenSide;
    }

    /**
     * Définit si les blancs peuvent faire le grand roque.
     *
     * @param whiteCanCastleQueenSide {@code true} si possible
     */
    public void setWhiteCanCastleQueenSide(Boolean whiteCanCastleQueenSide) {
        this.whiteCanCastleQueenSide = whiteCanCastleQueenSide;
    }

    /**
     * Indique si les noirs peuvent faire le petit roque.
     *
     * @return {@code true} si le petit roque noir est possible
     */
    public Boolean getBlackCanCastleKingSide() {
        return blackCanCastleKingSide;
    }

    /**
     * Définit si les noirs peuvent faire le petit roque.
     *
     * @param blackCanCastleKingSide {@code true} si possible
     */
    public void setBlackCanCastleKingSide(Boolean blackCanCastleKingSide) {
        this.blackCanCastleKingSide = blackCanCastleKingSide;
    }

    /**
     * Indique si les noirs peuvent faire le grand roque.
     *
     * @return {@code true} si le grand roque noir est possible
     */
    public Boolean getBlackCanCastleQueenSide() {
        return blackCanCastleQueenSide;
    }

    /**
     * Définit si les noirs peuvent faire le grand roque.
     *
     * @param blackCanCastleQueenSide {@code true} si possible
     */
    public void setBlackCanCastleQueenSide(Boolean blackCanCastleQueenSide) {
        this.blackCanCastleQueenSide = blackCanCastleQueenSide;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BoardState)) {
            return false;
        }

        BoardState state = (BoardState) obj;
        if ((this.epCell == null) != (state.epCell == null)) {
            return false;
        }

        if (this.epCell != null && !this.epCell.equals(state.epCell)) {
            return false;
        }

        return this.whiteCanCastleKingSide != state.whiteCanCastleKingSide
                && this.whiteCanCastleQueenSide != state.whiteCanCastleQueenSide
                && this.blackCanCastleKingSide != state.blackCanCastleKingSide
                && this.blackCanCastleQueenSide == state.blackCanCastleQueenSide;
    }

    @Override
    public int hashCode() {
        int result = epCell == null ? 0 : epCell.hashCode();
        result += whiteCanCastleKingSide != null && whiteCanCastleKingSide ? 2048 : 0;
        result += whiteCanCastleQueenSide != null && whiteCanCastleQueenSide ? 4096 : 0;
        result += blackCanCastleKingSide != null && blackCanCastleKingSide ? 8192 : 0;
        result += blackCanCastleQueenSide != null && blackCanCastleQueenSide ? 16384 : 0;
        return result;
    }

}
