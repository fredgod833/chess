package com.fgodard.chess.board;

import com.fgodard.chess.board.pieces.King;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class MoveHelper {

    public static void addElementsFromDirection(GameBoard board,
                                                BoardCell orgCell,
                                                Color playerColor,
                                                int colIncrement, int lineIncrement,
                                                Collection<BoardCell> destCells,
                                                Collection<Piece> targetPieces) {
        for (int i = 1; i < 8; i++) {
            Optional<BoardCell> nextCell = Board.getCell(orgCell, colIncrement * i, lineIncrement * i);
            if (nextCell.isEmpty()) {
                return;
            }
            Piece piece = board.getPiece(nextCell.get());
            if (piece == null) {
                if (destCells != null) {
                    destCells.add(nextCell.get());
                }
            } else {
                if (piece.getColor() != playerColor) {
                    if (destCells != null) {
                        destCells.add(nextCell.get());
                    }
                    if (targetPieces != null) {
                        targetPieces.add(piece);
                    }
                }
                break;
            }
        }

    }

    public static void addElementsFromDirection(Piece orgPiece,
                                                int colIncrement, int lineIncrement,
                                                Collection<BoardCell> destCells,
                                                Collection<Piece> targetPieces) {

        addElementsFromDirection(orgPiece.getCurrentBoard(), orgPiece.getCell(), orgPiece.getColor(), colIncrement, lineIncrement, destCells, targetPieces);

    }

    public static void addRelativeElement(GameBoard board,
                                          BoardCell orgCell,
                                          Color playerColor,
                                          int colIncrement, int lineIncrement,
                                          Collection<BoardCell> destCells,
                                          Collection<Piece> targetPieces) {

        Optional<BoardCell> nextCell = Board.getCell(orgCell, colIncrement, lineIncrement);
        if (nextCell.isEmpty()) {
            return;
        }
        Piece piece = board.getPiece(nextCell.get());
        if (piece == null) {
            if (destCells != null) {
                destCells.add(nextCell.get());
            }
        } else {
            if (piece.getColor() != playerColor) {
                if (destCells != null) {
                    destCells.add(nextCell.get());
                }
                if (targetPieces != null) {
                    targetPieces.add(piece);
                }
            }
        }

    }

    public static void addRelativeElement(Piece orgPiece,
                                          int colIncrement, int lineIncrement,
                                          Collection<BoardCell> destCells,
                                          Collection<Piece> targetPieces) {

        addRelativeElement(orgPiece.getCurrentBoard(), orgPiece.getCell(), orgPiece.getColor(), colIncrement, lineIncrement, destCells, targetPieces);

    }

    /**
     * Recherche les pieces qui attaquent une case de l'échiquier contre le joueur
     * @param board l'echiquier en cours
     * @param orgCell la case
     * @param playerColor la couleur du joueur
     * @param filter filtre les pieces  (optimise la recherche) (exemple : "RBQ")
     * @return la liste des pieces
     */
    public Collection<Piece> findAttackingPieces(GameBoard board, BoardCell orgCell, Color playerColor, final String filter) {

        ArrayList<Piece> result = new ArrayList<>();
        if (filter.contains("B") || filter.contains("Q")) {
            addElementsFromDirection(board, orgCell, playerColor, -1, 0, null, result);
            addElementsFromDirection(board, orgCell, playerColor, 0, -1, null, result);
            addElementsFromDirection(board, orgCell, playerColor, 0, 1, null, result);
            addElementsFromDirection(board, orgCell, playerColor, 1, 0, null, result);
        }
        Piece[] rookOrQueen = (Piece[]) result.stream()
                .filter(p -> ("RQ".indexOf(p.getSymbol())>=0) && (filter.indexOf(p.getSymbol())>=0)).toArray();

        result.clear();
        if (filter.contains("B") || filter.contains("Q")) {
            addElementsFromDirection(board, orgCell, playerColor, -1, -1, null, result);
            addElementsFromDirection(board, orgCell, playerColor, -1, 1, null, result);
            addElementsFromDirection(board, orgCell, playerColor, 1, -1, null, result);
            addElementsFromDirection(board, orgCell, playerColor, 1, 1, null, result);
        }
        Piece[] bishopOrQueen = (Piece[]) result.stream()
                .filter(p -> ("BQ".indexOf(p.getSymbol()) >= 0) && (filter.indexOf(p.getSymbol())>=0)).toArray();

        result.clear();
        if (filter.contains("N")) {
            addRelativeElement(board, orgCell, playerColor, 1, 2, null, result);
            addRelativeElement(board, orgCell, playerColor, 2, 1, null, result);
            addRelativeElement(board, orgCell, playerColor, 2, -1, null, result);
            addRelativeElement(board, orgCell, playerColor, 1, -2, null, result);
            addRelativeElement(board, orgCell, playerColor, -1, -2, null, result);
            addRelativeElement(board, orgCell, playerColor, -2, -1, null, result);
            addRelativeElement(board, orgCell, playerColor, -2, 1, null, result);
            addRelativeElement(board, orgCell, playerColor, -1, 2, null, result);
        }
        Piece[] knights = (Piece[]) result.stream().filter(p -> p.getSymbol() == 'N').toArray();

        result.clear();
        if (filter.contains("P")) {
            if (playerColor.equals(Color.WHITE)) {
                addRelativeElement(board, orgCell, playerColor, -1, 1, null, result);
                addRelativeElement(board, orgCell, playerColor, 1, 1, null, result);
            } else {
                addRelativeElement(board, orgCell, playerColor, -1, -1, null, result);
                addRelativeElement(board, orgCell, playerColor, 1, -1, null, result);
            }
        }
        Piece[] pawns = (Piece[]) result.stream().filter(p -> p.getSymbol() == 'P').toArray();

        result.clear();
        if (filter.contains("K")) {
            addRelativeElement(board, orgCell, playerColor,-1, -1, null, result);
            addRelativeElement(board, orgCell, playerColor,-1, 0, null, result);
            addRelativeElement(board, orgCell, playerColor,-1, 1, null, result);
            addRelativeElement(board, orgCell, playerColor,0, -1, null, result);
            addRelativeElement(board, orgCell, playerColor,0, 1, null, result);
            addRelativeElement(board, orgCell, playerColor,1, -1, null, result);
            addRelativeElement(board, orgCell, playerColor,1, 0, null, result);
            addRelativeElement(board, orgCell, playerColor,1, 1, null, result);
        }
        Piece[] king = (Piece[]) result.stream().filter(p -> p.getSymbol() == 'K').toArray();

        return Arrays.asList(mergePiecesArrays(rookOrQueen, bishopOrQueen, knights, pawns, king));
    }

    public Collection<Piece> findAttackingPieces(King king) {
        return findAttackingPieces(king, null);
    }

    public Collection<Piece> findAttackingPieces(King king, Character lastPieceMoved) {

        String filter;
        if (lastPieceMoved == null) {
            filter = "RBNQP";
        } else if (lastPieceMoved == 'N' || lastPieceMoved == 'P') {
            filter = "RBQ"+lastPieceMoved;
        } else {
            filter = "RBQ";
        }

        return findAttackingPieces(king.getCurrentBoard(), king.getCell(), king.getColor(), filter);
    }

    private static Piece[] mergePiecesArrays(Piece[]... piecesArray) {

        int count = 0;
        for (Piece[] pieces : piecesArray) {
            count += pieces.length;
        }

        Piece[] result = new Piece[count];
        count = 0;
        for (Piece[] pieces : piecesArray) {
            System.arraycopy(pieces, 0, result, count, pieces.length);
            count += pieces.length;
        }

        return result;
    }

}
