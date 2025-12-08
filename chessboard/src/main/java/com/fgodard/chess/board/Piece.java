package com.fgodard.chess.board;

import java.util.Collection;

/**
 * Created by crios on 23/04/23.
 */
public abstract class Piece {

    private Color color;

    private BoardCell cell;

    private GameBoard currentBoard;

    private char posSymbol;

    void setCurrentBoard(GameBoard currentBoard) {
        this.currentBoard = currentBoard;
    }

    protected void setColor(Color color) {
        this.color = color;
        if (color == Color.WHITE) {
            posSymbol = this.getSymbol();
        } else if (color == Color.BLACK) {
            posSymbol = (char) ((int) this.getSymbol() + 32);
        }
    }

    public GameBoard getCurrentBoard() {
        return currentBoard;
    }

    public Color getColor() {
        return this.color;
    }

    public void setCell(BoardCell cell) {
        this.cell = cell;
    }

    public BoardCell getCell() {
        return cell;
    }

    public char getPosSymbol() {
        return posSymbol;
    }

    public abstract Collection<BoardCell> getMoveCells();

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
