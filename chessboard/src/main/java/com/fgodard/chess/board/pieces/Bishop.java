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
public class Bishop extends Piece {

    @Override
    public Collection<BoardCell> getMoveCells() {
        Collection<BoardCell> result = new ArrayList<>();
        MoveHelper.addElementsFromDirection(this, -1, -1, result,null);
        MoveHelper.addElementsFromDirection(this,-1, 1, result,null);
        MoveHelper.addElementsFromDirection(this,1, -1, result,null);
        MoveHelper.addElementsFromDirection(this,1, 1, result,null);
        return result;
    }

    @Override
    public char getSymbol() {
        return 'B';
    }

}
