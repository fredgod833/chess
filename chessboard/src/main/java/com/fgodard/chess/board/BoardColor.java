package com.fgodard.chess.board;

/**
 * Énumération des couleurs des cases de l'échiquier.
 *
 * <p>Un échiquier standard alterne entre cases claires (blanches)
 * et cases foncées (noires). La case a1 est toujours une case foncée.
 *
 * @author crios
 * @see BoardCell
 */
public enum BoardColor {
    /** Case claire */
    WHITE,
    /** Case foncée */
    BLACK;
}