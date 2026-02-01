package com.fgodard.chess;

import com.fgodard.chess.beans.Ply;
import com.fgodard.chess.board.*;
import com.fgodard.chess.board.pieces.*;
import com.fgodard.chess.exception.InvalidMoveException;
import com.fgodard.chess.exception.InvalidPositionException;

import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class CheckDetectionTest {

    @Test
    public void testKingNotInCheck() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("8/8/8/8/4K3/8/8/4k3 w - -");

        Piece king = board.getPiece(BoardCell.E4);
        assertNotNull(king);
        assertTrue(king instanceof King);

        Collection<Piece> attackers = MoveHelper.findAttackingPieces((King) king);
        assertTrue(attackers.isEmpty());
    }

    @Test
    public void testKingInCheckByRook() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("4r3/8/8/8/4K3/8/8/4k3 w - -");

        King king = (King) board.getPiece(BoardCell.E4);
        Collection<Piece> attackers = MoveHelper.findAttackingPieces(king);
        assertFalse(attackers.isEmpty());
        assertEquals(1, attackers.size());

        Piece attacker = attackers.iterator().next();
        assertTrue(attacker instanceof Rook);
        assertEquals(BoardCell.E8, attacker.getCell());
    }

    @Test
    public void testKingInCheckByBishop() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("8/8/2b5/8/4K3/8/8/4k3 w - -");

        King king = (King) board.getPiece(BoardCell.E4);
        Collection<Piece> attackers = MoveHelper.findAttackingPieces(king);
        assertFalse(attackers.isEmpty());

        Piece attacker = attackers.iterator().next();
        assertTrue(attacker instanceof Bishop);
    }

    @Test
    public void testKingInCheckByKnight() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("8/8/3n4/8/4K3/8/8/4k3 w - -");

        King king = (King) board.getPiece(BoardCell.E4);
        Collection<Piece> attackers = MoveHelper.findAttackingPieces(king);
        assertFalse(attackers.isEmpty());

        Piece attacker = attackers.iterator().next();
        assertTrue(attacker instanceof Knight);
        assertEquals(BoardCell.D6, attacker.getCell());
    }

    @Test
    public void testKingInCheckByPawn() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("8/8/8/3p4/4K3/8/8/4k3 w - -");

        King king = (King) board.getPiece(BoardCell.E4);
        Collection<Piece> attackers = MoveHelper.findAttackingPieces(king);
        assertFalse(attackers.isEmpty());

        Piece attacker = attackers.iterator().next();
        assertTrue(attacker instanceof Pawn);
    }

    @Test
    public void testKingInCheckByQueen() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("8/8/8/8/q3K3/8/8/4k3 w - -");

        King king = (King) board.getPiece(BoardCell.E4);
        Collection<Piece> attackers = MoveHelper.findAttackingPieces(king);
        assertFalse(attackers.isEmpty());

        Piece attacker = attackers.iterator().next();
        assertTrue(attacker instanceof Queen);
    }

    @Test
    public void testKingInDoubleCheck() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("4r3/8/3n4/8/4K3/8/8/4k3 w - -");

        King king = (King) board.getPiece(BoardCell.E4);
        Collection<Piece> attackers = MoveHelper.findAttackingPieces(king);
        assertEquals(2, attackers.size());
    }

    @Test
    public void testKingMovesAvoidAttackedSquares() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("8/8/8/8/r3K3/8/8/4k3 w - -");

        King king = (King) board.getPiece(BoardCell.E4);
        assertNotNull(king);
        Collection<BoardCell> moves = king.getMoveCells();
        assertFalse(moves.isEmpty());
        assertTrue(moves.contains(BoardCell.F5) || moves.contains(BoardCell.F3) || moves.contains(BoardCell.F4));
    }

    @Test
    public void testCheckFlagSetOnMove() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("4k3/8/8/8/8/8/4R3/4K3 w - -");

        board.applyUci("e2e7");

        assertEquals(Color.BLACK, board.getTurnColor());
        assertNotNull(board.getPiece(BoardCell.E7));
    }

    @Test
    public void testRookCanMoveVertically() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("6k1/8/8/8/8/8/8/4K2R w - -");

        Piece rook = board.getPiece(BoardCell.H1);
        assertNotNull(rook);
        assertTrue(rook instanceof Rook);
        assertEquals(Color.WHITE, rook.getColor());

        Collection<BoardCell> moves = rook.getMoveCells();
        assertFalse(moves.isEmpty());
        assertTrue(moves.contains(BoardCell.H2));
        assertTrue(moves.contains(BoardCell.H8));
    }

    @Test
    public void testScholarsMate() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("r1bqkb1r/pppp1Qpp/2n2n2/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq -");

        King king = (King) board.getPiece(BoardCell.E8);
        assertNotNull(king);

        Collection<BoardCell> kingMoves = king.getMoveCells();
        assertTrue(kingMoves.isEmpty());

        Collection<Piece> attackers = MoveHelper.findAttackingPieces(king);
        assertFalse(attackers.isEmpty());
    }

    @Test
    public void testAttackBlockedByOwnPiece() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("4r3/4P3/8/8/4K3/8/8/4k3 w - -");

        King king = (King) board.getPiece(BoardCell.E4);
        Collection<Piece> attackers = MoveHelper.findAttackingPieces(king);
        assertTrue(attackers.isEmpty());
    }

    @Test
    public void testFindAttackingPiecesOnCell() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("8/8/8/8/4r3/8/8/R3K2k w - -");

        Piece blackRook = board.getPiece(BoardCell.E4);
        assertNotNull(blackRook);
        assertTrue(blackRook instanceof Rook);
        assertEquals(Color.BLACK, blackRook.getColor());
    }

    @Test
    public void testCheckNotMateWhenKingCanEscape() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("4k3/8/8/8/8/8/4R3/4K3 w - -");

        Ply ply = new Ply();
        ply.setPiece('R');
        ply.setOrigCell("e2");
        ply.setDestCell("e7");
        board.move(ply);

        assertTrue(ply.isCheck());
        assertFalse(ply.isMate());
    }

    @Test
    public void testCheckNotMateWhenPieceCanBlock() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("3nk3/8/8/8/8/8/8/R3K3 w - -");

        board.applyUci("a1a8");

        assertTrue(board.getPiece(BoardCell.A8) instanceof Rook);
        assertEquals(Color.BLACK, board.getTurnColor());
    }

    @Test
    public void testCheckNotMateWhenAttackerCanBeCaptured() throws InvalidPositionException, InvalidMoveException {
        GameBoard board = new GameBoard();
        board.importFEN("r3k3/8/8/8/8/8/8/R3K3 w - -");

        Ply ply = new Ply();
        ply.setPiece('R');
        ply.setOrigCell("a1");
        ply.setDestCell("a8");
        board.move(ply);

        assertTrue(ply.isCheck());
        assertFalse(ply.isMate());
    }
}
