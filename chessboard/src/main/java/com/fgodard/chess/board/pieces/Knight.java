package com.fgodard.chess.board.pieces;

import com.fgodard.chess.board.BoardCell;
import com.fgodard.chess.board.Piece;

import java.util.ArrayList;
import java.util.Collection;

import static com.fgodard.chess.board.MoveHelper.addRelativeElement;

/**
 * Created by crios on 23/04/23.
 */
public class Knight extends Piece {

    @Override
    public Collection<BoardCell> getMoveCells() {
        Collection<BoardCell> result = new ArrayList<>();
        addRelativeElement(this,1, 2, result, null);
        addRelativeElement(this,2, 1, result, null);
        addRelativeElement(this,2, -1, result, null);
        addRelativeElement(this,1, -2, result, null);
        addRelativeElement(this,-1, -2, result, null);
        addRelativeElement(this,-2, -1, result, null);
        addRelativeElement(this,-2, 1, result, null);
        addRelativeElement(this,-1, 2, result, null);
        return result;
    }

    @Override
    public char getSymbol() {
        return 'N';
    }

}
