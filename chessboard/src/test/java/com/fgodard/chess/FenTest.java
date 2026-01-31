package com.fgodard.chess;

import com.fgodard.chess.board.*;
import com.fgodard.chess.board.pieces.*;
import com.fgodard.chess.exception.InvalidPositionException;

import org.junit.Test;

import static org.junit.Assert.*;

public class FenTest {

    private static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -";

    @Test
    public void testImportStartingPosition() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN(STARTING_FEN);

        assertTrue(board.getPiece(BoardCell.A1) instanceof Rook);
        assertEquals(Color.WHITE, board.getPiece(BoardCell.A1).getColor());

        assertTrue(board.getPiece(BoardCell.B1) instanceof Knight);
        assertTrue(board.getPiece(BoardCell.C1) instanceof Bishop);
        assertTrue(board.getPiece(BoardCell.D1) instanceof Queen);
        assertTrue(board.getPiece(BoardCell.E1) instanceof King);
        assertTrue(board.getPiece(BoardCell.F1) instanceof Bishop);
        assertTrue(board.getPiece(BoardCell.G1) instanceof Knight);
        assertTrue(board.getPiece(BoardCell.H1) instanceof Rook);

        for (BoardCell cell : new BoardCell[]{BoardCell.A2, BoardCell.B2, BoardCell.C2, BoardCell.D2,
                BoardCell.E2, BoardCell.F2, BoardCell.G2, BoardCell.H2}) {
            assertTrue(board.getPiece(cell) instanceof Pawn);
            assertEquals(Color.WHITE, board.getPiece(cell).getColor());
        }

        assertTrue(board.getPiece(BoardCell.A8) instanceof Rook);
        assertEquals(Color.BLACK, board.getPiece(BoardCell.A8).getColor());

        assertTrue(board.getPiece(BoardCell.E8) instanceof King);
        assertEquals(Color.BLACK, board.getPiece(BoardCell.E8).getColor());

        for (BoardCell cell : new BoardCell[]{BoardCell.A7, BoardCell.B7, BoardCell.C7, BoardCell.D7,
                BoardCell.E7, BoardCell.F7, BoardCell.G7, BoardCell.H7}) {
            assertTrue(board.getPiece(cell) instanceof Pawn);
            assertEquals(Color.BLACK, board.getPiece(cell).getColor());
        }

        for (int row = 2; row <= 5; row++) {
            for (int col = 0; col < 8; col++) {
                BoardCell cell = Board.getCell(col, row).get();
                assertNull(board.getPiece(cell));
            }
        }
    }

    @Test
    public void testImportTurnColor() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN(STARTING_FEN);
        assertEquals(Color.WHITE, board.getTurnColor());

        board.importFEN("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq -");
        assertEquals(Color.BLACK, board.getTurnColor());
    }

    @Test
    public void testImportCastlingRights() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN(STARTING_FEN);

        assertTrue(board.isWhiteCanCastleKingSide());
        assertTrue(board.isWhiteCanCastleQueenSide());
        assertTrue(board.isBlackCanCastleKingSide());
        assertTrue(board.isBlackCanCastleQueenSide());

        board.importFEN("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w Kq -");
        assertTrue(board.isWhiteCanCastleKingSide());
        assertFalse(board.isWhiteCanCastleQueenSide());
        assertFalse(board.isBlackCanCastleKingSide());
        assertTrue(board.isBlackCanCastleQueenSide());

        board.importFEN("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w - -");
        assertFalse(board.isWhiteCanCastleKingSide());
        assertFalse(board.isWhiteCanCastleQueenSide());
        assertFalse(board.isBlackCanCastleKingSide());
        assertFalse(board.isBlackCanCastleQueenSide());
    }

    @Test
    public void testImportEnPassantSquare() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("rnbqkbnr/pppp1ppp/8/4pP2/8/8/PPPPP1PP/RNBQKBNR w KQkq e6");

        BoardCell epCell = board.getEnPassantCell();
        assertNotNull(epCell);
        assertEquals(BoardCell.E6, epCell);
    }

    @Test
    public void testImportMidGamePosition() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("r1bqkb1r/pppp1ppp/2n2n2/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq -");

        assertTrue(board.getPiece(BoardCell.C4) instanceof Bishop);
        assertEquals(Color.WHITE, board.getPiece(BoardCell.C4).getColor());

        assertTrue(board.getPiece(BoardCell.F3) instanceof Knight);
        assertEquals(Color.WHITE, board.getPiece(BoardCell.F3).getColor());

        assertTrue(board.getPiece(BoardCell.C6) instanceof Knight);
        assertEquals(Color.BLACK, board.getPiece(BoardCell.C6).getColor());

        assertTrue(board.getPiece(BoardCell.E4) instanceof Pawn);
        assertTrue(board.getPiece(BoardCell.E5) instanceof Pawn);

        assertNull(board.getPiece(BoardCell.E2));
        assertNull(board.getPiece(BoardCell.E7));
    }

    @Test
    public void testExportFEN() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN(STARTING_FEN);

        String exported = board.exportFEN();

        assertTrue(exported.startsWith("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"));
        assertTrue(exported.contains(" w "));
        assertTrue(exported.contains("KQkq"));
    }

    @Test
    public void testFenRoundTrip() throws InvalidPositionException {
        String[] testFens = {
                STARTING_FEN,
                "r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq -",
                "r1bqkb1r/pppp1ppp/2n2n2/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq -",
                "8/8/8/8/4K3/8/8/4k3 w - -"
        };

        for (String fen : testFens) {
            GameBoard board = new GameBoard();
            board.importFEN(fen);
            String exported = board.exportFEN();

            GameBoard board2 = new GameBoard();
            board2.importFEN(exported);

            for (BoardCell cell : BoardCell.values()) {
                Piece p1 = board.getPiece(cell);
                Piece p2 = board2.getPiece(cell);
                if (p1 == null) {
                    assertNull(p2);
                } else {
                    assertNotNull(p2);
                    assertEquals(p1.getSymbol(), p2.getSymbol());
                    assertEquals(p1.getColor(), p2.getColor());
                }
            }
        }
    }

    @Test
    public void testExportLLP() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN(STARTING_FEN);

        String llp = board.exportLLP();
        assertNotNull(llp);
        assertFalse(llp.isEmpty());
    }

    @Test
    public void testLLPRoundTrip() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN(STARTING_FEN);

        String llp = board.exportLLP();

        GameBoard board2 = new GameBoard();
        board2.importLLP(llp);

        for (BoardCell cell : BoardCell.values()) {
            Piece p1 = board.getPiece(cell);
            Piece p2 = board2.getPiece(cell);
            if (p1 == null) {
                assertNull(p2);
            } else {
                assertNotNull(p2);
                assertEquals(p1.getSymbol(), p2.getSymbol());
                assertEquals(p1.getColor(), p2.getColor());
            }
        }
    }

    @Test(expected = InvalidPositionException.class)
    public void testImportInvalidFEN() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN("invalid fen string");
    }

    @Test
    public void testExportPosition() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN(STARTING_FEN);

        com.fgodard.chess.beans.Position pos = board.exportPosition();
        assertNotNull(pos);
        assertNotNull(pos.getPosition());
        assertEquals(Color.WHITE, pos.getTurnColor());
        assertTrue(pos.getBoardState().isPresent());
        assertTrue(pos.getBoardState().get().getWhiteCanCastleKingSide());
    }

    @Test
    public void testExportHTML() throws InvalidPositionException {
        GameBoard board = new GameBoard();
        board.importFEN(STARTING_FEN);

        String htmlWhite = board.exportHTML(Color.WHITE);
        assertNotNull(htmlWhite);
        assertTrue(htmlWhite.contains("<table"));
        assertTrue(htmlWhite.contains("</table>"));
        assertTrue(htmlWhite.contains("board_cell"));

        String htmlBlack = board.exportHTML(Color.BLACK);
        assertNotNull(htmlBlack);
        assertTrue(htmlBlack.contains("<table"));
    }
}
