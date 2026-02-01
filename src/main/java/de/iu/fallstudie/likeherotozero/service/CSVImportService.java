package de.iu.fallstudie.likeherotozero.service;

import de.iu.fallstudie.likeherotozero.model.Co2Data; // Zugriff auf das Datenmodell
import de.iu.fallstudie.likeherotozero.repository.Co2Repository; // Zugriff auf das Datenbank-Repository
import jakarta.annotation.PostConstruct; // Ermöglicht Code-Ausführung direkt nach dem App-Start
import java.io.BufferedReader; // Effizientes Lesen von Text-Daten
import java.io.InputStreamReader; // Umwandlung von Byte-Streams in Text
import java.nio.charset.StandardCharsets; // Sicherstellung der korrekten Zeichenkodierung (UTF-8)
import org.springframework.beans.factory.annotation.Autowired; // Automatisches Verknüpfen von Komponenten (Dependency Injection)
import org.springframework.core.io.ClassPathResource; // Zum Finden von Dateien im resources-Ordner
import org.springframework.stereotype.Service; // Markiert diese Klasse als Geschäftslogik-Komponente

/**
 * Service für den automatischen Datenimport.
 */
@Service
public class CSVImportService {

  @Autowired
  private Co2Repository repository;

  /**
   * Startet automatisch nach dem Application-Start.
   */
  @PostConstruct
  public void init() {
    if (repository.count() == 0) {
      importCSV();
    }
  }

  private void importCSV() {
    try (
      BufferedReader br = new BufferedReader(
        new InputStreamReader(
          new ClassPathResource("co2_data.csv").getInputStream(),
          StandardCharsets.UTF_8
        )
      )
    ) {
      String line;
      br.readLine(); // Header überspringen

      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");

        // BEREINIGUNG: Entfernt Anführungszeichen, falls vorhanden (löst Fehler: For input string: ""1990"")
        for (int i = 0; i < values.length; i++) {
          values[i] = values[i].replace("\"", "").trim();
        }

        try {
          // Validierung und Parsing des CO2-Werts
          // Falls die Spalte leer ist oder keinen Double enthält, wird eine Exception geworfen
          double co2Val = Double.parseDouble(values[6]);

          Co2Data data = new Co2Data();
          // Mapping der CSV-Spalten auf das Java-Modell
          data.setCountry(values[2]);
          data.setYear(Integer.parseInt(values[4]));
          data.setCo2Value(co2Val);

          repository.save(data); // Speichern in der MySQL Datenbank
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
          // Loggen des Fehlers und Überspringen der Zeile, wenn der Wert ungültig ist
          System.out.println(
            "Zeile übersprungen: Ungültiger CO2-Wert oder fehlende Daten in Zeile: " +
            line
          );
        }
      }
      System.out.println("Import erfolgreich abgeschlossen.");
    } catch (Exception e) {
      System.err.println("Fehler beim Import: " + e.getMessage());
    }
  }
}
