package com.fgodard.chess.board;

import com.fgodard.chess.beans.Game;
import java.io.IOException;

/**
 * Interface fonctionnelle pour l'export de parties d'échecs.
 * <p>
 * Permet de définir une stratégie d'export personnalisée pour les objets {@link Game}.
 * Utilisée notamment par {@link PGNHelper} lors du parsing de fichiers PGN pour
 * traiter chaque partie extraite.
 * </p>
 *
 * @author crios
 * @since 16/11/23
 * @see Game
 * @see PGNHelper#readPgnFile(java.io.File, GameExporter)
 */
@FunctionalInterface
public interface GameExporter {

    /**
     * Exporte une partie d'échecs.
     *
     * @param game la partie à exporter
     * @throws IOException si une erreur d'entrée/sortie survient lors de l'export
     */
    void exportGame(Game game) throws IOException;


}
