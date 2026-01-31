package com.fgodard.chess;

import com.fgodard.chess.beans.Ply;
import com.fgodard.chess.board.*;
import com.fgodard.chess.board.pieces.*;
import com.fgodard.chess.exception.InvalidCellException;
import com.fgodard.chess.exception.InvalidMoveException;
import com.fgodard.chess.exception.InvalidPieceException;
import com.fgodard.chess.exception.InvalidPositionException;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameBoardTest {

    private static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -";

    @Test
    public void testAddPieceAndGetPiece() throws InvalidPieceException, InvalidCellException {
        GameBoard board = new GameBoard();
        board.addPiece('K', 4, 0);

        Piece king = board.getPiece(BoardCell.E1);
        assertNotNull(king);
        assertTrue(king instanceof King);
        assertEquals(Color.WHITE, king.getColor());
        assertEquals(BoardCell.E1, king.getCell());
    }

    @Test
    public void testAddBlackPiece() throws InvalidPieceException, InvalidCellException {
        GameBoard board = new GameBoard();
        board.addPiece('k', 4, 7);

        Piece king = board.getPiece(BoardCell.E8);
        assertNotNull(king);
        assertTrue(king instanceof King);
        assertEquals(Color.BLACK, king.getColor());
    }

    @Test
    public void testGetPieceByAlgebraicNotation() throws InvalidPieceException, InvalidCellException {
        GameBoard board = new GameBoard();
        board.addPiece('Q', 3, 0);

        Piece queen = board.getPiece("d1");
        assertNotNull(queen);
        assertTrue(queen instanceof Queen);
    }

    @Test
    public void testGetPieceEmptyCell() throws InvalidCellException {
        GameBoard board = new GameBoard();
        Piece piece = board.getPiece(BoardCell.E4);
        assertNull(piece);
    }

    @Test
    public void testGetPieceNullCell() {
        GameBoard board = new GameBoard();
        Piece piece = board.getPiece((BoardCell) null);
        assertNull(piece);
    }

    @Test(expected = InvalidPieceException.class)
    public void testAddInvalidPiece() throws InvalidPieceException, InvalidCellException {
        GameBoard board = new GameBoard();
        board.addPiece('X', 0, 0);
    }

    @Test(expected = InvalidCellException.class)
    public void testAddPieceInvalidCell() throws InvalidPieceException, InvalidCellException {
        GameBoard board = new GameBoard();
        board.addPiece('K', 8, 0);
    }

    @Test
    public void testMoveRegularPiece() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN(STARTING_FEN);

        Ply ply = new Ply();
        ply.setPiece('P');
        ply.setDestCell("e4");

        board.move(ply);

        assertNull(board.getPiece(BoardCell.E2));
        assertNotNull(board.getPiece(BoardCell.E4));
        assertTrue(board.getPiece(BoardCell.E4) instanceof Pawn);
    }

    @Test
    public void testTurnColorAlternation() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN(STARTING_FEN);

        assertEquals(Color.WHITE, board.getTurnColor());

        Ply ply = new Ply();
        ply.setPiece('P');
        ply.setDestCell("e4");
        board.move(ply);

        assertEquals(Color.BLACK, board.getTurnColor());

        ply = new Ply();
        ply.setPiece('P');
        ply.setDestCell("e5");
        board.move(ply);

        assertEquals(Color.WHITE, board.getTurnColor());
    }

    @Test
    public void testPlyCounterIncrement() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN(STARTING_FEN);

        assertEquals(0, board.getPlyNo());

        Ply ply = new Ply();
        ply.setPiece('P');
        ply.setDestCell("e4");
        board.move(ply);

        assertEquals(1, board.getPlyNo());

        ply = new Ply();
        ply.setPiece('P');
        ply.setDestCell("e5");
        board.move(ply);

        assertEquals(2, board.getPlyNo());
    }

    @Test
    public void testApplyUciMove() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN(STARTING_FEN);

        board.applyUci("e2e4");

        assertNull(board.getPiece(BoardCell.E2));
        assertNotNull(board.getPiece(BoardCell.E4));
        assertEquals(Color.BLACK, board.getTurnColor());
    }

    @Test
    public void testApplyUciCapture() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq -");

        board.applyUci("e4d5");

        assertNull(board.getPiece(BoardCell.E4));
        Piece piece = board.getPiece(BoardCell.D5);
        assertNotNull(piece);
        assertTrue(piece instanceof Pawn);
        assertEquals(Color.WHITE, piece.getColor());
    }

    @Test(expected = InvalidMoveException.class)
    public void testApplyInvalidUci() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN(STARTING_FEN);
        board.applyUci("e2");
    }

    @Test(expected = InvalidMoveException.class)
    public void testApplyUciNoPieceAtSource() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN(STARTING_FEN);
        board.applyUci("e4e5");
    }

    @Test
    public void testSetTurnColor() {
        GameBoard board = new GameBoard();
        board.setTurnColor(Color.BLACK);
        assertEquals(Color.BLACK, board.getTurnColor());

        board.setTurnColor(Color.WHITE);
        assertEquals(Color.WHITE, board.getTurnColor());
    }

    @Test
    public void testCastlingRightsAfterImport() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN(STARTING_FEN);

        assertTrue(board.isWhiteCanCastleKingSide());
        assertTrue(board.isWhiteCanCastleQueenSide());
        assertTrue(board.isBlackCanCastleKingSide());
        assertTrue(board.isBlackCanCastleQueenSide());
    }

    @Test
    public void testCastlingRightsPartial() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w Kq -");

        assertTrue(board.isWhiteCanCastleKingSide());
        assertFalse(board.isWhiteCanCastleQueenSide());
        assertFalse(board.isBlackCanCastleKingSide());
        assertTrue(board.isBlackCanCastleQueenSide());
    }

    @Test
    public void testCastlingRightsNone() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w - -");

        assertFalse(board.isWhiteCanCastleKingSide());
        assertFalse(board.isWhiteCanCastleQueenSide());
        assertFalse(board.isBlackCanCastleKingSide());
        assertFalse(board.isBlackCanCastleQueenSide());
    }
}
