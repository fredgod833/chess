package com.fgodard.chess.board.pieces;

import com.fgodard.chess.board.BoardCell;
import com.fgodard.chess.board.Piece;

import java.util.ArrayList;
import java.util.Collection;

import static com.fgodard.chess.board.MoveHelper.addRelativeElement;

/**
 * Représente le cavalier aux échecs.
 *
 * <p>Le cavalier se déplace en "L" : deux cases dans une direction
 * (horizontale ou verticale) puis une case perpendiculairement.
 *
 * <p>C'est la seule pièce qui peut sauter par-dessus les autres pièces.
 *
 * @author crios
 * @see Piece
 */
public class Knight extends Piece {

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<BoardCell> getMoveCells() {
        Collection<BoardCell> result = new ArrayList<>();
        addRelativeElement(this,1, 2, result, null);
        addRelativeElement(this,2, 1, result, null);
        addRelativeElement(this,2, -1, result, null);
        addRelativeElement(this,1, -2, result, null);
        addRelativeElement(this,-1, -2, result, null);
        addRelativeElement(this,-2, -1, result, null);
        addRelativeElement(this,-2, 1, result, null);
        addRelativeElement(this,-1, 2, result, null);
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @return 'N' pour kNight (Cavalier)
     */
    @Override
    public char getSymbol() {
        return 'N';
    }

}
