package de.iu.fallstudie.likeherotozero.controller;

import de.iu.fallstudie.likeherotozero.model.Co2Data;
import de.iu.fallstudie.likeherotozero.repository.Co2Repository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Der Co2Controller steuert die Web-Oberfläche der Anwendung.
 * Er stellt nun auch eine Liste aller verfügbaren Länder für das Dropdown bereit.
 */
@Controller
public class Co2Controller {

  @Autowired
  private Co2Repository repository;

  @GetMapping("/")
  public String showIndex(
    @RequestParam(
      name = "country",
      required = false,
      defaultValue = "Deutschland"
    ) String country,
    Model model
  ) {
    List<Co2Data> allData = repository.findAll();

    // 1. Liste aller einzigartigen Ländernamen für das Dropdown-Menü erstellen
    List<String> distinctCountries = allData
      .stream()
      .map(Co2Data::getCountry)
      .distinct()
      .sorted()
      .collect(Collectors.toList());

    // 2. Filter-Logik anwenden
    List<Co2Data> filteredData;
    if (
      country == null ||
      country.trim().isEmpty() ||
      country.equalsIgnoreCase("Alle")
    ) {
      filteredData = allData;
      country = "Alle Länder";
    } else {
      final String filterValue = country.trim();
      filteredData = allData
        .stream()
        .filter(
          d ->
            d.getCountry() != null &&
            d.getCountry().equalsIgnoreCase(filterValue)
        )
        .collect(Collectors.toList());
    }

    // 3. Daten an das Model übergeben
    model.addAttribute("co2DataList", filteredData);
    model.addAttribute("selectedCountry", country);
    model.addAttribute("countries", distinctCountries); // Die Liste für das Dropdown

    return "index";
  }

  @GetMapping("/login")
  public String login() {
    return "login"; // Sucht nach templates/login.html
  }
}
