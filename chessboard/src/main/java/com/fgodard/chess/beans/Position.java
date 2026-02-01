package com.fgodard.chess.beans;

import com.fgodard.chess.board.Color;

import java.io.Serializable;
import java.util.Optional;

/**
 * Représente une position d'échecs avec son contexte.
 *
 * <p>Cette classe encapsule :
 * <ul>
 *   <li>La position des pièces au format LLP</li>
 *   <li>La couleur du joueur ayant le trait</li>
 *   <li>L'état de l'échiquier (droits de roque, case en passant)</li>
 *   <li>Les statistiques optionnelles liées à la position</li>
 * </ul>
 *
 * @author crios
 * @see BoardState
 * @see PosStat
 */
public class Position implements Serializable {

    /** Position des pièces au format LLP */
    private String position = null;

    /** Couleur du joueur ayant le trait */
    private Color turnColor;

    /** État de l'échiquier (prise en passant, possibilités de roque) */
    private BoardState boardState = null;

    /** Statistiques liées à la position */
    private PosStat posStat = null;

    /**
     * Retourne la position des pièces au format LLP.
     *
     * @return la chaîne LLP
     */
    public String getPosition() {
        return position;
    }

    /**
     * Définit la position des pièces au format LLP.
     *
     * @param position la chaîne LLP
     */
    public void setPosition(final String position) {
        this.position = position;
    }

    /**
     * Retourne la couleur du joueur ayant le trait.
     *
     * @return la couleur (blanc ou noir)
     */
    public Color getTurnColor() {
        return turnColor;
    }

    /**
     * Définit la couleur du joueur ayant le trait.
     *
     * @param turnColor la couleur
     */
    public void setTurnColor(Color turnColor) {
        this.turnColor = turnColor;
    }

    /**
     * Retourne les statistiques associées à la position.
     *
     * @return un Optional contenant les statistiques, ou vide si non disponibles
     */
    public Optional<PosStat> getPosStat() {
        return Optional.ofNullable(posStat);
    }

    /**
     * Retourne l'état de l'échiquier.
     *
     * @return un Optional contenant l'état, ou vide si non défini
     */
    public Optional<BoardState> getBoardState() {
        return Optional.ofNullable(boardState);
    }

    /**
     * Définit l'état de l'échiquier.
     *
     * @param boardState l'état de l'échiquier
     */
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

    /**
     * Définit les statistiques associées à la position.
     *
     * @param posStat les statistiques
     */
    public void setPosStat(PosStat posStat) {
        this.posStat = posStat;
    }
}
