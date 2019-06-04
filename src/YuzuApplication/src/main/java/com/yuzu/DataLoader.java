package com.yuzu;

import com.yuzu.entities.Task;
import com.yuzu.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    TaskRepository taskRepository;

    
    public void run(String... args) throws Exception {
        taskRepository.save(new Task("Earth", "World","hash1"));
        taskRepository.save(new Task("China", "Beging","hash2"));
        taskRepository.save(new Task("Germany", "Berlin","hash3"));
        taskRepository.save(new Task("USA", "Washington DC","hash 4"));
        taskRepository.save(new Task("Russia", "Moscow","hash5"));
        taskRepository.save(new Task("Namibia", "Windhoek","hash 6"));
        taskRepository.save(new Task("India", "New Delhi","hash7"));
        taskRepository.save(new Task("North Korea", "Pyongyang","hash8"));
        taskRepository.save(new Task("Kenya", "Nairobi","hash9"));
        taskRepository.save(new Task("Canada", "Ottawa","hash10"));
        taskRepository.save(new Task("Jamaica", "Kingston","hash11"));
        taskRepository.save(new Task("Brazil", "Brasilia","hash12"));
        taskRepository.save(new Task("Egypt", "Cairo","hash13"));
        taskRepository.save(new Task("Nigeria", "Lagos","hash14"));
        taskRepository.save(new Task("Jordon", "Amman","hash15"));
        taskRepository.save(new Task("Curacao", "Willemstad","hash16"));
        taskRepository.save(new Task("Sao Tome Principe", "Sao Tome","hash17"));
    }
}