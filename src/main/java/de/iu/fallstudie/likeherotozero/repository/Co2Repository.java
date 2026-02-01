package de.iu.fallstudie.likeherotozero.repository;

import de.iu.fallstudie.likeherotozero.model.Co2Data; // Referenz auf das Datenmodell
import org.springframework.data.jpa.repository.JpaRepository; // Importieren vom JPA
import org.springframework.stereotype.Repository;

/**
 * Repository-Schnittstelle für den Zugriff auf CO2-Daten.
 */
@Repository // Markiert als Bean
public interface Co2Repository extends JpaRepository<Co2Data, Long> {
  // Bietet automatische Funktionen an, daher kein Code!
}
