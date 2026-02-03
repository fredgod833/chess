package com.fgodard.chess.board;

import com.fgodard.chess.beans.Game;
import com.fgodard.chess.beans.Ply;
import com.fgodard.chess.beans.Position;
import com.fgodard.chess.exception.InvalidMoveException;
import com.fgodard.chess.exception.InvalidPgnException;
import com.fgodard.chess.exception.InvalidPositionException;
import com.fgodard.text.TextStripe;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilitaire pour le parsing et l'import de fichiers PGN (Portable Game Notation).
 * <p>
 * Cette classe permet de lire des fichiers PGN contenant une ou plusieurs parties d'échecs,
 * d'extraire les métadonnées (joueurs, Elo, date, résultat, code ECO) et de reconstituer
 * la séquence des coups joués.
 * </p>
 * <p>
 * Le format PGN est le standard pour l'échange de parties d'échecs. Il comprend :
 * <ul>
 *     <li>Des en-têtes entre crochets (Event, Site, Date, White, Black, Result, etc.)</li>
 *     <li>Les coups en notation algébrique standard (SAN)</li>
 *     <li>Des commentaires optionnels entre accolades ou après point-virgule</li>
 *     <li>Des variantes optionnelles entre parenthèses</li>
 * </ul>
 * </p>
 *
 * <p><b>Exemple d'utilisation :</b></p>
 * <pre>{@code
 * PGNHelper.readPgnFile(new File("parties.pgn"), game -> {
 *     System.out.println(game.getWhitePlayerName() + " vs " + game.getBlackPlayerName());
 * });
 * }</pre>
 *
 * @author crios
 * @since 11/11/23
 * @see Game
 * @see GameExporter
 */
public class PGNHelper {

    private static final Pattern movePattern = Pattern.compile("([BKNPQR]?)([a-h]?)([1-8]?)([x])?([a-h]{1}[1-8]{1})[=]?([BNQR]?)([+]?)([#]?)");
    private static final Pattern spacePattern = Pattern.compile("([ ]{1})");
    private static final Pattern numPattern = Pattern.compile("([0-9]+[\\.]+)");
    private static final Pattern multSpacesPattern = Pattern.compile("([ ]+)");
    private static final Pattern tagPattern = Pattern.compile("([$]{1}[0-9]* )");
    private static final Pattern multEOLPattern = Pattern.compile("([\\r|\\n]+)");
    private static final Pattern commentEOLPattern = Pattern.compile("([;]{1}[^\\n]*[\\n])");
    static final Pattern headerPattern = Pattern.compile("[\\[]([A-Za-z]*)[\\t ]*[\"](.*)[\"][\\t ]*[\\]]");
    private static final Pattern datePattern = Pattern.compile("([0-9?]{4})[\\.]([0-9?]{2})[\\.]([0-9?]{2})");


    private static boolean importHeaderLine(Game game, final String headerLine) {
        Matcher m = PGNHelper.headerPattern.matcher(headerLine);
        if (m.find()){
            appendGameHeader(game, m.group(1).trim().toUpperCase(), m.group(2).trim());
            return true;
        }
        return false;
    }

    private static Ply buildPly(final String algebricPly) throws InvalidMoveException {

        boolean read = false;

        Ply ply = new Ply();
        if (algebricPly.startsWith("O-O-O")) {
            ply.setQueenCastle(true);
            read = true;

        } else if (algebricPly.startsWith("O-O")) {
            ply.setKingCastle(true);
            read = true;

        } else {
            Matcher m = movePattern.matcher(algebricPly);
            if (m.find()) {
                String pieceStr = m.group(1);

                Character piece;
                if (pieceStr == null || pieceStr.isEmpty()) {
                    piece = 'P';
                } else {
                    piece = pieceStr.charAt(0);
                }
                ply.setPiece(piece);

                String destCell = m.group(5);
                ply.setDestCell(destCell);

                String orgPos = m.group(2);
                if (m.group(3) != null) {
                    if (orgPos == null) {
                        orgPos = m.group(3);
                    } else {
                        orgPos = orgPos.concat(m.group(3));
                    }
                }

                ply.setOrigCell(orgPos);
                ply.setTake("x".equals(m.group(4)));
                String promPiece = m.group(6);
                if (promPiece != null && !promPiece.isEmpty()) {
                    ply.setPromotion(promPiece.charAt(0));
                }

                ply.setCheck("+".equals(m.group(7)));
                ply.setMate("#".equals(m.group(8)));

                read = true;
            }
        }

        if (read) {
            return ply;

        } else {
            throw new InvalidMoveException("Mouvement invalide (%s)", algebricPly);

        }
    }

    private static void appendGameHeader(Game game, final String headerName, final String headerValue) {
        if ("EVENT".equals(headerName)){
            game.setEventName(headerValue);

        } else if ("SITE".equals(headerName)){
            game.setSite(headerValue);

        } else if ("DATE".equals(headerName)){
            Matcher m = datePattern.matcher(headerValue);
            if (m.find()) {
                String year = m.group(1);
                if (!year.contains("?")) {
                    game.setYear(Integer.parseInt(year));
                }
                String month = m.group(1);
                if (!month.contains("?")) {
                    game.setMonth(Integer.parseInt(month));
                }
                String day = m.group(1);
                if (!day.contains("?")) {
                    game.setDay(Integer.parseInt(day));
                }
            }

        } else if ("ROUND".equals(headerName)){

        } else if ("WHITE".equals(headerName)){
            game.setWhitePlayerName(headerValue);

        } else if ("BLACK".equals(headerName)){
            game.setBlackPlayerName(headerValue);

        } else if ("RESULT".equals(headerName)){
            setGameResult(game, headerValue);

        } else if ("WHITETITLE".equals(headerName)){

        } else if ("BLACKTITLE".equals(headerName)){

        } else if ("WHITEELO".equals(headerName)){
            game.setWhiteElo(Integer.parseInt(headerValue));

        } else if ("BLACKELO".equals(headerName)){
            game.setBlackElo(Integer.parseInt(headerValue));

        } else if ("WHITEUSCF".equals(headerName)){
        } else if ("BLACKUSCF".equals(headerName)){
        } else if ("WHITENA".equals(headerName)){
        } else if ("BLACKNA".equals(headerName)){
        } else if ("WHITETYPE".equals(headerName)){
        } else if ("BLACKTYPE".equals(headerName)){
        } else if ("EVENTDATE".equals(headerName)){
        } else if ("EVENTSPONSOR".equals(headerName)){
        } else if ("SECTION".equals(headerName)){
        } else if ("STAGE".equals(headerName)){
        } else if ("BOARD".equals(headerName)){
        } else if ("OPENING".equals(headerName)){
        } else if ("VARIATION".equals(headerName)){
        } else if ("SUBVARIATION".equals(headerName)){
        } else if ("ECO".equals(headerName)){
            game.setEcoCode(headerValue.toUpperCase());

        } else if ("NIC".equals(headerName)){
        } else if ("TIME".equals(headerName)){
        } else if ("UTCTIME".equals(headerName)){
        } else if ("UTCDATE".equals(headerName)){
        } else if ("TIMECONTROL".equals(headerName)){
        } else if ("SETUP".equals(headerName)){
        } else if ("FEN".equals(headerName)){
            game.setStartFENPosition(headerValue);

        } else if ("TERMINATION".equals(headerName)){
        } else if ("ANNOTATOR".equals(headerName)){
        } else if ("MODE".equals(headerName)){
        } else if ("PLYCOUNT".equals(headerName)){
        }
    }

    private static boolean setGameResult(Game game, final String sResult) {
        if ("0-1".equals(sResult)) {
            game.setResult(-1);
            return true;
        }

        if ("1-0".equals(sResult)) {
            game.setResult(1);
            return true;
        }

        if ("1/2-1/2".equals(sResult)) {
            game.setResult(0);
            return true;
        }

        return false;
    }

    private static void appendMoves(Game game, final String pgnHeader, final String pgnMoves) throws InvalidPgnException {

        try {

            game.setPgnHeaders(pgnHeader);
            game.setPgnBody(pgnMoves);

            GameBoard board = new GameBoard();
            try {
                board.importFEN(game.getStartFENPosition());
            } catch (InvalidPositionException ex) {
                throw new InvalidPgnException(ex,"PGN invalide.");
            }

            if (board.isChess960()) {
                // non traité
                return;
            }

            //Nettoyage de la partie (suppression des commentaires, retour ligne, etudes)
            String body = pgnMoves;
            body = multEOLPattern.matcher(body).replaceAll("\n");
            body = commentEOLPattern.matcher(body).replaceAll(" ");
            body = multEOLPattern.matcher(body).replaceAll(" ");
            body = tagPattern.matcher(body).replaceAll("");
            body = numPattern.matcher(body).replaceAll(" ");
            body = multSpacesPattern.matcher(body).replaceAll(" ");
            body = spacePattern.matcher(body).replaceAll("\n");
            body = body.trim();

            String[] plyArray = body.split("\n");
            Ply ply;
            Position position = board.exportPosition();
            for (String plyStr : plyArray) {
                if (plyStr != null && ! plyStr.isEmpty() && !"*".equals(plyStr)) {
                    if ("Z0".equals(plyStr) || "--".equals(plyStr)) {
                        game.resetMoves();
                        board.resetForNullMove();
                        game.setStartFENPosition(board.exportFEN());

                    } else if (setGameResult(game, plyStr)) {
                        //end game
                        break;
                    } else {
                        ply = buildPly(plyStr);
                        ply.setInitialPosition(position);
                        board.move(ply);
                        position = board.exportPosition();
                        ply.setFinalPosition(position);
                        if (ply.getColor() == Color.WHITE) {
                            ply.setEloRange(game.getWhiteEloRange());
                        } else if (ply.getColor() == Color.BLACK) {
                            ply.setEloRange(game.getBlackEloRange());
                        }
                        ply.setYearRange(game.getYearRange());
                        game.addNewMove(ply);
                    }
                }
            }

        } catch (InvalidMoveException e) {
            throw new InvalidPgnException(e, "PGN invalide.");
        }
    }

    private static void importPGN(BufferedReader reader, GameExporter exporter) throws IOException, InvalidPgnException {

        Game game = new Game();
        StringBuilder sbPgn = new StringBuilder();
        StringBuilder sbMoves = new StringBuilder();
        boolean hasContent = false;

        TextStripe commentsTextStripe = new TextStripe("{","}");
        TextStripe studyTextStripe = new TextStripe("\\(","\\)");
        String line;
        String fileLine = "";
        do {

            line = commentsTextStripe.retrieveLine();
            while (line.isEmpty() && fileLine != null) {
                    fileLine = reader.readLine();
                    commentsTextStripe.appendLine(fileLine);
                    line = commentsTextStripe.retrieveLine();
            }

            if (!line.isEmpty()) {
                Matcher m = headerPattern.matcher(line);
                if (m.find()) {
                    // la ligne est un header de partie.
                    if (hasContent) {
                        // on a un jeu en cours d'extraction mais on découvre un nouveau header : il s'agit d'une nouvelle partie.
                        // on sauvegarde la précédente.
                        appendMoves(game, sbPgn.toString(), sbMoves.toString());
                        exporter.exportGame(game);
                        //réinit du jeu à importer.
                        hasContent = false;
                        sbPgn = new StringBuilder();
                        sbMoves = new StringBuilder();
                        game = new Game();
                        studyTextStripe.clear();
                    }
                    // on renseigne la propriété décrite dans ce header.
                    appendGameHeader(game, m.group(1).toUpperCase(), m.group(2));
                    // on sauvegarde dans le pgn reformatté.
                    sbPgn.append(line);
                    sbPgn.append("\n");

                } else {

                    hasContent = true;
                    studyTextStripe.appendLine(line);
                    String filteredStr = studyTextStripe.retrieveLine();
                    while (!filteredStr.isEmpty()) {
                        sbMoves.append(filteredStr);
                        sbMoves.append(" ");
                        filteredStr = studyTextStripe.retrieveLine();
                    }

                }
            }

        } while (!line.isEmpty());

        if (hasContent) {
            appendMoves(game, sbPgn.toString(), sbMoves.toString());
            exporter.exportGame(game);
        }
    }

    /**
     * Lit un fichier PGN et exporte chaque partie via l'exporteur fourni.
     * <p>
     * Le fichier peut contenir plusieurs parties. Chaque partie est parsée,
     * validée et passée à l'exporteur pour traitement.
     * </p>
     *
     * @param inputFile le fichier PGN à lire
     * @param exporter  l'exporteur appelé pour chaque partie extraite
     * @throws IOException         si le fichier est inaccessible ou en cas d'erreur de lecture
     * @throws InvalidPgnException si le contenu PGN est invalide
     */
    public static void readPgnFile(File inputFile, GameExporter exporter) throws IOException, InvalidPgnException {

        if (inputFile == null || !inputFile.exists() || !inputFile.canRead() ) {
            throw new InvalidPgnException("Fichier pgn inaccessible (%s).", inputFile);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));) {
            importPGN(reader, exporter);

        }
    }
}
