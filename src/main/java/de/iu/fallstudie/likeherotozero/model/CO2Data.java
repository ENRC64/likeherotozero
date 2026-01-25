package de.iu.fallstudie.likeherotozero.model;

import jakarta.persistence.*; // JPA importieren
import lombok.Data; // Reduzierung von Boilerplate Code

@Entity // Markiert die Klasse als Entität (Tabelle) einer Datenbank
@Table(name = "co2_data") // Konkrete Benennung der Tabelle
@Data //Annotation von Lombok, die Standard Getter,Setter Methoden implementiert
public class CO2Data {

  @Id //Primärschlüssel
  @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
  private Long id;

  private String country;
  private Integer year;
  private Double co2Value;
}
