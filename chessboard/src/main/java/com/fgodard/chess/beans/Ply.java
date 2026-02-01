package com.fgodard.chess.beans;

import com.fgodard.chess.board.Color;

import java.io.Serializable;

/**
 * Représente un demi-coup (ply) dans une partie d'échecs.
 *
 * <p>Un demi-coup correspond au déplacement d'un seul joueur.
 * Deux demi-coups forment un coup complet (blanc puis noir).
 *
 * <p>Cette classe contient toutes les informations nécessaires pour décrire un coup :
 * <ul>
 *   <li>La pièce déplacée et sa couleur</li>
 *   <li>Les cases d'origine et de destination</li>
 *   <li>Les indicateurs spéciaux (prise, roque, promotion, en passant)</li>
 *   <li>Les indicateurs d'échec et de mat</li>
 *   <li>Les positions avant et après le coup</li>
 * </ul>
 *
 * @author crios
 * @see Position
 */
public class Ply implements Serializable {

    /** Couleur du joueur qui effectue le coup */
    private Color color;

    /** Tranche de classement Elo associée au coup (pour statistiques) */
    private Integer eloRange;

    /** Tranche d'année associée au coup (pour statistiques) */
    private Integer yearRange;

    /** Indique si le coup est un petit roque (O-O) */
    private boolean kingCastle;

    /** Indique si le coup est un grand roque (O-O-O) */
    private boolean queenCastle;

    /** Symbole de la pièce déplacée (K, Q, R, B, N, P) */
    private char piece;

    /** Case d'origine en notation algébrique (ex: "e2") */
    private String origCell;

    /** Case de destination en notation algébrique (ex: "e4") */
    private String destCell;

    /** Pièce de promotion (Q, R, B, N) ou null si pas de promotion */
    private Character promotion;

    /** Indique si le coup est une prise */
    private boolean take;

    /** Indique si le coup met le roi adverse en échec */
    private boolean check;

    /** Indique si le coup est un mat */
    private boolean mate;

    /** Indique si le coup est une prise en passant */
    private boolean enPassant;

    /** Position avant le coup */
    private Position initialPosition;

    /** Position après le coup */
    private Position finalPosition;

    /**
     * Retourne la tranche de classement Elo associée au coup.
     *
     * @return la tranche Elo, ou {@code null} si non définie
     */
    public Integer getEloRange() {
        return eloRange;
    }

    /**
     * Définit la tranche de classement Elo associée au coup.
     *
     * @param eloRange la tranche Elo
     */
    public void setEloRange(Integer eloRange) {
        this.eloRange = eloRange;
    }

    /**
     * Retourne la tranche d'année associée au coup.
     *
     * @return la tranche d'année, ou {@code null} si non définie
     */
    public Integer getYearRange() {
        return yearRange;
    }

    /**
     * Définit la tranche d'année associée au coup.
     *
     * @param yearRange la tranche d'année
     */
    public void setYearRange(Integer yearRange) {
        this.yearRange = yearRange;
    }

    /**
     * Retourne la couleur du joueur qui effectue le coup.
     *
     * @return la couleur (blanc ou noir)
     */
    public Color getColor() {
        return color;
    }

    /**
     * Définit la couleur du joueur qui effectue le coup.
     *
     * @param color la couleur
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Retourne le symbole de la pièce déplacée.
     *
     * @return le symbole (K, Q, R, B, N ou P)
     */
    public char getPiece() {
        return piece;
    }

    /**
     * Définit le symbole de la pièce déplacée.
     *
     * @param piece le symbole de la pièce
     */
    public void setPiece(char piece) {
        this.piece = piece;
    }

    /**
     * Retourne la case d'origine du coup.
     *
     * @return la notation algébrique de la case d'origine (ex: "e2")
     */
    public String getOrigCell() {
        return origCell;
    }

    /**
     * Définit la case d'origine du coup.
     *
     * @param origCell la notation algébrique de la case
     */
    public void setOrigCell(final String origCell) {
        this.origCell = origCell;
    }

    /**
     * Retourne la case de destination du coup.
     *
     * @return la notation algébrique de la case de destination (ex: "e4")
     */
    public String getDestCell() {
        return destCell;
    }

    /**
     * Définit la case de destination du coup.
     *
     * @param destCell la notation algébrique de la case
     */
    public void setDestCell(final String destCell) {
        this.destCell = destCell;
    }

    /**
     * Indique si le coup est une prise.
     *
     * @return {@code true} si une pièce adverse est capturée
     */
    public boolean isTake() {
        return take;
    }

    /**
     * Définit si le coup est une prise.
     *
     * @param take {@code true} si prise
     */
    public void setTake(boolean take) {
        this.take = take;
    }

    /**
     * Retourne la pièce de promotion.
     *
     * @return le symbole de la pièce de promotion (Q, R, B, N), ou {@code null} si pas de promotion
     */
    public Character getPromotion() {
        return promotion;
    }

    /**
     * Définit la pièce de promotion.
     *
     * @param promotion le symbole de la pièce
     */
    public void setPromotion(Character promotion) {
        this.promotion = promotion;
    }

    /**
     * Indique si le coup est un petit roque (O-O).
     *
     * @return {@code true} si petit roque
     */
    public boolean isKingCastle() {
        return kingCastle;
    }

    /**
     * Définit si le coup est un petit roque.
     *
     * @param kingCastle {@code true} si petit roque
     */
    public void setKingCastle(boolean kingCastle) {
        this.kingCastle = kingCastle;
    }

    /**
     * Indique si le coup est un grand roque (O-O-O).
     *
     * @return {@code true} si grand roque
     */
    public boolean isQueenCastle() {
        return queenCastle;
    }

    /**
     * Définit si le coup est un grand roque.
     *
     * @param queenCastle {@code true} si grand roque
     */
    public void setQueenCastle(boolean queenCastle) {
        this.queenCastle = queenCastle;
    }

    /**
     * Retourne la position avant le coup.
     *
     * @return la position initiale
     */
    public Position getInitialPosition() {
        return initialPosition;
    }

    /**
     * Définit la position avant le coup.
     *
     * @param initialPosition la position initiale
     */
    public void setInitialPosition(Position initialPosition) {
        this.initialPosition = initialPosition;
    }

    /**
     * Retourne la position après le coup.
     *
     * @return la position finale
     */
    public Position getFinalPosition() {
        return finalPosition;
    }

    /**
     * Définit la position après le coup.
     *
     * @param finalPosition la position finale
     */
    public void setFinalPosition(Position finalPosition) {
        this.finalPosition = finalPosition;
    }

    /**
     * Indique si le coup met le roi adverse en échec.
     *
     * @return {@code true} si échec
     */
    public boolean isCheck() {
        return check;
    }

    /**
     * Définit si le coup met le roi adverse en échec.
     *
     * @param check {@code true} si échec
     */
    public void setCheck(boolean check) {
        this.check = check;
    }

    /**
     * Indique si le coup est un mat.
     *
     * @return {@code true} si mat
     */
    public boolean isMate() {
        return mate;
    }

    /**
     * Définit si le coup est un mat.
     *
     * @param mate {@code true} si mat
     */
    public void setMate(boolean mate) {
        this.mate = mate;
    }

    /**
     * Indique si le coup est une prise en passant.
     *
     * @return {@code true} si prise en passant
     */
    public boolean isEnPassant() {
        return enPassant;
    }

    /**
     * Définit si le coup est une prise en passant.
     *
     * @param enPassant {@code true} si prise en passant
     */
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
