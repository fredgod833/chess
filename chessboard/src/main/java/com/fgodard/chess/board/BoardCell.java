package com.fgodard.chess.board;

/**
 * Énumération des 64 cases de l'échiquier.
 *
 * <p>Chaque case est définie par :
 * <ul>
 *   <li>Son nom en notation algébrique (A1 à H8)</li>
 *   <li>Ses coordonnées (colonne 0-7, ligne 0-7)</li>
 *   <li>Son indice linéaire (0-63)</li>
 *   <li>Sa couleur (cases claires ou foncées)</li>
 * </ul>
 *
 * <p>Les cases sont ordonnées de A1 à H8, ligne par ligne :
 * A1, B1, C1, ..., H1, A2, B2, ..., H8.
 *
 * @author crios
 * @see BoardColor
 */
public enum BoardCell {

    /** Case a1 (coin inférieur gauche, case noire) */
    A1(0, 0, "a1", BoardColor.BLACK),
    /** Case b1 */
    B1(1, 0, "b1", BoardColor.WHITE),
    /** Case c1 */
    C1(2, 0, "c1", BoardColor.BLACK),
    /** Case d1 */
    D1(3, 0, "d1", BoardColor.WHITE),
    /** Case e1 (case initiale du roi blanc) */
    E1(4, 0, "e1", BoardColor.BLACK),
    /** Case f1 */
    F1(5, 0, "f1", BoardColor.WHITE),
    /** Case g1 */
    G1(6, 0, "g1", BoardColor.BLACK),
    /** Case h1 (coin inférieur droit, case blanche) */
    H1(7, 0, "h1", BoardColor.WHITE),
    A2(0, 1, "a2", BoardColor.WHITE), B2(1, 1, "b2", BoardColor.BLACK), C2(2, 1, "c2", BoardColor.WHITE), D2(3, 1, "d2", BoardColor.BLACK), E2(4, 1, "e2", BoardColor.WHITE), F2(5, 1, "f2", BoardColor.BLACK), G2(6, 1, "g2", BoardColor.WHITE), H2(7, 1, "h2", BoardColor.BLACK),
    A3(0, 2, "a3", BoardColor.BLACK), B3(1, 2, "b3", BoardColor.WHITE), C3(2, 2, "c3", BoardColor.BLACK), D3(3, 2, "d3", BoardColor.WHITE), E3(4, 2, "e3", BoardColor.BLACK), F3(5, 2, "f3", BoardColor.WHITE), G3(6, 2, "g3", BoardColor.BLACK), H3(7, 2, "h3", BoardColor.WHITE),
    A4(0, 3, "a4", BoardColor.WHITE), B4(1, 3, "b4", BoardColor.BLACK), C4(2, 3, "c4", BoardColor.WHITE), D4(3, 3, "d4", BoardColor.BLACK), E4(4, 3, "e4", BoardColor.WHITE), F4(5, 3, "f4", BoardColor.BLACK), G4(6, 3, "g4", BoardColor.WHITE), H4(7, 3, "h4", BoardColor.BLACK),
    A5(0, 4, "a5", BoardColor.BLACK), B5(1, 4, "b5", BoardColor.WHITE), C5(2, 4, "c5", BoardColor.BLACK), D5(3, 4, "d5", BoardColor.WHITE), E5(4, 4, "e5", BoardColor.BLACK), F5(5, 4, "f5", BoardColor.WHITE), G5(6, 4, "g5", BoardColor.BLACK), H5(7, 4, "h5", BoardColor.WHITE),
    A6(0, 5, "a6", BoardColor.WHITE), B6(1, 5, "b6", BoardColor.BLACK), C6(2, 5, "c6", BoardColor.WHITE), D6(3, 5, "d6", BoardColor.BLACK), E6(4, 5, "e6", BoardColor.WHITE), F6(5, 5, "f6", BoardColor.BLACK), G6(6, 5, "g6", BoardColor.WHITE), H6(7, 5, "h6", BoardColor.BLACK),
    A7(0, 6, "a7", BoardColor.BLACK), B7(1, 6, "b7", BoardColor.WHITE), C7(2, 6, "c7", BoardColor.BLACK), D7(3, 6, "d7", BoardColor.WHITE), E7(4, 6, "e7", BoardColor.BLACK), F7(5, 6, "f7", BoardColor.WHITE), G7(6, 6, "g7", BoardColor.BLACK), H7(7, 6, "h7", BoardColor.WHITE),
    /** Case a8 (coin supérieur gauche, case blanche) */
    A8(0, 7, "a8", BoardColor.WHITE), B8(1, 7, "b8", BoardColor.BLACK), C8(2, 7, "c8", BoardColor.WHITE), D8(3, 7, "d8", BoardColor.BLACK),
    /** Case e8 (case initiale du roi noir) */
    E8(4, 7, "e8", BoardColor.WHITE), F8(5, 7, "f8", BoardColor.BLACK), G8(6, 7, "g8", BoardColor.WHITE),
    /** Case h8 (coin supérieur droit, case noire) */
    H8(7, 7, "h8", BoardColor.BLACK);

    /** Indice linéaire de la case (0-63) */
    private final int idx;

    /** Indice de ligne (0-7, où 0 correspond à la rangée 1) */
    private final int lineIdx;

    /** Indice de colonne (0-7, où 0 correspond à la colonne a) */
    private final int colIdx;

    /** Couleur de la case (claire ou foncée) */
    private final BoardColor color;

    /** Notation algébrique de la case (ex: "e4") */
    private final String algebricPos;

    BoardCell(int col, int line, final String algebricPos, BoardColor color) {
        this.colIdx = col;
        this.lineIdx = line;
        this.idx = col + line * 8;
        this.algebricPos = algebricPos;
        this.color = color;
    }

    /**
     * Retourne l'indice de ligne (0-7).
     *
     * @return l'indice de ligne (0 = rangée 1, 7 = rangée 8)
     */
    public int getLineIdx() {
        return lineIdx;
    }

    /**
     * Retourne l'indice de colonne (0-7).
     *
     * @return l'indice de colonne (0 = colonne a, 7 = colonne h)
     */
    public int getColIdx() {
        return colIdx;
    }

    /**
     * Retourne l'indice linéaire de la case (0-63).
     *
     * @return l'indice linéaire
     */
    public int getIdx() {
        return idx;
    }

    /**
     * Retourne la couleur de la case.
     *
     * @return la couleur (claire ou foncée)
     */
    public BoardColor getColor() {
        return color;
    }

    /**
     * Retourne la notation algébrique de la case.
     *
     * @return la notation algébrique (ex: "e4")
     */
    public String getAlgebricPos() {
        return algebricPos;
    }

    /**
     * Retourne la lettre de la colonne.
     *
     * @return la lettre de colonne ('a' à 'h')
     */
    public char getCol() {
        return (char) (colIdx + 'a');
    }

    /**
     * Retourne le numéro de la ligne (1-8).
     *
     * @return le numéro de ligne
     */
    public int getLine() {
        return lineIdx + 1;
    }
}
