package com.fgodard.chess.board.pieces;

import com.fgodard.chess.board.BoardCell;
import com.fgodard.chess.board.Color;
import com.fgodard.chess.board.MoveHelper;
import com.fgodard.chess.board.Piece;

import java.util.ArrayList;
import java.util.Collection;

import static com.fgodard.chess.board.MoveHelper.addRelativeElement;

/**
 * Created by crios on 23/04/23.
 */
public class King extends Piece {

    @Override
    public Collection<BoardCell> getMoveCells() {
        
        Collection<BoardCell> freeCells = new ArrayList<>();
        addRelativeElement(this, -1, -1, freeCells, null);
        addRelativeElement(this, -1, 0, freeCells, null);
        addRelativeElement(this, -1, 1, freeCells, null);
        addRelativeElement(this, 0, -1, freeCells, null);
        addRelativeElement(this, 0, 1, freeCells, null);
        addRelativeElement(this, 1, -1, freeCells, null);
        addRelativeElement(this, 1, 0, freeCells, null);
        addRelativeElement(this, 1, 1, freeCells, null);
        Collection<BoardCell> result = new ArrayList<>();
        Collection<Piece> attackingPieces;
        for (BoardCell cell: freeCells) {
            attackingPieces = MoveHelper.findAttackingPieces(this.getCurrentBoard(), cell, this.getColor(), "RBNQP");
            if (attackingPieces == null || attackingPieces.isEmpty()) {
                result.add(cell);
            }
        }
        return result;
    }

    @Override
    public char getSymbol() {
        return 'K';
    }

}
