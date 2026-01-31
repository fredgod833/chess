package com.fgodard.chess;

import com.fgodard.chess.board.Board;
import com.fgodard.chess.board.BoardCell;
import com.fgodard.chess.board.BoardColor;
import com.fgodard.chess.exception.InvalidCellException;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class BoardCellTest {

    @Test
    public void testGetCellByCoordinatesValid() {
        Optional<BoardCell> cell = Board.getCell(0, 0);
        assertTrue(cell.isPresent());
        assertEquals(BoardCell.A1, cell.get());

        cell = Board.getCell(4, 3);
        assertTrue(cell.isPresent());
        assertEquals(BoardCell.E4, cell.get());

        cell = Board.getCell(7, 7);
        assertTrue(cell.isPresent());
        assertEquals(BoardCell.H8, cell.get());
    }

    @Test
    public void testGetCellByCoordinatesInvalid() {
        Optional<BoardCell> cell = Board.getCell(-1, 0);
        assertFalse(cell.isPresent());

        cell = Board.getCell(0, -1);
        assertFalse(cell.isPresent());

        cell = Board.getCell(8, 0);
        assertFalse(cell.isPresent());

        cell = Board.getCell(0, 8);
        assertFalse(cell.isPresent());
    }

    @Test
    public void testGetCellByAlgebraicNotation() throws InvalidCellException {
        BoardCell cell = Board.getCell("e4");
        assertEquals(BoardCell.E4, cell);

        cell = Board.getCell("a1");
        assertEquals(BoardCell.A1, cell);

        cell = Board.getCell("h8");
        assertEquals(BoardCell.H8, cell);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetCellByInvalidAlgebraicNotation() throws InvalidCellException {
        Board.getCell("i9");
    }

    @Test(expected = InvalidCellException.class)
    public void testGetCellByEmptyString() throws InvalidCellException {
        Board.getCell("");
    }

    @Test
    public void testGetAlgebricPos() {
        assertEquals("e4", BoardCell.E4.getAlgebricPos());
        assertEquals("a1", BoardCell.A1.getAlgebricPos());
        assertEquals("h8", BoardCell.H8.getAlgebricPos());
        assertEquals("d5", BoardCell.D5.getAlgebricPos());
    }

    @Test
    public void testGetColor() {
        assertEquals(BoardColor.BLACK, BoardCell.A1.getColor());
        assertEquals(BoardColor.WHITE, BoardCell.B1.getColor());
        assertEquals(BoardColor.WHITE, BoardCell.A2.getColor());
        assertEquals(BoardColor.BLACK, BoardCell.B2.getColor());
        assertEquals(BoardColor.WHITE, BoardCell.E4.getColor());
        assertEquals(BoardColor.BLACK, BoardCell.D4.getColor());
    }

    @Test
    public void testGetColAndLine() {
        assertEquals('e', BoardCell.E4.getCol());
        assertEquals(4, BoardCell.E4.getLine());

        assertEquals('a', BoardCell.A1.getCol());
        assertEquals(1, BoardCell.A1.getLine());

        assertEquals('h', BoardCell.H8.getCol());
        assertEquals(8, BoardCell.H8.getLine());
    }

    @Test
    public void testGetIdx() {
        assertEquals(0, BoardCell.A1.getIdx());
        assertEquals(7, BoardCell.H1.getIdx());
        assertEquals(8, BoardCell.A2.getIdx());
        assertEquals(63, BoardCell.H8.getIdx());
    }

    @Test
    public void testRelativeCellNavigation() {
        Optional<BoardCell> cell = Board.getCell(BoardCell.E4, 1, 1);
        assertTrue(cell.isPresent());
        assertEquals(BoardCell.F5, cell.get());

        cell = Board.getCell(BoardCell.E4, -1, -1);
        assertTrue(cell.isPresent());
        assertEquals(BoardCell.D3, cell.get());

        cell = Board.getCell(BoardCell.E4, 2, 1);
        assertTrue(cell.isPresent());
        assertEquals(BoardCell.G5, cell.get());
    }

    @Test
    public void testRelativeCellNavigationOffBoard() {
        Optional<BoardCell> cell = Board.getCell(BoardCell.A1, -1, 0);
        assertFalse(cell.isPresent());

        cell = Board.getCell(BoardCell.A1, 0, -1);
        assertFalse(cell.isPresent());

        cell = Board.getCell(BoardCell.H8, 1, 0);
        assertFalse(cell.isPresent());

        cell = Board.getCell(BoardCell.H8, 0, 1);
        assertFalse(cell.isPresent());
    }

    @Test
    public void testAllCellsHaveUniqueIdx() {
        BoardCell[] cells = BoardCell.values();
        assertEquals(64, cells.length);

        boolean[] seen = new boolean[64];
        for (BoardCell cell : cells) {
            int idx = cell.getIdx();
            assertTrue(idx >= 0 && idx < 64);
            assertFalse(seen[idx]);
            seen[idx] = true;
        }
    }
}
