package com.fgodard.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilitaire pour filtrer des blocs de texte délimités par des caractères d'ouverture et de fermeture.
 * <p>
 * Cette classe permet de traiter un flux de texte ligne par ligne en supprimant automatiquement
 * les blocs délimités (par exemple les commentaires entre accolades ou les variantes entre parenthèses).
 * Elle gère correctement les blocs multi-lignes et les blocs imbriqués de même niveau.
 * </p>
 *
 * <p><b>Exemple d'utilisation :</b></p>
 * <pre>{@code
 * TextStripe stripe = new TextStripe("{", "}");
 * stripe.appendLine("1. e4 {un commentaire} e5");
 * String result = stripe.retrieveLine(); // retourne "1. e4  e5"
 * }</pre>
 *
 * @author crios
 * @since 06/05/23
 */
public class TextStripe {

    private String line = null;
    private Pattern blocPattern;

    private char openChar;
    private char closeChar;

    /**
     * Construit un nouveau filtre de texte avec les délimiteurs spécifiés.
     *
     * @param openChar  le caractère ou pattern d'ouverture de bloc (ex: "{" ou "\\(")
     * @param closeChar le caractère ou pattern de fermeture de bloc (ex: "}" ou "\\)")
     */
    public TextStripe(String openChar, String closeChar) {
        String sPattern = String.format("[%1$s]([^%2$s%1$s]*)[%2$s]", openChar, closeChar);
        blocPattern = Pattern.compile(sPattern);
        this.openChar = openChar.charAt(openChar.length()-1);
        this.closeChar = closeChar.charAt(closeChar.length()-1);;
    }

    /**
     * Récupère la prochaine ligne filtrée disponible.
     * <p>
     * Retourne une chaîne vide si :
     * <ul>
     *     <li>Aucune ligne n'a été ajoutée</li>
     *     <li>Un bloc délimité est encore ouvert (en attente de fermeture)</li>
     * </ul>
     * </p>
     *
     * @return la ligne filtrée sans les blocs délimités, ou une chaîne vide si indisponible
     */
    public String retrieveLine() {
        if (line==null || line.indexOf(openChar)>=0 || line.indexOf(closeChar) >= 0) {
            return "";
        }

        int eolPos = line.indexOf('\n');
        if ( eolPos > 0 ) {
            final String result = line.substring(0,eolPos);
            eolPos++;
            if (eolPos<line.length()) {
                line = line.substring(eolPos).trim();
            } else {
                line = null;
            }
            return result;

        } else {
            String result = line;
            line = null;
            return result;
        }
    }

    private void cleanBlocs() {
        Matcher m = blocPattern.matcher(line);
        while (m.find()) {
            String start = line.substring(0, m.start());
            String end = line.substring(m.end());
            line = start.concat(" ").concat(end).trim();
            m = blocPattern.matcher(line);
        }
    }

    /**
     * Ajoute une ligne de texte au buffer interne.
     * <p>
     * Les blocs délimités complets sont automatiquement supprimés.
     * Les blocs incomplets (ouverts mais non fermés) sont conservés en attente.
     * </p>
     *
     * @param line la ligne à ajouter (ignorée si null ou vide)
     */
    public void appendLine(final String line) {
        if (line == null || line.isEmpty()) {
            return;
        }
        if (this.line == null || this.line.isEmpty()) {
            this.line = line;
        } else {
            this.line = this.line.concat("\n").concat(line).trim();
        }
        cleanBlocs();
    }

    /**
     * Réinitialise le buffer interne.
     */
    public void clear() {
        this.line = null;
    }

    /**
     * Indique si le buffer est vide.
     *
     * @return {@code true} si aucune donnée n'est en attente de traitement
     */
    public boolean isEmpty() {
        return (line==null || line.trim().isEmpty());
    }

}
