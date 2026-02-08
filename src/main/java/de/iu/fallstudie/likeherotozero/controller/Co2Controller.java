package de.iu.fallstudie.likeherotozero.controller;

import de.iu.fallstudie.likeherotozero.model.Co2Data;
import de.iu.fallstudie.likeherotozero.repository.Co2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class Co2Controller {

    @Autowired
    private Co2Repository repository;

    @GetMapping("/")
    public String showIndex(@RequestParam(name = "country", required = false) String country, 
                            Model model) {
        
        List<Co2Data> allData = repository.findAll();
        if (allData == null) {
            allData = new ArrayList<>();
        }

        List<String> distinctCountries = allData.stream()
                .filter(d -> d.getCountry() != null)
                .map(Co2Data::getCountry)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        List<Co2Data> filteredData;
        String displayCountry;

        if (country == null || country.trim().isEmpty() || country.equalsIgnoreCase("Alle")) {
            filteredData = allData;
            displayCountry = "Alle Länder";
        } else {
            final String filterValue = country.trim();
            displayCountry = filterValue;
            filteredData = allData.stream()
                .filter(d -> d.getCountry() != null && d.getCountry().equalsIgnoreCase(filterValue))
                .collect(Collectors.toList());
        }

        model.addAttribute("co2DataList", filteredData);
        model.addAttribute("selectedCountry", displayCountry);
        model.addAttribute("countries", distinctCountries);
        
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Zeigt das Formular zum Hinzufügen an
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("newData", new Co2Data());
        return "add-data";
    }

    // Verarbeitet das Absenden des Formulars
    @PostMapping("/add")
    public String addData(@ModelAttribute Co2Data co2Data) {
        repository.save(co2Data);
        return "redirect:/?country=" + co2Data.getCountry();
    }
}