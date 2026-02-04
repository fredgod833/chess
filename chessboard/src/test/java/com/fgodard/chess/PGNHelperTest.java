package com.fgodard.chess;

import com.fgodard.chess.beans.Game;
import com.fgodard.chess.beans.Ply;
import com.fgodard.chess.board.PGNHelper;
import com.fgodard.chess.exception.InvalidPgnException;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PGNHelperTest {

    private File getResourceFile(String filename) {
        URL resource = getClass().getClassLoader().getResource("pgn/" + filename);
        assertNotNull("Resource file not found: " + filename, resource);
        return new File(resource.getFile());
    }

    @Test
    public void testReadSimpleGame() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("simple_game.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        assertEquals(1, games.size());
        Game game = games.get(0);

        assertEquals("Test Event", game.getEventName());
        assertEquals("Test Site", game.getSite());
        assertEquals("Player White", game.getWhitePlayerName());
        assertEquals("Player Black", game.getBlackPlayerName());
        assertEquals(Integer.valueOf(2500), game.getWhiteElo());
        assertEquals(Integer.valueOf(2400), game.getBlackElo());
        assertEquals("C50", game.getEcoCode());
        assertEquals(Integer.valueOf(1), game.getResult());
    }

    @Test
    public void testReadMultipleGames() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("multiple_games.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        assertEquals(3, games.size());

        assertEquals("First Game", games.get(0).getEventName());
        assertEquals("White1", games.get(0).getWhitePlayerName());
        assertEquals(Integer.valueOf(1), games.get(0).getResult());

        assertEquals("Second Game", games.get(1).getEventName());
        assertEquals("White2", games.get(1).getWhitePlayerName());
        assertEquals(Integer.valueOf(-1), games.get(1).getResult());

        assertEquals("Third Game", games.get(2).getEventName());
        assertEquals("White3", games.get(2).getWhitePlayerName());
        assertEquals(Integer.valueOf(0), games.get(2).getResult());
    }

    @Test
    public void testReadGameWithComments() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("game_with_comments.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        assertEquals(1, games.size());
        Game game = games.get(0);

        assertEquals("Commented Game", game.getEventName());
        assertFalse(game.getPlies().isEmpty());
    }

    @Test
    public void testCastlingKingSide() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("castling_game.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        assertEquals(1, games.size());
        Game game = games.get(0);

        List<Ply> plies = game.getPlies();
        boolean foundKingSideCastle = false;
        for (Ply ply : plies) {
            if (ply.isKingCastle()) {
                foundKingSideCastle = true;
                break;
            }
        }
        assertTrue("King-side castling not found", foundKingSideCastle);
    }

    @Test
    public void testCastlingQueenSide() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("castling_game.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        assertEquals(1, games.size());
        Game game = games.get(0);

        List<Ply> plies = game.getPlies();
        boolean foundQueenSideCastle = false;
        for (Ply ply : plies) {
            if (ply.isQueenCastle()) {
                foundQueenSideCastle = true;
                break;
            }
        }
        assertTrue("Queen-side castling not found", foundQueenSideCastle);
    }

    @Test
    public void testPromotion() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("promotion_game.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        assertEquals(1, games.size());
        Game game = games.get(0);

        assertFalse(game.getPlies().isEmpty());
        Ply promotionPly = game.getPlies().get(0);
        assertEquals(Character.valueOf('Q'), promotionPly.getPromotion());
    }

    @Test
    public void testCaptures() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("capture_game.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        assertEquals(1, games.size());
        Game game = games.get(0);

        List<Ply> plies = game.getPlies();
        int captureCount = 0;
        for (Ply ply : plies) {
            if (ply.isTake()) {
                captureCount++;
            }
        }
        assertTrue("Expected at least 2 captures", captureCount >= 2);
    }

    @Test
    public void testPlyCount() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("simple_game.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        assertEquals(1, games.size());
        Game game = games.get(0);

        assertEquals(12, game.getPlies().size());
    }

    @Test
    public void testDisambiguation() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("disambiguation_game.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        assertEquals(1, games.size());
        Game game = games.get(0);

        assertFalse(game.getPlies().isEmpty());
    }

    @Test
    public void testCustomStartPosition() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("promotion_game.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        assertEquals(1, games.size());
        Game game = games.get(0);

        assertNotEquals(Game.FEN_STD_START_POSITION, game.getStartFENPosition());
        assertTrue(game.getStartFENPosition().contains("8/P7/8/8/8/8/8/4K2k"));
    }

    @Test
    public void testPositionList() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("simple_game.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        Game game = games.get(0);
        List<String> positions = game.getPosList();

        assertNotNull(positions);
        assertEquals(game.getPlies().size() + 1, positions.size());
    }

    @Test
    public void testPgnBodyStored() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("simple_game.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        Game game = games.get(0);
        assertNotNull(game.getPgnBody());
        assertFalse(game.getPgnBody().isEmpty());
    }

    @Test
    public void testPgnHeadersStored() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("simple_game.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        Game game = games.get(0);
        assertNotNull(game.getPgnHeaders());
        assertTrue(game.getPgnHeaders().contains("Event"));
    }

    @Test(expected = InvalidPgnException.class)
    public void testReadNonExistentFile() throws IOException, InvalidPgnException {
        File pgnFile = new File("/non/existent/file.pgn");
        PGNHelper.readPgnFile(pgnFile, game -> {});
    }

    @Test(expected = InvalidPgnException.class)
    public void testReadNullFile() throws IOException, InvalidPgnException {
        PGNHelper.readPgnFile(null, game -> {});
    }

    @Test
    public void testEloRangeCalculation() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("simple_game.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        Game game = games.get(0);
        assertNotNull(game.getWhiteEloRange());
        assertNotNull(game.getBlackEloRange());
    }

    @Test
    public void testResultWhiteWins() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("simple_game.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        assertEquals(Integer.valueOf(1), games.get(0).getResult());
    }

    @Test
    public void testResultBlackWins() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("multiple_games.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        assertEquals(Integer.valueOf(-1), games.get(1).getResult());
    }

    @Test
    public void testResultDraw() throws IOException, InvalidPgnException {
        File pgnFile = getResourceFile("multiple_games.pgn");
        List<Game> games = new ArrayList<>();

        PGNHelper.readPgnFile(pgnFile, games::add);

        assertEquals(Integer.valueOf(0), games.get(2).getResult());
    }
}
