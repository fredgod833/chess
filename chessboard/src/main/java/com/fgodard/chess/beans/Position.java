package com.fgodard.chess.beans;

import com.fgodard.chess.board.Color;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by crios on 22/06/23.
 */
public class Position implements Serializable {

    // LLP position
    private String position = null;

    // Couleur du trait
    private Color turnColor;

    // Etat de l'échiquier (prise en passant, possibilites de roque...)
    private BoardState boardState = null;

    // Statistiques liées à la position
    private PosStat posStat = null;

    public String getPosition() {
        return position;
    }

    public void setPosition(final String position) {
        this.position = position;
    }

    public Color getTurnColor() {
        return turnColor;
    }

    public void setTurnColor(Color turnColor) {
        this.turnColor = turnColor;
    }  

    public Optional<PosStat> getPosStat() {
        return Optional.ofNullable(posStat);
    }

    public Optional<BoardState> getBoardState() {
        return Optional.ofNullable(boardState);
    }

    public void setBoardState(BoardState boardState) {
        this.boardState = boardState;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Position)) {
            return false;
        }
        Position pos = (Position) obj;
        if (this.position == null) {
            return pos.position == null;
        }
        if (!this.position.equals(pos.position)) {
            return false;
        }
        if (this.boardState == null) {
            return pos.boardState == null;
        }

        return this.boardState.equals(pos.boardState);
    }

    @Override
    public int hashCode() {
        if (this.position == null) {
            return 0;
        }
        return this.position.hashCode();
    }

    public void setPosStat(PosStat posStat) {
        this.posStat = posStat;
    }
}
