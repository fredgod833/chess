package com.fgodard.chess.board.pieces;

import com.fgodard.chess.board.BoardCell;
import com.fgodard.chess.board.MoveHelper;
import com.fgodard.chess.board.Piece;

import java.util.ArrayList;
import java.util.Collection;

import static com.fgodard.chess.board.MoveHelper.addElementsFromDirection;

/**
 * Représente la dame (reine) aux échecs.
 *
 * <p>La dame est la pièce la plus puissante. Elle peut se déplacer
 * d'un nombre quelconque de cases dans toutes les directions :
 * horizontale, verticale et diagonale.
 *
 * <p>Elle combine les mouvements de la tour et du fou.
 *
 * @author crios
 * @see Piece
 * @see Rook
 * @see Bishop
 */
public class Queen extends Piece {

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<BoardCell> getMoveCells() {
        Collection<BoardCell> result = new ArrayList<>();
        addElementsFromDirection(this, -1, -1, result, null);
        addElementsFromDirection(this, -1, 0, result, null);
        addElementsFromDirection(this, -1, 1, result, null);
        addElementsFromDirection(this, 0, -1, result, null);
        addElementsFromDirection(this, 0, 1, result, null);
        addElementsFromDirection(this, 1, -1, result, null);
        addElementsFromDirection(this, 1, 0, result, null);
        addElementsFromDirection(this, 1, 1, result, null);
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @return 'Q' pour Queen (Dame)
     */
    @Override
    public char getSymbol() {
        return 'Q';
    }

}
