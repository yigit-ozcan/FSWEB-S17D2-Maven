package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    private final Taxable taxable;
    public Map<Integer, Developer> developers;

    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

    @PostConstruct
    public void init() {
        developers = new HashMap<>();
    }

    @GetMapping
    public List<Developer> getAllDevelopers() {
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable int id) {
        return developers.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Developer addDeveloper(@RequestBody Developer developer) {

        double salary = developer.getSalary();
        Developer newDeveloper;

        if(developer.getExperience() == Experience.JUNIOR) {
            salary = salary - (salary * taxable.getSimpleTaxRate() / 100);
            newDeveloper = new JuniorDeveloper(
                    developer.getId(),
                    developer.getName(),
                    salary
            );
        } else if (developer.getExperience() == Experience.MID) {
            salary = salary - (salary * taxable.getMiddleTaxRate() / 100);
            newDeveloper = new MidDeveloper(
                    developer.getId(),
                    developer.getName(),
                    salary
            );
        } else {
            salary = salary - (salary * taxable.getUpperTaxRate() / 100);
            newDeveloper = new SeniorDeveloper(
                    developer.getId(),
                    developer.getName(),
                    salary
            );
        }

        developers.put(newDeveloper.getId(), newDeveloper);
        return newDeveloper;
    }

    @PutMapping("/{id}")
    public Developer updateDeveloper(@PathVariable int id, @RequestBody Developer developer) {
        developers.put(id, developer);
        return developer;
    }

    @DeleteMapping("/{id}")
    public Developer deleteDeveloper(@PathVariable int id) {
        return developers.remove(id);
    }
}

