package com.fgodard.chess.board.pieces;

import com.fgodard.chess.board.GameBoard;
import com.fgodard.chess.board.Piece;
import com.fgodard.chess.board.Color;
import com.fgodard.chess.board.BoardCell;
import com.fgodard.chess.board.Board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Représente le pion aux échecs.
 *
 * <p>Le pion a des règles de déplacement uniques :
 * <ul>
 *   <li>Il avance d'une case vers l'avant (ou deux cases depuis sa position initiale)</li>
 *   <li>Il capture en diagonale</li>
 *   <li>Il peut effectuer une prise en passant</li>
 *   <li>Il se promeut en atteignant la dernière rangée</li>
 * </ul>
 *
 * <p>Les pions blancs avancent vers les rangées supérieures (1→8),
 * les pions noirs vers les rangées inférieures (8→1).
 *
 * @author crios
 * @see Piece
 */
public class Pawn extends Piece {

    /**
     * {@inheritDoc}
     *
     * <p>Pour le pion, les cases de destination incluent :
     * <ul>
     *   <li>La case devant (si vide)</li>
     *   <li>La case deux cases devant (si depuis la position initiale et chemin libre)</li>
     *   <li>Les cases diagonales avant (si occupation adverse ou prise en passant)</li>
     * </ul>
     */
    @Override
    public Collection<BoardCell> getMoveCells() {

        Collection<BoardCell> result = new ArrayList<>();
        if (this.getColor() == Color.BLACK) {
            if (addMoveDirection(result, -1) && getCell().getLine() == 7) {
                addMoveDirection(result, -2);
            }
            addTakeDirection(result, -1, -1);
            addTakeDirection(result, 1, -1);
        }

        if (this.getColor() == Color.WHITE) {
            if (addMoveDirection(result, 1) && getCell().getLine() == 2) {
                addMoveDirection(result, 2);
            }
            addTakeDirection(result, -1, 1);
            addTakeDirection(result, 1, 1);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @return 'P' pour Pawn (Pion)
     */
    @Override
    public char getSymbol() {
        return 'P';
    }

    /**
     * Ajoute une case de déplacement avant si elle est libre.
     *
     * @param result la collection où ajouter la case
     * @param lineIncrement l'incrément de ligne (+1 pour blanc, -1 pour noir)
     * @return {@code true} si la case a été ajoutée (libre), {@code false} sinon
     */
    private boolean addMoveDirection(Collection<BoardCell> result, int lineIncrement) {
        Optional<BoardCell> nextCell = Board.getCell(this.getCell(), 0, lineIncrement);
        if (nextCell.isPresent() && getCurrentBoard().getPiece(nextCell.get()) == null) {
            result.add(nextCell.get());
            return true;
        }
        return false;
    }

    /**
     * Ajoute une case de prise diagonale si elle contient une pièce adverse ou permet la prise en passant.
     *
     * @param result la collection où ajouter la case
     * @param colIncrement l'incrément de colonne (-1 ou +1)
     * @param lineIncrement l'incrément de ligne (+1 pour blanc, -1 pour noir)
     */
    private void addTakeDirection(Collection<BoardCell> result, int colIncrement, int lineIncrement) {

        Optional<BoardCell> nextCell = Board.getCell(this.getCell(), colIncrement, lineIncrement);
        GameBoard board = getCurrentBoard();
        if (!nextCell.isPresent()) {
            return;
        }

        if (nextCell.get() == board.getEnPassantCell()) {
            result.add(nextCell.get());
            return;
        }

        Piece piece = board.getPiece(nextCell.get());
        if (piece != null && piece.getColor() != this.getColor()) {
            result.add(nextCell.get());
        }

    }

}
