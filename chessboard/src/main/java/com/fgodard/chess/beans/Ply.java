package com.fgodard.chess.beans;

import com.fgodard.chess.board.Color;

import java.io.Serializable;

/**
 * Created by crios on 29/04/23.
 */
public class Ply implements Serializable {

    private Color color;

    private Integer eloRange;

    private Integer yearRange;

    private boolean kingCastle;

    private boolean queenCastle;

    private char piece;

    private String origCell;

    private String destCell;

    private Character promotion;

    private boolean take;

    private boolean chess;

    private boolean mat;

    private boolean enPassant;

    private Position initialPosition;

    private Position finalPosition;

    public Integer getEloRange() {
        return eloRange;
    }

    public void setEloRange(Integer eloRange) {
        this.eloRange = eloRange;
    }

    public Integer getYearRange() {
        return yearRange;
    }

    public void setYearRange(Integer yearRange) {
        this.yearRange = yearRange;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public char getPiece() {
        return piece;
    }

    public void setPiece(char piece) {
        this.piece = piece;
    }

    public String getOrigCell() {
        return origCell;
    }

    public void setOrigCell(final String origCell) {
        this.origCell = origCell;
    }

    public String getDestCell() {
        return destCell;
    }

    public void setDestCell(final String destCell) {
        this.destCell = destCell;
    }

    public boolean isTake() {
        return take;
    }

    public void setTake(boolean take) {
        this.take = take;
    }

    public Character getPromotion() {
        return promotion;
    }

    public void setPromotion(Character promotion) {
        this.promotion = promotion;
    }

    public boolean isKingCastle() {
        return kingCastle;
    }

    public void setKingCastle(boolean kingCastle) {
        this.kingCastle = kingCastle;
    }

    public boolean isQueenCastle() {
        return queenCastle;
    }

    public void setQueenCastle(boolean queenCastle) {
        this.queenCastle = queenCastle;
    }

    public Position getInitialPosition() {
        return initialPosition;
    }

    public void setInitialPosition(Position initialPosition) {
        this.initialPosition = initialPosition;
    }

    public Position getFinalPosition() {
        return finalPosition;
    }

    public void setFinalPosition(Position finalPosition) {
        this.finalPosition = finalPosition;
    }

    public boolean isChess() {
        return chess;
    }

    public void setChess(boolean chess) {
        this.chess = chess;
    }

    public boolean isMat() {
        return mat;
    }

    public void setMat(boolean mat) {
        this.mat = mat;
    }

    public boolean isEnPassant() {
        return enPassant;
    }

    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    private boolean equals(final String s1, final String s2) {
        if (s1 == null) {
            return s2 == null;
        }

        if (s2 == null) {
            return false;
        }

        return s1.equals(s2);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Ply)) {
            return false;
        }

        Ply p = (Ply) obj;
        if (p.isKingCastle() != kingCastle) {
            return false;
        }

        if (p.isQueenCastle() != queenCastle) {
            return false;
        }

        if (p.getPiece() != piece) {
            return false;
        }

        if (!equals(p.getOrigCell(), origCell)) {
            return false;
        }

        if (!equals(p.getDestCell(), destCell)) {
            return false;
        }

        if (!equals(String.valueOf(p.getPromotion()), String.valueOf(promotion))) {
            return false;
        }

        return true;

    }

    @Override
    public int hashCode() {
        if (kingCastle) {
            return 1;
        }
        if (queenCastle) {
            return 2;
        }
        return ((int) piece) + 62 + origCell.hashCode() + destCell.hashCode();
    }

}
