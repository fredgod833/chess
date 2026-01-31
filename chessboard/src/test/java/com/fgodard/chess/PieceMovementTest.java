package com.fgodard.chess;

import com.fgodard.chess.board.*;
import com.fgodard.chess.board.pieces.*;
import com.fgodard.chess.exception.InvalidPositionException;

import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class PieceMovementTest {

    private static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -";

    @Test
    public void testPawnSinglePushWhite() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN(STARTING_FEN);

        Piece pawn = board.getPiece(BoardCell.E2);
        assertNotNull(pawn);
        assertTrue(pawn instanceof Pawn);
        assertEquals(Color.WHITE, pawn.getColor());

        Collection<BoardCell> moves = pawn.getMoveCells();
        assertTrue(moves.contains(BoardCell.E3));
        assertTrue(moves.contains(BoardCell.E4));
    }

    @Test
    public void testPawnSinglePushBlack() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq -");

        Piece pawn = board.getPiece(BoardCell.E7);
        assertNotNull(pawn);
        assertTrue(pawn instanceof Pawn);
        assertEquals(Color.BLACK, pawn.getColor());

        Collection<BoardCell> moves = pawn.getMoveCells();
        assertTrue(moves.contains(BoardCell.E6));
        assertTrue(moves.contains(BoardCell.E5));
    }

    @Test
    public void testPawnDoublePushOnlyFromStartingRank() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq -");

        Piece pawn = board.getPiece(BoardCell.E4);
        assertNotNull(pawn);

        Collection<BoardCell> moves = pawn.getMoveCells();
        assertFalse(moves.contains(BoardCell.E6));
    }

    @Test
    public void testPawnCaptures() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq -");

        Piece pawn = board.getPiece(BoardCell.E4);
        assertNotNull(pawn);

        Collection<BoardCell> moves = pawn.getMoveCells();
        assertTrue(moves.contains(BoardCell.D5));
        assertTrue(moves.contains(BoardCell.E5));
    }

    @Test
    public void testPawnBlockedByPiece() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("rnbqkbnr/pppp1ppp/4p3/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -");

        Piece pawn = board.getPiece(BoardCell.E2);
        Collection<BoardCell> moves = pawn.getMoveCells();
        assertTrue(moves.contains(BoardCell.E3));
        assertTrue(moves.contains(BoardCell.E4));
    }

    @Test
    public void testKnightMoves() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("8/8/8/8/4N3/8/8/8 w - -");

        Piece knight = board.getPiece(BoardCell.E4);
        assertNotNull(knight);
        assertTrue(knight instanceof Knight);

        Collection<BoardCell> moves = knight.getMoveCells();
        assertEquals(8, moves.size());
        assertTrue(moves.contains(BoardCell.F6));
        assertTrue(moves.contains(BoardCell.G5));
        assertTrue(moves.contains(BoardCell.G3));
        assertTrue(moves.contains(BoardCell.F2));
        assertTrue(moves.contains(BoardCell.D2));
        assertTrue(moves.contains(BoardCell.C3));
        assertTrue(moves.contains(BoardCell.C5));
        assertTrue(moves.contains(BoardCell.D6));
    }

    @Test
    public void testKnightInCorner() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("N7/8/8/8/8/8/8/8 w - -");

        Piece knight = board.getPiece(BoardCell.A8);
        assertNotNull(knight);

        Collection<BoardCell> moves = knight.getMoveCells();
        assertEquals(2, moves.size());
        assertTrue(moves.contains(BoardCell.B6));
        assertTrue(moves.contains(BoardCell.C7));
    }

    @Test
    public void testBishopMovesOnEmptyBoard() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("8/8/8/8/4B3/8/8/8 w - -");

        Piece bishop = board.getPiece(BoardCell.E4);
        assertNotNull(bishop);
        assertTrue(bishop instanceof Bishop);

        Collection<BoardCell> moves = bishop.getMoveCells();
        assertEquals(13, moves.size());
        assertTrue(moves.contains(BoardCell.F5));
        assertTrue(moves.contains(BoardCell.G6));
        assertTrue(moves.contains(BoardCell.H7));
        assertTrue(moves.contains(BoardCell.D3));
        assertTrue(moves.contains(BoardCell.C2));
        assertTrue(moves.contains(BoardCell.B1));
        assertTrue(moves.contains(BoardCell.D5));
        assertTrue(moves.contains(BoardCell.F3));
    }

    @Test
    public void testBishopBlockedByOwnPiece() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("8/8/6P1/8/4B3/8/8/8 w - -");

        Piece bishop = board.getPiece(BoardCell.E4);
        Collection<BoardCell> moves = bishop.getMoveCells();
        assertTrue(moves.contains(BoardCell.F5));
        assertFalse(moves.contains(BoardCell.G6));
        assertFalse(moves.contains(BoardCell.H7));
    }

    @Test
    public void testBishopCanCaptureEnemyPiece() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("8/8/6p1/8/4B3/8/8/8 w - -");

        Piece bishop = board.getPiece(BoardCell.E4);
        Collection<BoardCell> moves = bishop.getMoveCells();
        assertTrue(moves.contains(BoardCell.F5));
        assertTrue(moves.contains(BoardCell.G6));
        assertFalse(moves.contains(BoardCell.H7));
    }

    @Test
    public void testRookMovesOnEmptyBoard() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("8/8/8/8/4R3/8/8/8 w - -");

        Piece rook = board.getPiece(BoardCell.E4);
        assertNotNull(rook);
        assertTrue(rook instanceof Rook);

        Collection<BoardCell> moves = rook.getMoveCells();
        assertEquals(14, moves.size());
        assertTrue(moves.contains(BoardCell.E1));
        assertTrue(moves.contains(BoardCell.E8));
        assertTrue(moves.contains(BoardCell.A4));
        assertTrue(moves.contains(BoardCell.H4));
    }

    @Test
    public void testRookBlockedByPieces() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("8/8/4P3/8/2p1R3/8/8/8 w - -");

        Piece rook = board.getPiece(BoardCell.E4);
        Collection<BoardCell> moves = rook.getMoveCells();
        assertTrue(moves.contains(BoardCell.C4));
        assertFalse(moves.contains(BoardCell.B4));
        assertTrue(moves.contains(BoardCell.E5));
        assertFalse(moves.contains(BoardCell.E6));
    }

    @Test
    public void testQueenMoves() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("8/8/8/8/4Q3/8/8/8 w - -");

        Piece queen = board.getPiece(BoardCell.E4);
        assertNotNull(queen);
        assertTrue(queen instanceof Queen);

        Collection<BoardCell> moves = queen.getMoveCells();
        assertEquals(27, moves.size());
    }

    @Test
    public void testKingMovesOnEmptyBoard() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("8/8/8/8/4K3/8/8/8 w - -");

        Piece king = board.getPiece(BoardCell.E4);
        assertNotNull(king);
        assertTrue(king instanceof King);

        Collection<BoardCell> moves = king.getMoveCells();
        assertEquals(8, moves.size());
        assertTrue(moves.contains(BoardCell.D3));
        assertTrue(moves.contains(BoardCell.D4));
        assertTrue(moves.contains(BoardCell.D5));
        assertTrue(moves.contains(BoardCell.E3));
        assertTrue(moves.contains(BoardCell.E5));
        assertTrue(moves.contains(BoardCell.F3));
        assertTrue(moves.contains(BoardCell.F4));
        assertTrue(moves.contains(BoardCell.F5));
    }

    @Test
    public void testKingInCorner() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("K7/8/8/8/8/8/8/8 w - -");

        Piece king = board.getPiece(BoardCell.A8);
        Collection<BoardCell> moves = king.getMoveCells();
        assertEquals(3, moves.size());
        assertTrue(moves.contains(BoardCell.A7));
        assertTrue(moves.contains(BoardCell.B7));
        assertTrue(moves.contains(BoardCell.B8));
    }
}
