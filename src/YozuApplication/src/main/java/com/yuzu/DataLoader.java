package com.yuzu;

import com.yuzu.entities.Task;
import com.yuzu.repositories.TaskRepository;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    TaskRepository taskRepository;

    
    public void run(String... args) throws Exception {
    	String DATE_FORMAT_NOW = "dd-MM-yyyy";
    	Calendar cal = Calendar.getInstance();SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
    	
        taskRepository.save(new Task(sdf.format(cal.getTime()),"Sample Title 1", "Sample Message 1","hashtag1"));
        taskRepository.save(new Task(sdf.format(cal.getTime()),"Sample Title 2", "Sample Message 2","hashtag2"));
        taskRepository.save(new Task(sdf.format(cal.getTime()),"Sample Title 2", "Sample Message 3","hashtag3"));
        taskRepository.save(new Task(sdf.format(cal.getTime()),"Sample Title 3", "Sample Message 4","hashtag4"));
    }
}