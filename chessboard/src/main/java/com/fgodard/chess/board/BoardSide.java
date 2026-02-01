package com.fgodard.chess.board;

/**
 * Énumération des côtés de l'échiquier pour le roque.
 *
 * <p>Le roque peut se faire de deux côtés :
 * <ul>
 *   <li><b>Côté roi</b> (petit roque) : le roi se déplace de 2 cases vers la tour h</li>
 *   <li><b>Côté dame</b> (grand roque) : le roi se déplace de 2 cases vers la tour a</li>
 * </ul>
 *
 * @author crios
 */
public enum BoardSide {
    /** Côté roi (petit roque, O-O) */
    KING,
    /** Côté dame (grand roque, O-O-O) */
    QUEEN;
}
