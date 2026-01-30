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
 * Created by crios on 23/04/23.
 */
public class Pawn extends Piece {

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

    @Override
    public char getSymbol() {
        return 'P';
    }

    private boolean addMoveDirection(Collection<BoardCell> result, int lineIncrement) {
        Optional<BoardCell> nextCell = Board.getCell(this.getCell(), 0, lineIncrement);
        if (nextCell.isPresent() && getCurrentBoard().getPiece(nextCell.get()) == null) {
            result.add(nextCell.get());
            return true;
        }
        return false;
    }

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
