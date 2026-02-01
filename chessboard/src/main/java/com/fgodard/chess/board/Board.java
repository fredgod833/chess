package com.fgodard.chess.board;

import com.fgodard.chess.exception.InvalidCellException;

import java.util.Optional;

/**
 * Classe utilitaire pour la gestion des cases de l'échiquier.
 *
 * <p>Cette classe fournit des méthodes statiques pour :
 * <ul>
 *   <li>Convertir des coordonnées (colonne, ligne) en case {@link BoardCell}</li>
 *   <li>Convertir une notation algébrique (ex: "e4") en case</li>
 *   <li>Calculer des cases relatives à partir d'une case de base</li>
 * </ul>
 *
 * <p>La classe est non instanciable (constructeur privé) car elle ne contient
 * que des méthodes statiques.
 *
 * @author crios
 * @see BoardCell
 */
public class Board {

    /**
     * Private Constructor
     */
    private Board() {

    }

    private static String getCellName(int colIdx, int lineIdx) {
        char[] result = new char[2];
        result[0] = (char)('A' + colIdx);
        result[1] = (char)('1' + lineIdx);
        return String.valueOf(result);
    }

    /**
     * Retourne la case correspondant aux coordonnées spécifiées.
     *
     * @param colIdx l'indice de colonne (0-7, où 0=a et 7=h)
     * @param lineIdx l'indice de ligne (0-7, où 0=1 et 7=8)
     * @return un Optional contenant la case, ou vide si les coordonnées sont hors échiquier
     */
    public static Optional<BoardCell> getCell(int colIdx, int lineIdx) {
        if (colIdx < 0 || colIdx > 7) {
            return Optional.empty();
        }
        if (lineIdx < 0 || lineIdx > 7) {
            return Optional.empty();
        }

        return Optional.of(BoardCell.valueOf(getCellName(colIdx,lineIdx)));
    }

    /**
     * Convertit une notation algébrique en case de l'échiquier.
     *
     * @param alpha la notation algébrique (ex: "e4", "a1", "h8")
     * @return la case correspondante
     * @throws InvalidCellException si la notation est invalide
     */
    public static BoardCell getCell(final String alpha) throws InvalidCellException {
        int line;
        try {
            line = Integer.parseInt(alpha.substring(1, 2)) - 1;
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new InvalidCellException(e, "Numéro de ligne invalide (%s).", alpha);
        }

        int col;
        try {
            col = alpha.charAt(0) - 97;
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidCellException(e, "Lettre de colonne invalide (%s).", alpha);
        }

        return BoardCell.values()[col + line * 8];
    }

    /**
     * Retourne une case relative à la case de base avec un incrément.
     *
     * <p>Cette méthode permet de naviguer sur l'échiquier à partir d'une case donnée.
     *
     * @param cell la case de base
     * @param colIncrement l'incrément de colonne (négatif pour aller vers a, positif vers h)
     * @param lineIncrement l'incrément de ligne (négatif pour aller vers 1, positif vers 8)
     * @return un Optional contenant la case relative, ou vide si hors échiquier
     */
    public static Optional<BoardCell> getCell(BoardCell cell, int colIncrement, int lineIncrement) {
        int col = cell.getColIdx() + colIncrement;
        int line = cell.getLineIdx() + lineIncrement;
        return getCell(col, line);
    }

}
