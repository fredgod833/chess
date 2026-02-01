package com.fgodard.chess.board;

import java.util.Collection;

/**
 * Classe abstraite représentant une pièce d'échecs.
 *
 * <p>Cette classe définit les propriétés communes à toutes les pièces :
 * <ul>
 *   <li>La couleur (blanc ou noir)</li>
 *   <li>La case actuelle sur l'échiquier</li>
 *   <li>La référence vers l'échiquier courant</li>
 *   <li>Le symbole de position (majuscule pour blanc, minuscule pour noir)</li>
 * </ul>
 *
 * <p>Chaque type de pièce (Roi, Dame, Tour, Fou, Cavalier, Pion) doit implémenter
 * les méthodes abstraites pour définir son symbole et calculer ses cases de destination.
 *
 * @author crios
 * @see com.fgodard.chess.board.pieces.King
 * @see com.fgodard.chess.board.pieces.Queen
 * @see com.fgodard.chess.board.pieces.Rook
 * @see com.fgodard.chess.board.pieces.Bishop
 * @see com.fgodard.chess.board.pieces.Knight
 * @see com.fgodard.chess.board.pieces.Pawn
 */
public abstract class Piece {

    private Color color;

    private BoardCell cell;

    private GameBoard currentBoard;

    private char posSymbol;

    /**
     * Définit l'échiquier sur lequel se trouve cette pièce.
     *
     * @param currentBoard l'échiquier courant
     */
    void setCurrentBoard(GameBoard currentBoard) {
        this.currentBoard = currentBoard;
    }

    /**
     * Définit la couleur de la pièce.
     *
     * <p>Le symbole de position est automatiquement mis à jour :
     * majuscule pour les blancs, minuscule pour les noirs.
     *
     * @param color la couleur de la pièce
     */
    protected void setColor(Color color) {
        this.color = color;
        if (color == Color.WHITE) {
            posSymbol = this.getSymbol();
        } else if (color == Color.BLACK) {
            posSymbol = (char) ((int) this.getSymbol() + 32);
        }
    }

    /**
     * Retourne l'échiquier sur lequel se trouve cette pièce.
     *
     * @return l'échiquier courant
     */
    public GameBoard getCurrentBoard() {
        return currentBoard;
    }

    /**
     * Retourne la couleur de la pièce.
     *
     * @return la couleur (blanc ou noir)
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Définit la case sur laquelle se trouve la pièce.
     *
     * @param cell la case de l'échiquier
     */
    public void setCell(BoardCell cell) {
        this.cell = cell;
    }

    /**
     * Retourne la case sur laquelle se trouve la pièce.
     *
     * @return la case actuelle
     */
    public BoardCell getCell() {
        return cell;
    }

    /**
     * Retourne le symbole de position de la pièce.
     *
     * <p>Le symbole est en majuscule pour les pièces blanches (ex: K, Q, R, B, N, P)
     * et en minuscule pour les pièces noires (ex: k, q, r, b, n, p).
     *
     * @return le symbole de position
     */
    public char getPosSymbol() {
        return posSymbol;
    }

    /**
     * Calcule et retourne les cases où cette pièce peut se déplacer.
     *
     * <p>Cette méthode prend en compte :
     * <ul>
     *   <li>Les règles de déplacement spécifiques à chaque type de pièce</li>
     *   <li>Les pièces amies qui bloquent le passage</li>
     *   <li>Les pièces adverses qui peuvent être prises</li>
     * </ul>
     *
     * @return la collection des cases de destination possibles
     */
    public abstract Collection<BoardCell> getMoveCells();

    /**
     * Retourne le symbole de la pièce en notation algébrique.
     *
     * <p>Les symboles sont : K (Roi), Q (Dame), R (Tour), B (Fou), N (Cavalier), P (Pion).
     *
     * @return le symbole de la pièce (toujours en majuscule)
     */
    public abstract char getSymbol();

    @Override
    public String toString() {
        if (cell != null) {
            return String.valueOf(getSymbol()).concat(cell.getAlgebricPos());
        }
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!o.getClass().equals(this.getClass())) {
            return false;
        }
        Piece p = (Piece) o;
        if (this.getColor() != p.getColor()) {
            return false;
        }
        if (!this.getCell().equals(p.getCell())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (int) getPosSymbol();
    }

}
