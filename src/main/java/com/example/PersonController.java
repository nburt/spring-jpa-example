package com.example;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonRepository repository;

    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @PostMapping("")
    public Person create(@Valid @RequestBody Person person) {
        this.repository.save(person);
        return person;
    }

    @GetMapping("")
    public Iterable<Person> list() {
        return this.repository.findAll();
    }

}
