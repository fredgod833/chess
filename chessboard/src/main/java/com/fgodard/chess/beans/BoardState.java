package com.fgodard.chess.beans;

import java.io.Serializable;

/**
 * Created by crios on 18/11/23.
 */
public class BoardState implements Serializable {

    // case de prise en passant
    private String epCell;

    private Boolean whiteCanCastleKingSide;

    private Boolean whiteCanCastleQueenSide;

    private Boolean blackCanCastleKingSide;

    private Boolean blackCanCastleQueenSide;

    public String getEpCell() {
        return epCell;
    }

    public void setEpCell(final String epCell) {
        this.epCell = epCell;
    }

    public Boolean getWhiteCanCastleKingSide() {
        return whiteCanCastleKingSide;
    }

    public void setWhiteCanCastleKingSide(Boolean whiteCanCastleKingSide) {
        this.whiteCanCastleKingSide = whiteCanCastleKingSide;
    }

    public Boolean getWhiteCanCastleQueenSide() {
        return whiteCanCastleQueenSide;
    }

    public void setWhiteCanCastleQueenSide(Boolean whiteCanCastleQueenSide) {
        this.whiteCanCastleQueenSide = whiteCanCastleQueenSide;
    }

    public Boolean getBlackCanCastleKingSide() {
        return blackCanCastleKingSide;
    }

    public void setBlackCanCastleKingSide(Boolean blackCanCastleKingSide) {
        this.blackCanCastleKingSide = blackCanCastleKingSide;
    }

    public Boolean getBlackCanCastleQueenSide() {
        return blackCanCastleQueenSide;
    }

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
