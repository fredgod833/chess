package com.fgodard.chess.board.pieces;

import com.fgodard.chess.board.BoardCell;
import com.fgodard.chess.board.Color;
import com.fgodard.chess.board.MoveHelper;
import com.fgodard.chess.board.Piece;

import java.util.ArrayList;
import java.util.Collection;

import static com.fgodard.chess.board.MoveHelper.addRelativeElement;

/**
 * Représente le roi aux échecs.
 *
 * <p>Le roi peut se déplacer d'une case dans toutes les directions
 * (horizontale, verticale et diagonale), mais ne peut pas se mettre en échec.
 *
 * <p>Contrairement aux autres pièces, le calcul des cases de destination
 * du roi exclut les cases contrôlées par l'adversaire.
 *
 * @author crios
 * @see Piece
 */
public class King extends Piece {

    /**
     * {@inheritDoc}
     *
     * <p>Pour le roi, cette méthode filtre les cases attaquées par l'adversaire
     * afin d'empêcher le roi de se mettre en échec.
     */
    @Override
    public Collection<BoardCell> getMoveCells() {
        
        Collection<BoardCell> freeCells = new ArrayList<>();
        addRelativeElement(this, -1, -1, freeCells, null);
        addRelativeElement(this, -1, 0, freeCells, null);
        addRelativeElement(this, -1, 1, freeCells, null);
        addRelativeElement(this, 0, -1, freeCells, null);
        addRelativeElement(this, 0, 1, freeCells, null);
        addRelativeElement(this, 1, -1, freeCells, null);
        addRelativeElement(this, 1, 0, freeCells, null);
        addRelativeElement(this, 1, 1, freeCells, null);
        Collection<BoardCell> result = new ArrayList<>();
        Collection<Piece> attackingPieces;
        for (BoardCell cell: freeCells) {
            attackingPieces = MoveHelper.findAttackingPieces(this.getCurrentBoard(), cell, this.getColor(), "RBNQP");
            if (attackingPieces == null || attackingPieces.isEmpty()) {
                result.add(cell);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @return 'K' pour King (Roi)
     */
    @Override
    public char getSymbol() {
        return 'K';
    }

}
