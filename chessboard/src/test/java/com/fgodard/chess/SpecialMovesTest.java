package com.fgodard.chess;

import com.fgodard.chess.beans.Ply;
import com.fgodard.chess.board.*;
import com.fgodard.chess.board.pieces.*;
import com.fgodard.chess.exception.InvalidMoveException;
import com.fgodard.chess.exception.InvalidPositionException;

import org.junit.Test;

import static org.junit.Assert.*;

public class SpecialMovesTest {

    @Test
    public void testWhiteKingSideCastle() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq -");

        Ply ply = new Ply();
        ply.setKingCastle(true);
        board.move(ply);

        assertNull(board.getPiece(BoardCell.E1));
        assertNull(board.getPiece(BoardCell.H1));
        assertTrue(board.getPiece(BoardCell.G1) instanceof King);
        assertTrue(board.getPiece(BoardCell.F1) instanceof Rook);
        assertFalse(board.isWhiteCanCastleKingSide());
        assertFalse(board.isWhiteCanCastleQueenSide());
    }

    @Test
    public void testWhiteQueenSideCastle() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq -");

        Ply ply = new Ply();
        ply.setQueenCastle(true);
        board.move(ply);

        assertNull(board.getPiece(BoardCell.E1));
        assertNull(board.getPiece(BoardCell.A1));
        assertTrue(board.getPiece(BoardCell.C1) instanceof King);
        assertTrue(board.getPiece(BoardCell.D1) instanceof Rook);
    }

    @Test
    public void testBlackKingSideCastle() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R b KQkq -");

        Ply ply = new Ply();
        ply.setKingCastle(true);
        board.move(ply);

        assertNull(board.getPiece(BoardCell.E8));
        assertNull(board.getPiece(BoardCell.H8));
        assertTrue(board.getPiece(BoardCell.G8) instanceof King);
        assertTrue(board.getPiece(BoardCell.F8) instanceof Rook);
        assertFalse(board.isBlackCanCastleKingSide());
        assertFalse(board.isBlackCanCastleQueenSide());
    }

    @Test
    public void testBlackQueenSideCastle() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R b KQkq -");

        Ply ply = new Ply();
        ply.setQueenCastle(true);
        board.move(ply);

        assertNull(board.getPiece(BoardCell.E8));
        assertNull(board.getPiece(BoardCell.A8));
        assertTrue(board.getPiece(BoardCell.C8) instanceof King);
        assertTrue(board.getPiece(BoardCell.D8) instanceof Rook);
    }

    @Test(expected = InvalidMoveException.class)
    public void testCastleWhenNotAllowed() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w - -");

        Ply ply = new Ply();
        ply.setKingCastle(true);
        board.move(ply);
    }

    @Test
    public void testCastleViaUci() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq -");

        board.applyUci("e1g1");

        assertTrue(board.getPiece(BoardCell.G1) instanceof King);
        assertTrue(board.getPiece(BoardCell.F1) instanceof Rook);
    }

    @Test
    public void testEnPassantCellTracking() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("rnbqkbnr/pppp1ppp/8/4p3/3P4/8/PPP1PPPP/RNBQKBNR w KQkq -");

        board.applyUci("e2e4");

        BoardCell epCell = board.getEnPassantCell();
        assertNotNull(epCell);
        assertEquals(BoardCell.E3, epCell);
    }

    @Test
    public void testEnPassantCapture() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("rnbqkbnr/pppp1ppp/8/4pP2/8/8/PPPPP1PP/RNBQKBNR w KQkq e6");

        Piece whitePawn = board.getPiece(BoardCell.F5);
        assertNotNull(whitePawn);

        java.util.Collection<BoardCell> moves = whitePawn.getMoveCells();
        assertTrue(moves.contains(BoardCell.E6));
    }

    @Test
    public void testEnPassantCaptureExecution() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("rnbqkbnr/pppp1ppp/8/4pP2/8/8/PPPPP1PP/RNBQKBNR w KQkq e6");

        Ply ply = new Ply();
        ply.setPiece('P');
        ply.setOrigCell("f5");
        ply.setDestCell("e6");
        board.move(ply);

        assertNull(board.getPiece(BoardCell.F5));
        assertNull(board.getPiece(BoardCell.E5));
        assertNotNull(board.getPiece(BoardCell.E6));
        assertTrue(board.getPiece(BoardCell.E6) instanceof Pawn);
        assertEquals(Color.WHITE, board.getPiece(BoardCell.E6).getColor());
    }

    @Test
    public void testPawnPromotionToQueen() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("8/4P3/8/8/8/8/8/4K2k w - -");

        Ply ply = new Ply();
        ply.setPiece('P');
        ply.setDestCell("e8");
        ply.setPromotion('Q');
        board.move(ply);

        Piece promoted = board.getPiece(BoardCell.E8);
        assertNotNull(promoted);
        assertTrue(promoted instanceof Queen);
        assertEquals(Color.WHITE, promoted.getColor());
    }

    @Test
    public void testPawnPromotionToRook() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("8/4P3/8/8/8/8/8/4K2k w - -");

        Ply ply = new Ply();
        ply.setPiece('P');
        ply.setDestCell("e8");
        ply.setPromotion('R');
        board.move(ply);

        assertTrue(board.getPiece(BoardCell.E8) instanceof Rook);
    }

    @Test
    public void testPawnPromotionToKnight() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("8/4P3/8/8/8/8/8/4K2k w - -");

        Ply ply = new Ply();
        ply.setPiece('P');
        ply.setDestCell("e8");
        ply.setPromotion('N');
        board.move(ply);

        assertTrue(board.getPiece(BoardCell.E8) instanceof Knight);
    }

    @Test
    public void testPawnPromotionToBishop() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("8/4P3/8/8/8/8/8/4K2k w - -");

        Ply ply = new Ply();
        ply.setPiece('P');
        ply.setDestCell("e8");
        ply.setPromotion('B');
        board.move(ply);

        assertTrue(board.getPiece(BoardCell.E8) instanceof Bishop);
    }

    @Test
    public void testPromotionViaUci() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("8/4P3/8/8/8/8/8/4K2k w - -");

        board.applyUci("e7e8q");

        assertTrue(board.getPiece(BoardCell.E8) instanceof Queen);
    }

    @Test
    public void testBlackPawnPromotion() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("4K2k/8/8/8/8/8/4p3/8 b - -");

        Ply ply = new Ply();
        ply.setPiece('P');
        ply.setDestCell("e1");
        ply.setPromotion('Q');
        board.move(ply);

        Piece promoted = board.getPiece(BoardCell.E1);
        assertNotNull(promoted);
        assertTrue(promoted instanceof Queen);
        assertEquals(Color.BLACK, promoted.getColor());
    }

    @Test
    public void testCastlingRightsLostAfterKingMove() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("r3k2r/pppppppp/8/8/8/8/PPPP1PPP/R3K2R w KQkq -");

        assertTrue(board.isWhiteCanCastleKingSide());
        assertTrue(board.isWhiteCanCastleQueenSide());

        Ply ply = new Ply();
        ply.setPiece('K');
        ply.setDestCell("e2");
        board.move(ply);

        assertFalse(board.isWhiteCanCastleKingSide());
        assertFalse(board.isWhiteCanCastleQueenSide());
    }

    @Test
    public void testCastlingRightsLostAfterRookMove() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq -");

        Ply ply = new Ply();
        ply.setPiece('R');
        ply.setOrigCell("h1");
        ply.setDestCell("g1");
        board.move(ply);

        assertFalse(board.isWhiteCanCastleKingSide());
        assertTrue(board.isWhiteCanCastleQueenSide());
    }
}
