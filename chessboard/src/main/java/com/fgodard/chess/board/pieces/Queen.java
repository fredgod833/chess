package com.fgodard.chess.board.pieces;

import com.fgodard.chess.board.BoardCell;
import com.fgodard.chess.board.MoveHelper;
import com.fgodard.chess.board.Piece;

import java.util.ArrayList;
import java.util.Collection;

import static com.fgodard.chess.board.MoveHelper.addElementsFromDirection;

/**
 * Created by crios on 23/04/23.
 */
public class Queen extends Piece {

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

    @Override
    public char getSymbol() {
        return 'Q';
    }

}
