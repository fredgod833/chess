package com.fgodard.chess.board;

import com.fgodard.chess.exception.InvalidCellException;
import com.fgodard.chess.exception.InvalidPieceColorException;
import com.fgodard.chess.exception.InvalidPieceException;
import com.fgodard.chess.exception.InvalidPositionException;

import java.util.Arrays;
import java.util.Optional;

/**
 * Classe utilitaire pour l'import et l'export de positions d'échecs.
 *
 * <p>Cette classe gère plusieurs formats de représentation des positions :
 * <ul>
 *   <li><b>FEN</b> (Forsyth-Edwards Notation) : format standard lisible</li>
 *   <li><b>LLP</b> (Low Length Position) : format compressé propriétaire</li>
 *   <li><b>HTML</b> : représentation visuelle sous forme de tableau</li>
 * </ul>
 *
 * <h3>Format LLP</h3>
 * <p>Le format LLP encode chaque case sur un seul caractère selon la grille suivante :
 * <pre>
 *   a b c d e f g h
 *   i j k l m n o p
 *   q r s t u v w x
 *   &lt; { 5 6 7 8 } &gt;
 *   [ ( 1 2 3 4 ) ]
 *   Q R S T U V W X
 *   I J K L M N O P
 *   A B C D E F G H
 * </pre>
 *
 * <p>Une position LLP se compose de 4 sections séparées par des tirets :
 * <ol>
 *   <li>Positions des 8 pions blancs</li>
 *   <li>Positions des 8 pions noirs</li>
 *   <li>Positions des 8 pièces blanches (KQRRBBNN) + promotions</li>
 *   <li>Positions des 8 pièces noires (kqrrbbnn) + promotions</li>
 * </ol>
 *
 * @author crios
 * @see GameBoard
 */
class PositionExporter {

    /**
     * Table de correspondance pour encoder une case sur 1 caractère (format LLP).
     * L'indice dans la chaîne correspond à l'indice de la case (0-63).
     */
    private static final String CELLS = "ABCDEFGHIJKLMNOPQRSTUVWX[(1234)]<{5678}>qrstuvwxijklmnopabcdefgh";

    /**
     * Symboles des pions dans l'ordre d'indexation LLP (8 blancs puis 8 noirs).
     */
    private static final String PAWN_POS = "PPPPPPPPpppppppp";

    /**
     * Symboles des pièces (hors pions) dans l'ordre d'indexation LLP.
     * Ordre : Roi, Dame, 2 Tours, 2 Fous, 2 Cavaliers pour chaque couleur.
     */
    private static final String PIECES_POS = "KQRRBBNNkqrrbbnn";

    /**
     * Concaténation des symboles de pions et de pièces pour l'indexation complète.
     */
    private static final String PIECES = PAWN_POS + PIECES_POS;


    private PositionExporter() {

    }

    private static void appendFENPosition(StringBuilder sb, GameBoard g) {

        Optional<BoardCell> cell;
        int emptyCellCount = 0;
        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                cell = Board.getCell(col, row);
                Piece p = g.getPiece(cell.get());
                if (p == null) {
                    emptyCellCount++;
                } else {
                    if (emptyCellCount > 0) {
                        sb.append(String.valueOf(emptyCellCount));
                        emptyCellCount = 0;
                    }
                    sb.append(p.getPosSymbol());
                }
            }

            if (emptyCellCount > 0) {
                sb.append(String.valueOf(emptyCellCount));
                emptyCellCount = 0;
            }

            if (row > 0) {
                sb.append("/");
            }

        }

    }

    private static void appendMlAttribute(StringBuilder sb, final String attributeName, final String... attributeValue) {
        if (attributeValue != null && attributeValue.length > 0) {
            sb.append(" ");
            sb.append(attributeName);
            sb.append("=\"");
            for (String val : attributeValue) {
                sb.append(val);
            }
            sb.append("\"");
        }
    }

    private static void appendBlackHTMLPosition(StringBuilder sb, GameBoard g) {
        sb.append("<table class=\"board\">\n");
        BoardCell cell;
        appendBlackBoardLetters(sb, "top");
        for (int row = 0; row < 8; row++) {
            sb.append("\t<tr>\n");
            String sRow = String.valueOf(row + 1);
            appendBoardLineNumber(sb, "left", sRow, sRow);
            for (int col = 7; col >= 0; col--) {
                // on est jamais en dehors de l'échiquier ici.
                cell = Board.getCell(col, row).get();
                Piece p = g.getPiece(cell);
                sb.append("\t\t<td");
                appendMlAttribute(sb, "id", "board_cell_", cell.getAlgebricPos());
                appendMlAttribute(sb, "class", "board_cell--", cell.getColor().name().toLowerCase());
                if (p != null) {
                    appendMlAttribute(sb, "data", "piece_", p.getColor().name().toLowerCase(), "_", String.valueOf(p.getSymbol()).toLowerCase());
                }
                sb.append(">");
                if (p != null) {
                    sb.append(getHtmlCode(p));
                }
                sb.append("</td>\n");
            }
            appendBoardLineNumber(sb, "right", sRow, sRow);
            sb.append("\t</tr>\n");
        }
        appendBlackBoardLetters(sb, "bottom");
        sb.append("</table>\n");
    }

    private static void appendWhiteHTMLPosition(StringBuilder sb, GameBoard g) {
        sb.append("<table class=\"board\">\n");
        BoardCell cell;
        appendWhiteBoardLetters(sb, "top");
        for (int row = 7; row >= 0; row--) {
            sb.append("\t<tr>\n");
            String sRow = String.valueOf(row + 1);
            appendBoardLineNumber(sb, "left", sRow, sRow);
            for (int col = 0; col < 8; col++) {
                // pas de test Optional on est toujours dans l'échiquier.
                cell = Board.getCell(col, row).get();
                Piece p = g.getPiece(cell);
                sb.append("\t\t<td");
                appendMlAttribute(sb, "id", "board_cell_", cell.getAlgebricPos());
                appendMlAttribute(sb, "class", "board_cell--", cell.getColor().name().toLowerCase());
                if (p != null) {
                    appendMlAttribute(sb, "data", "piece_", p.getColor().name().toLowerCase(), "_", String.valueOf(p.getSymbol()).toLowerCase());
                }
                sb.append(">");
                if (p != null) {
                    sb.append(getHtmlCode(p));
                }
                sb.append("</td>\n");
            }
            appendBoardLineNumber(sb, "right", sRow, sRow);
            sb.append("\t</tr>\n");
        }
        appendWhiteBoardLetters(sb, "bottom");
        sb.append("</table>\n");
    }

    private static void appendBlackBoardLetters(StringBuilder sb, final String side) {
        sb.append("\t<tr>\n");
        appendBoardLineNumber(sb, "corner", side.concat("Left"), "");
        for (char col = 'h'; col >= 'a'; col--) {
            String sCol = String.valueOf(col);
            appendBoardLineNumber(sb, side, sCol, sCol);
        }
        appendBoardLineNumber(sb, "corner", side.concat("Right"), "");
        sb.append("\t</tr>\n");
    }

    private static void appendWhiteBoardLetters(StringBuilder sb, final String side) {
        sb.append("\t<tr>\n");
        appendBoardLineNumber(sb, "corner", side.concat("Left"), "");
        for (char col = 'a'; col <= 'h'; col++) {
            String sCol = String.valueOf(col);
            appendBoardLineNumber(sb, side, sCol, sCol);
        }
        appendBoardLineNumber(sb, "corner", side.concat("Right"), "");
        sb.append("\t</tr>\n");
    }

    private static void appendBoardLineNumber(StringBuilder sb, final String side, final String ref, final String text) {
        sb.append("\t\t<td");
        appendMlAttribute(sb, "id", "board_num_", side, "_", String.valueOf(ref));
        appendMlAttribute(sb, "class", "board_num--", side);
        sb.append(">");
        sb.append(text);
        sb.append("</td>\n");
    }

    private static String getHtmlCode(Piece p) {
        if (p.getColor() == Color.WHITE) {
            switch (p.getSymbol()) {
                case 'P':
                    return "&#9817;";
                case 'B':
                    return "&#9815;";
                case 'N':
                    return "&#9816;";
                case 'K':
                    return "&#9812;";
                case 'Q':
                    return "&#9813;";
                case 'R':
                    return "&#9814;";
            }
        }
        if (p.getColor() == Color.BLACK) {
            switch (p.getSymbol()) {
                case 'P':
                    return "&#9823;";
                case 'B':
                    return "&#9821;";
                case 'N':
                    return "&#9822;";
                case 'K':
                    return "&#9818;";
                case 'Q':
                    return "&#9819;";
                case 'R':
                    return "&#9820;";
            }
        }
        return "";
    }

    private static void appendLLPBoard(GameBoard g, StringBuilder sb) {
        BoardCell cell;
        char[] pieces = PIECES.toCharArray();
        // tableau a remplir des positions de pieces
        char[] result = new char[32];
        Arrays.fill(result, '.');

        //partie variable pour le stockage des pieces supplémentaire dûes à promotion
        String blackExtraPieces = "";
        String whiteExtraPieces = "";

        BoardCell[] cells = BoardCell.values();
        int cellIdx;
        for (int col = 0; col < 8; col++) {
            for (int row = 7; row >= 0; row--) {
                cellIdx = row * 8 + col;
                cell = cells[cellIdx];
                char cellPos = CELLS.charAt(cellIdx);
                Piece piece = g.getPiece(cell);

                if (piece != null) {
                    char c = piece.getPosSymbol();
                    int i = indexOf(pieces, c);

                    if (i >= 0) {
                        pieces[i] = ' ';
                        result[i] = cellPos;

                    } else {
                        // piece supplémentaire (promotion)
                        // prom = valeur indiquant le type de piece obtenue par promotion
                        //int prom = indexOf(PROMOTION_PIECES, c);
                        if (piece.getColor() == Color.BLACK) {
                            blackExtraPieces += c + cellPos;

                        } else {
                            whiteExtraPieces += c + cellPos;

                        }
                    }
                }
            }
        }
        String sResult = String.valueOf(result);
        //pions blancs
        sb.append(String.valueOf(sResult.substring(0, 8)));
        sb.append("-");
        //pions noirs
        sb.append(String.valueOf(sResult.substring(8, 16)));
        sb.append("-");
        // pieces blanches
        sb.append(String.valueOf(sResult.substring(16, 24)));
        sb.append(whiteExtraPieces);
        sb.append("-");
        // pieces noires
        sb.append(String.valueOf(sResult.substring(24, 32)));
        sb.append(blackExtraPieces);
    }

    private static int indexOf(char[] value, char ch) {
        for (int i = 0; i < value.length; i++) {
            if (value[i] == ch) {
                return i;
            }
        }
        return -1;
    }

    private static int lastIndexOf(char[] value, char ch, int start) {
        for (int i = start; i > 0; i--) {
            if (value[i] == ch) {
                return i;
            }
        }
        return -1;
    }

    private static void appendTurnColor(StringBuilder sb, GameBoard g) {
        if (g.getTurnColor() == Color.BLACK) {
            sb.append("b");
        } else {
            sb.append("w");
        }
    }

    private static void appendCastleInfo(StringBuilder sb, GameBoard g) {

        boolean castle = false;

        if (g.isWhiteCanCastleKingSide()) {
            sb.append("K");
            castle = true;
        }

        if (g.isWhiteCanCastleQueenSide()) {
            sb.append("Q");
            castle = true;
        }

        if (g.isBlackCanCastleKingSide()) {
            sb.append("k");
            castle = true;
        }

        if (g.isBlackCanCastleQueenSide()) {
            sb.append("q");
            castle = true;
        }

        if (!castle) {
            sb.append("-");
        }

    }

    private static void appendEnPassantInfo(StringBuilder sb, GameBoard g) {
        BoardCell enPassantCell = g.getEnPassantCell();
        if (enPassantCell == null) {
            sb.append("-");
        } else {
            sb.append(enPassantCell.getAlgebricPos());
        }
    }

    private static void importLLPBoard(GameBoard board, final String boardPosition) throws InvalidPositionException {
        String[] posArray = boardPosition.split("-");
        if (posArray.length != 4) {
            throw new InvalidPositionException("Position %s invalide.", boardPosition);
        }

        try {
            String sBoardPos = posArray[0] + posArray[1] + posArray[2].substring(0, 8) + posArray[3].substring(0, 8);
            for (int i = 0; i < 32; i++) {
                char cPiece = PIECES.charAt(i);
                char cPos = sBoardPos.charAt(i);
                if (cPos != '.') {
                    int cellIdx = CELLS.indexOf(cPos);
                    int line = cellIdx / 8;
                    int col = cellIdx % 8;
                    board.addPiece(cPiece, col, line);
                }
            }
            addPromotedPieces(board, posArray[2]);
            addPromotedPieces(board, posArray[3]);

        } catch (InvalidPieceException | InvalidCellException e) {
            throw new InvalidPositionException(e,"Position %s invalide.", boardPosition);

        }

    }

    private static void addPromotedPieces(GameBoard board, String promotedPiece) throws InvalidPieceException, InvalidCellException {
        for (int i = 8; i < promotedPiece.length() - 1; i += 2) {
            char cPiece = promotedPiece.charAt(i);
            char cPos = promotedPiece.charAt(i + 1);
            int cellIdx = CELLS.indexOf(cPos);
            int line = cellIdx / 8;
            int col = cellIdx % 8;
            board.addPiece(cPiece, col, line);
        }
    }

    private static void importFenBoard(GameBoard board, final String boardPosition) throws InvalidPositionException {
        try {
            String[] lines = boardPosition.split("/");
            int lineNum = 7;
            for (String line : lines) {
                int col = 0;
                int p = 0;
                while (col < 8) {
                    Character c = line.charAt(p);
                    if (c > '0' && c < '9') {
                        col += Integer.parseInt(String.valueOf(c));
                    } else {
                        board.addPiece(c, col, lineNum);
                        col++;
                    }
                    p++;
                }
                lineNum--;
            }
        } catch (InvalidPieceException | InvalidCellException e) {
            throw new InvalidPositionException(e, "Position FEN %s invalide");
        }
    }

    /**
     * Importe une position au format LLP dans l'échiquier.
     *
     * @param board l'échiquier à remplir
     * @param boardPosition la position au format LLP
     * @throws InvalidPositionException si la position est invalide
     */
    static void importLLP(final GameBoard board, final String boardPosition) throws InvalidPositionException {
        importLLPBoard(board, boardPosition);
    }

    /**
     * Importe une position au format FEN dans l'échiquier.
     *
     * <p>Le format FEN comprend :
     * <ul>
     *   <li>La position des pièces</li>
     *   <li>Le trait (w/b)</li>
     *   <li>Les droits de roque</li>
     *   <li>La case de prise en passant</li>
     * </ul>
     *
     * @param board l'échiquier à remplir
     * @param fenPosition la position au format FEN
     * @throws InvalidPositionException si la position FEN est invalide
     */
    public static void importFEN(final GameBoard board, final String fenPosition) throws InvalidPositionException {
        try {
            String[] gameData = fenPosition.split(" ");
            importFenBoard(board, gameData[0]);
            board.setNextTurnColor(gameData[1]);
            board.setCastleInfo(gameData[2]);
            if (!"-".equals(gameData[3])) {
                board.setEnPassantCell(gameData[3]);
            }
        } catch (InvalidPieceColorException | InvalidCellException e) {
            throw new InvalidPositionException(e, "Position FEN %s invalide", fenPosition);
        }
    }

    /**
     * Exporte la position de l'échiquier au format HTML.
     *
     * @param g l'échiquier à exporter
     * @param color le point de vue (blancs ou noirs en bas)
     * @return la représentation HTML de l'échiquier
     */
    static String exportHTML(GameBoard g, Color color) {
        StringBuilder sb = new StringBuilder(100);
        if (color == Color.BLACK) {
            appendBlackHTMLPosition(sb, g);
        } else {
            appendWhiteHTMLPosition(sb, g);
        }
        return sb.toString();
    }

    /**
     * Exporte la position de l'échiquier au format FEN.
     *
     * @param g l'échiquier à exporter
     * @return la position au format FEN
     */
    public static String exportFEN(GameBoard g) {
        StringBuilder sb = new StringBuilder(100);
        appendFENPosition(sb, g);
        sb.append(" ");
        appendTurnColor(sb, g);
        sb.append(" ");
        appendCastleInfo(sb, g);
        sb.append(" ");
        appendEnPassantInfo(sb, g);
        return sb.toString();
    }

    /**
     * Exporte la position de l'échiquier au format LLP.
     *
     * @param g l'échiquier à exporter
     * @return la position au format LLP
     */
    public static String exportLLP(GameBoard g) {
        StringBuilder sb = new StringBuilder();
        appendLLPBoard(g, sb);
        return sb.toString();
    }

}
