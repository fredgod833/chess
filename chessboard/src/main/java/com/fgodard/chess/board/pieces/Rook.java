package com.fgodard.chess.board.pieces;

import com.fgodard.chess.board.BoardCell;
import com.fgodard.chess.board.MoveHelper;
import com.fgodard.chess.board.Piece;

import java.util.ArrayList;
import java.util.Collection;

import static com.fgodard.chess.board.MoveHelper.addElementsFromDirection;

/**
 * Représente la tour aux échecs.
 *
 * <p>La tour peut se déplacer d'un nombre quelconque de cases
 * horizontalement ou verticalement.
 *
 * <p>La tour participe au roque avec le roi.
 *
 * @author crios
 * @see Piece
 */
public class Rook extends Piece {

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<BoardCell> getMoveCells() {
        Collection<BoardCell> result = new ArrayList<>();
        addElementsFromDirection(this, -1, 0, result, null);
        addElementsFromDirection(this, 0, -1, result, null);
        addElementsFromDirection(this, 0, 1, result, null);
        addElementsFromDirection(this, 1, 0, result, null);
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @return 'R' pour Rook (Tour)
     */
    @Override
    public char getSymbol() {
        return 'R';
    }
}
