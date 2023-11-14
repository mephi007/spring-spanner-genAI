package com.example.demo;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// import org.springframework.stereotype.Controller;

@RestController
public class YogaController {
    
    private YogaRepository yogaRepository;

    public YogaController(YogaRepository repo){
        this.yogaRepository = repo;
    }

    @GetMapping("/")
    public List<Yoga> getAllBlocks(){
        return (List<Yoga>) yogaRepository.findAll();
        // return null;
    }
}
