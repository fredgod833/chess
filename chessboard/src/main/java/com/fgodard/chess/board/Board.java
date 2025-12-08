package com.fgodard.chess.board;

import com.fgodard.chess.exception.InvalidCellException;

import java.util.Optional;

/**
 * Created by crios on 22/04/23.
 */
public class Board {

    /**
     * Private Constructor
     */
    private Board() {

    }

    private static String getCellName(int colIdx, int lineIdx) {
        char[] result = new char[2];
        result[0] = (char)('A' + colIdx);
        result[1] = (char)('1' + lineIdx);
        return String.valueOf(result);
    }

    public static Optional<BoardCell> getCell(int colIdx, int lineIdx) {
        if (colIdx < 0 || colIdx > 7) {
            return Optional.empty();
        }
        if (lineIdx < 0 || lineIdx > 7) {
            return Optional.empty();
        }

        return Optional.of(BoardCell.valueOf(getCellName(colIdx,lineIdx)));
    }

    public static BoardCell getCell(final String alpha) throws InvalidCellException {
        int line;
        try {
            line = Integer.parseInt(alpha.substring(1, 2)) - 1;
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new InvalidCellException(e, "Numéro de ligne invalide (%s).", alpha);
        }

        int col;
        try {
            col = alpha.charAt(0) - 97;
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidCellException(e, "Lettre de colonne invalide (%s).", alpha);
        }

        return BoardCell.values()[col + line * 8];
    }

    /**
     * Retourne une celulle relative à la cellulle de base avec un incrément
     * retourne null si la cellule est hors echiquier.
     *
     * @param cell
     * @param colIncrement
     * @param lineIncrement
     * @return
     */
    public static Optional<BoardCell> getCell(BoardCell cell, int colIncrement, int lineIncrement) {
        int col = cell.getColIdx() + colIncrement;
        int line = cell.getLineIdx() + lineIncrement;
        return getCell(col, line);
    }

}
