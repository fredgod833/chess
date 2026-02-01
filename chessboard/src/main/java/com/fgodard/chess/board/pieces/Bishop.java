package com.fgodard.chess.board.pieces;

import com.fgodard.chess.board.BoardCell;
import com.fgodard.chess.board.MoveHelper;
import com.fgodard.chess.board.Piece;

import java.util.ArrayList;
import java.util.Collection;

import static com.fgodard.chess.board.MoveHelper.addElementsFromDirection;

/**
 * Représente le fou aux échecs.
 *
 * <p>Le fou peut se déplacer d'un nombre quelconque de cases
 * en diagonale. Il reste toujours sur des cases de la même couleur.
 *
 * @author crios
 * @see Piece
 */
public class Bishop extends Piece {

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<BoardCell> getMoveCells() {
        Collection<BoardCell> result = new ArrayList<>();
        MoveHelper.addElementsFromDirection(this, -1, -1, result,null);
        MoveHelper.addElementsFromDirection(this,-1, 1, result,null);
        MoveHelper.addElementsFromDirection(this,1, -1, result,null);
        MoveHelper.addElementsFromDirection(this,1, 1, result,null);
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @return 'B' pour Bishop (Fou)
     */
    @Override
    public char getSymbol() {
        return 'B';
    }

}
