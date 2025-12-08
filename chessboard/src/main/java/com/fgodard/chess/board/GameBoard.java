package com.fgodard.chess.board;

import com.fgodard.chess.beans.BoardState;
import com.fgodard.chess.beans.Ply;
import com.fgodard.chess.beans.Position;
import com.fgodard.chess.board.pieces.*;
import com.fgodard.chess.exception.InvalidCellException;
import com.fgodard.chess.exception.InvalidMoveException;
import com.fgodard.chess.exception.InvalidPieceColorException;
import com.fgodard.chess.exception.InvalidPieceException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by crios on 23/04/23.
 */
public class GameBoard {

    private static final Pattern Castle960Pattern = Pattern.compile("[a-hA-H]{1}");

    private boolean chess960;

    private final Piece[] boardMap = new Piece[64];

    private final ArrayList<Piece> whitePiecesList = new ArrayList<>(16);
    private final ArrayList<Piece> blackPiecesList = new ArrayList<>(16);

    private boolean whiteCanCastleKingSide;
    private boolean whiteCanCastleQueenSide;
    private boolean blackCanCastleKingSide;
    private boolean blackCanCastleQueenSide;

    private Color turnColor;

    private BoardCell enPassantCell;

    private Pawn enPassantPawn;

    private int plyNo = 0;

    private int currentMove = 0;

    private int plyNoSincePawn = 0;

    private String fen;

    private String llp;

    private String html;

    private Position pos;

    private BoardState state = null;

    private void clearCells() {
        whitePiecesList.clear();
        blackPiecesList.clear();
        //pieceBoardMap.clear();
        for (int i = 0; i < 64; i++) {
            boardMap[i] = null;
        }
        plyNoSincePawn = 0;
        plyNo = 0;
        currentMove = 0;
        turnColor = null;
        clearExportPosition();
    }

    private Piece buildPiece(final Character piece) throws InvalidPieceException {
        char up = Character.toUpperCase(piece);
        switch (up) {
            case 'B' : {
                return new Bishop();
            }
            case 'K' : {
                return new King();
            }
            case 'N' : {
                return new Knight();
            }
            case 'P' : {
                return new Pawn();
            }
            case 'Q' : {
                return new Queen();
            }
            case 'R' : {
                return new Rook();
            }
            default : throw new InvalidPieceException("Piece invalide (%s).", piece);
        }

    }

    public Piece getPiece(final String alpha) throws InvalidCellException {
        if (alpha == null) {
            return null;
        }
        BoardCell cell = Board.getCell(alpha);
        return boardMap[cell.getIdx()];
    }

    public Piece getPiece(BoardCell cell) {
        if (cell == null) {
            return null;
        }
        return boardMap[cell.getIdx()];
    }

    private void nextTurn() {
        if (turnColor == null) {
            return;
        }
        switch (turnColor) {
            case BLACK: {
                turnColor = Color.WHITE;
                currentMove++;
                break;
            }
            case WHITE: {
                turnColor = Color.BLACK;
                break;
            }
        }
    }

    public void resetForNullMove() {
        switch (turnColor) {
            case BLACK: {
                turnColor = Color.WHITE;
                break;
            }
            case WHITE: {
                turnColor = Color.BLACK;
                break;
            }
        }
        currentMove = 0;
        plyNo = 0;
    }

    private Piece removePiece(BoardCell cell) {
        Piece p = boardMap[cell.getIdx()];
        boardMap[cell.getIdx()] = null;
        if (p == null) {
            return p;
        }
        if (p.getColor() == Color.WHITE) {
            whitePiecesList.remove(p);
        }
        if (p.getColor() == Color.BLACK) {
            blackPiecesList.remove(p);
        }
        p.setCell(null);
        return p;
    }

    private void removePiece(Piece piece) {
        if (piece.getColor() == Color.BLACK) {
            blackPiecesList.remove(piece);
        }
        if (piece.getColor() == Color.WHITE) {
            whitePiecesList.remove(piece);
        }
        BoardCell cell = piece.getCell();
        boardMap[cell.getIdx()] = null;
        piece.setCell(null);
    }

    private void movePiece(Piece pieceToMove, BoardCell destCell, Piece promPiece) {
        resetExportPostions();
        enPassantCell = null;
        enPassantPawn = null;
        if (promPiece != null) {
            removePiece(pieceToMove);
            addPiece(promPiece, destCell);
            plyNoSincePawn = 0;

        } else {
            BoardCell currentCell = pieceToMove.getCell();
            boardMap[currentCell.getIdx()] = null;
            boardMap[destCell.getIdx()] = pieceToMove;
            pieceToMove.setCell(destCell);
            if (pieceToMove instanceof Pawn) {
                int d = destCell.getLineIdx() - currentCell.getLineIdx();
                if ((d == 2 || d == -2) && (hasSidePawn(destCell, -1) || hasSidePawn(destCell, +1))) {
                    Optional<BoardCell> enPassantCell = Board.getCell(destCell.getColIdx(), (destCell.getLineIdx() + currentCell.getLineIdx()) / 2);
                    if (enPassantCell.isPresent()) {
                        setEnPassantCell(enPassantCell.get());
                        setEnPassantPawn((Pawn) pieceToMove);
                    }
                }
                plyNoSincePawn = 0;
            } else {
                plyNoSincePawn++;
            }
        }
        plyNo++;
    }

    private boolean hasSidePawn(BoardCell destCell, int i) {

        Optional<BoardCell> sideCell = Board.getCell(destCell.getColIdx() + i, destCell.getLineIdx());
        if (!sideCell.isPresent()) {
            return false;
        }
        Piece sidePiece = this.getPiece(sideCell.get());
        if (sidePiece != null && sidePiece instanceof Pawn) {
            return true;
        }
        return false;

    }

    private Piece getPieceToTake(Piece pieceToMove, BoardCell cell) {

        Piece pieceToTake;
        if (cell == enPassantCell && pieceToMove instanceof Pawn) {
            pieceToTake = enPassantPawn;
        } else {
            pieceToTake = getPiece(cell);
        }
        return pieceToTake;

    }

    private Collection<Piece> getPiecesForDestination(Color color, Character pieceSymbol, BoardCell cell) {

        Collection<Piece> result = new ArrayList<>();
        if (color == null || color == Color.WHITE) {
            appendPiecesForDestination(result, cell, whitePiecesList, pieceSymbol);
        }
        if (color == null || color == Color.BLACK) {
            appendPiecesForDestination(result, cell, blackPiecesList, pieceSymbol);
        }
        return result;

    }

    private void appendPiecesForDestination(Collection<Piece> result, BoardCell cell, Collection<Piece> pieceList, char pieceSymbol) {

        for (Piece p : pieceList) {
            if (pieceSymbol == p.getSymbol() && p.getMoveCells().contains(cell)) {
                result.add(p);
            }
        }

    }

    private Piece getPieceToMove(Character piece, String orgPos, String destCell) throws InvalidMoveException {

        BoardCell cell;
        try {
            cell = Board.getCell(destCell);
            Collection<Piece> pieces = getPiecesForDestination(getTurnColor(), piece, cell);

            if (orgPos != null && !orgPos.isEmpty()) {
                Stream<Piece> pStream;
                final String origPos = orgPos;
                char c = origPos.charAt(0);
                if (c >= '1' && c <= '8') {
                    pStream = pieces.stream().filter(p -> p.getCell().getAlgebricPos().endsWith(origPos));
                } else {
                    pStream = pieces.stream().filter(p -> p.getCell().getAlgebricPos().startsWith(origPos));
                }
                pieces = pStream.collect(Collectors.toCollection(ArrayList::new));
                if (pieces.size() == 1) {
                    return pieces.iterator().next();
                }

            }

            if (pieces.size() == 1) {
                return pieces.iterator().next();

            } else {
                System.out.println(orgPos);
                System.out.println(piece);
                System.out.println(cell);
                System.out.println(pieces);

            }

            throw new InvalidMoveException("Mouvement invalide (%1$s %2$s -> %3$s)", piece, orgPos, destCell);

        } catch (InvalidCellException e) {

            throw new InvalidMoveException(e, "Mouvement invalide (%1$s %2$s -> %3$s)", piece, orgPos, destCell);

        }

    }

    public void addPiece(final Character piece, int col, int line) throws InvalidPieceException {

        Optional<BoardCell> cell = Board.getCell(col, line);
        Piece p = buildPiece(piece);
        p.setCurrentBoard(this);
        p.setCell(cell.get());
        if (piece > 'A' && piece < 'Z') {
            p.setColor(Color.WHITE);
            whitePiecesList.add(p);
        } else {
            p.setColor(Color.BLACK);
            blackPiecesList.add(p);
        }

        boardMap[cell.get().getIdx()] = p;

    }

    private void addPiece(final Piece piece, BoardCell cell) {

        piece.setCurrentBoard(this);
        if (piece.getColor() == Color.WHITE) {
            whitePiecesList.add(piece);
        }
        if (piece.getColor() == Color.BLACK) {
            blackPiecesList.add(piece);
        }
        piece.setCell(cell);
        boardMap[cell.getIdx()] = piece;

    }

    private void castleBlackKingSide() throws InvalidMoveException {

        if (!blackCanCastleKingSide) {
            throw new InvalidMoveException("Rocque noir interdit coté roi. (%s)", plyNo);
        }
        King k = (King) removePiece(BoardCell.E8);
        Rook r = (Rook) getPiece(BoardCell.H8);
        if (k == null || r == null) {
            throw new InvalidMoveException("Rocque noir interdit coté roi. (%s)", plyNo);
        }
        movePiece(r, BoardCell.F8, null);
        addPiece(k, BoardCell.G8);

    }

    private void castleBlackQueenSide() throws InvalidMoveException {

        if (!blackCanCastleQueenSide) {
            throw new InvalidMoveException("Rocque noir interdit coté reine. (%s)", plyNo);
        }
        King k = (King) removePiece(BoardCell.E8);
        Rook r = (Rook) getPiece(BoardCell.A8);
        if (k == null || r == null) {
            throw new InvalidMoveException("Rocque noir interdit coté reine. (%s)", plyNo);
        }
        movePiece(r, BoardCell.D8, null);
        addPiece(k, BoardCell.C8);

    }

    private void castleWhiteKingSide() throws InvalidMoveException {

        if (!whiteCanCastleKingSide) {
            throw new InvalidMoveException("Rocque blanc interdit coté roi. (%s)", plyNo);
        }
        King k = (King) removePiece(BoardCell.E1);
        Rook r = (Rook) getPiece(BoardCell.H1);
        if (k == null || r == null) {
            throw new InvalidMoveException("Rocque blanc interdit coté roi. (%s)", plyNo);
        }
        movePiece(r, BoardCell.F1, null);
        addPiece(k, BoardCell.G1);

    }

    private void castleWhiteQueenSide() throws InvalidMoveException {

        if (!whiteCanCastleQueenSide) {
            throw new InvalidMoveException("Rocque blanc interdit coté reine. (%s)", plyNo);
        }
        King k = (King) removePiece(BoardCell.E1);
        Rook r = (Rook) getPiece(BoardCell.A1);
        if (k == null || r == null) {
            throw new InvalidMoveException("Rocque blanc interdit coté reine. (%s)", plyNo);
        }
        movePiece(r, BoardCell.D1, null);
        addPiece(k, BoardCell.C1);

    }

    private void castle(BoardSide side) throws InvalidMoveException {

        if (turnColor == Color.BLACK) {
            switch (side) {
                case KING:
                    castleBlackKingSide();
                    break;
                case QUEEN:
                    castleBlackQueenSide();
                    break;
            }
            blackCanCastleKingSide = false;
            blackCanCastleQueenSide = false;

        } else if (turnColor == Color.WHITE) {
            switch (side) {
                case KING:
                    castleWhiteKingSide();
                    break;
                case QUEEN:
                    castleWhiteQueenSide();
                    break;
            }
            whiteCanCastleKingSide = false;
            whiteCanCastleQueenSide = false;
        }

    }

    private void movePiece(Ply ply) throws InvalidMoveException {

        BoardCell destCell;
        try {
            destCell = Board.getCell(ply.getDestCell());

        } catch (InvalidCellException e) {
            throw new InvalidMoveException(e, "Mouvement invalide (%1$s %2$s -> %3$s)", ply.getPiece(), ply.getOrigCell(), ply.getDestCell());

        }

        Piece promPiece = null;
        Piece pieceToMove = getPieceToMove(ply.getPiece(), ply.getOrigCell(), ply.getDestCell());

        //Complete le déplacement avec détermination de la case d'origine et de la couleur
        ply.setOrigCell(pieceToMove.getCell().getAlgebricPos());
        ply.setColor(pieceToMove.getColor());

        //Complete si prise en passant
        if (enPassantCell != null && pieceToMove instanceof Pawn && ply.getDestCell().equals(enPassantCell.getAlgebricPos())) {
            ply.setEnPassant(true);
        } else {
            ply.setEnPassant(false);
        }

        // Validité du Roque
        if (pieceToMove instanceof King) {
            if (pieceToMove.getColor() == Color.BLACK) {
                blackCanCastleKingSide = false;
                blackCanCastleQueenSide = false;

            } else if (pieceToMove.getColor() == Color.WHITE) {
                whiteCanCastleKingSide = false;
                whiteCanCastleQueenSide = false;

            }

        } else if (pieceToMove instanceof Rook) {
            if (pieceToMove.getColor() == Color.BLACK) {
                if (ply.getOrigCell().equals("a8")) {
                    blackCanCastleQueenSide = false;

                } else if (ply.getOrigCell().equals("h8")) {
                    blackCanCastleKingSide = false;

                }
            } else if (pieceToMove.getColor() == Color.WHITE) {
                if (ply.getOrigCell().equals("a1")) {
                    whiteCanCastleQueenSide = false;

                } else if (ply.getOrigCell().equals("h1")) {
                    whiteCanCastleKingSide = false;

                }
            }
        }

        if (ply.getPromotion() != null) {
            try {
                promPiece = buildPiece(ply.getPromotion());
                promPiece.setCurrentBoard(this);
                promPiece.setColor(pieceToMove.getColor());

            } catch (InvalidPieceException e) {
                throw new InvalidMoveException(e, "Mouvement invalide (%1$s %2$s -> %3$s => %4$s)", ply.getPiece(), ply.getOrigCell(), ply.getDestCell(), ply.getPromotion());

            }
        }

        Piece pieceToTake = getPieceToTake(pieceToMove, destCell);
        if (pieceToTake != null) {
            removePiece(pieceToTake);
            ply.setTake(true);
        }

        movePiece(pieceToMove, destCell, promPiece);

    }

    private void clearExportPosition() {
        this.fen = null;
        this.llp = null;
        this.html = null;
    }

    public void move(Ply ply) throws InvalidMoveException {

        if (turnColor == null && ply.getColor() != null) {
            turnColor = ply.getColor();
        }

        if (ply.isKingCastle()) {
            castle(BoardSide.KING);
            ply.setColor(turnColor);

        } else if (ply.isQueenCastle()) {
            castle(BoardSide.QUEEN);
            ply.setColor(turnColor);

        } else {
            movePiece(ply);

        }

        clearExportPosition();

        nextTurn();

    }

    public boolean isWhiteCanCastleKingSide() {
        return whiteCanCastleKingSide;
    }

    public boolean isWhiteCanCastleQueenSide() {
        return whiteCanCastleQueenSide;
    }

    public boolean isBlackCanCastleKingSide() {
        return blackCanCastleKingSide;
    }

    public boolean isBlackCanCastleQueenSide() {
        return blackCanCastleQueenSide;
    }

    public void setTurnColor(Color turnColor) {
        this.turnColor = turnColor;
        clearExportPosition();
    }

    public Color getTurnColor() {
        return turnColor;
    }

    public int getPlyNo() {
        return plyNo;
    }

    protected int getCurrentMove() {
        return currentMove;
    }

    public BoardCell getEnPassantCell() {
        return enPassantCell;
    }

    private void setEnPassantCell(BoardCell enPassantCell) {
        this.enPassantCell = enPassantCell;
        clearExportPosition();
    }

    private Pawn getEnPassantPawn() {
        return enPassantPawn;
    }

    private void setEnPassantPawn(Pawn enPassantPawn) {

        this.enPassantPawn = enPassantPawn;
        clearExportPosition();

    }

    public boolean isChess960() {
        return chess960;
    }

    void setEnPassantCell(final String enPassant) throws InvalidCellException {

        if (enPassant == null || enPassant.length() < 2) {
            enPassantCell = null;
            return;
        }

        BoardCell cell = Board.getCell(enPassant);
        setEnPassantCell(cell);
        int line = cell.getLineIdx();
        int col = cell.getColIdx();
        if (line == 5) {
            line = 4;
        }
        if (line == 2) {
            line = 3;
        }

        Optional<BoardCell> pawnCell = Board.getCell(col, line);
        enPassantPawn = (Pawn) getPiece(pawnCell.get());

    }

    void setCastleInfo(final String castleInfos) {

        Matcher m = Castle960Pattern.matcher(castleInfos);
        if (m.find()) {
            this.chess960 = true;

        } else {
            this.chess960 = false;
            whiteCanCastleKingSide = castleInfos.contains("K");
            whiteCanCastleQueenSide = castleInfos.contains("Q");
            blackCanCastleKingSide = castleInfos.contains("k");
            blackCanCastleQueenSide = castleInfos.contains("q");
        }
        clearExportPosition();

    }

    void setNextTurnColor(final String color) throws InvalidPieceColorException {

        if (color.equals("w")) {
            this.turnColor = Color.WHITE;
        } else if (color.equals("b")) {
            this.turnColor = Color.BLACK;
        } else {
            throw new InvalidPieceColorException("Couleur de trait invalide (%s).", color);
        }
        clearExportPosition();

    }

    private void resetExportPostions() {
        fen = null;
        llp = null;
        html = null;
        pos = null;
        state = null;
    }

    public void importFEN(final String position) throws InvalidPieceColorException, InvalidCellException, InvalidPieceException {

        clearCells();
        PositionExporter.importFEN(this, position);
        //this.fen = position;

    }

    public String exportFEN() {

        if (fen != null) {
            return fen;
        }
        fen = PositionExporter.exportFEN(this);
        return fen;

    }

    public void importLLP(final String position) throws InvalidPieceColorException, InvalidCellException, InvalidPieceException {
        clearCells();
        PositionExporter.importLLP(this, position);
        llp = position;

    }

    public String exportLLP() {

        if (llp != null) {
            return llp;
        }
        llp = PositionExporter.exportLLP(this);
        return llp;

    }

    public Position exportPosition() {

        if (pos != null) {
            return pos;
        }
        pos = new Position();
        pos.setPosition(exportLLP());
        pos.setTurnColor(this.getTurnColor());

        if (state == null) {
            state = new BoardState();
            state.setEpCell(this.enPassantCell == null ? null : enPassantCell.getAlgebricPos());
            state.setWhiteCanCastleKingSide(this.whiteCanCastleKingSide);
            state.setWhiteCanCastleQueenSide(this.whiteCanCastleQueenSide);
            state.setBlackCanCastleKingSide(this.blackCanCastleKingSide);
            state.setBlackCanCastleQueenSide(this.blackCanCastleQueenSide);
        }
        pos.setBoardState(state);
        return pos;

    }

    public String exportHTML(Color color) {

        html = PositionExporter.exportHTML(this, color);
        return html;

    }

    public void applyUci(final String uci) throws InvalidMoveException {

        if (uci == null || uci.length() < 4) {
            throw new InvalidMoveException("uci %s invalid", uci);
        }

        BoardCell orgCell;
        BoardCell destCell;

        try {
            orgCell = BoardCell.valueOf(uci.substring(0, 2).toUpperCase());
            destCell = BoardCell.valueOf(uci.substring(2, 4).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidMoveException(e, "uci %1$s have invalid cells", uci);
        }

        Piece pieceToMove = getPiece(orgCell);
        if (pieceToMove == null) {
            throw new InvalidMoveException("uci %1$s invalid, no piece at %2$s", uci, orgCell.name());
        }
        Piece destPiece = getPiece(destCell);

        Ply ply = new Ply();

        if (pieceToMove instanceof King && orgCell.getCol() == 'e') {
            char destCol = destCell.getCol();
            if (destCol == 'g' || destCol == 'h') {
                ply.setKingCastle(true);
                move(ply);
                return;
            }
            if (destCol == 'c' || destCol == 'a') {
                ply.setQueenCastle(true);
                move(ply);
                return;
            }
        }

        ply.setPiece(pieceToMove.getSymbol());
        ply.setOrigCell(orgCell.name().toLowerCase());
        ply.setDestCell(destCell.name().toLowerCase());
        ply.setTake(destPiece != null);
        if (destCell == getEnPassantCell()) {
            ply.setTake(true);
        }
        if (uci.length() == 5) {
            ply.setPromotion(uci.charAt(4));
        }

        //TODO
        //ply.setChess("+".equals(m.group(7)));
        //ply.setMat("#".equals(m.group(8)));

        move(ply);
    }

}

