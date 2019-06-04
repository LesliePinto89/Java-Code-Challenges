package com.yuzu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuzu.entities.Task;
import com.yuzu.repositories.TaskRepository;

@Controller
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @GetMapping("/")
    public String showPage(Model model, @RequestParam(defaultValue = "0") int page) {
        model.addAttribute("data",
        		taskRepository.findAll(PageRequest.of(page, 4)));
        model.addAttribute("currentPage", page);
        return "index";
    }

    @PostMapping("/save")
    public String save(Task task) {
    	taskRepository.save(task);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deleteTask(long id) {
    	taskRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/findOne")
    @ResponseBody
    public Task findOne(long id) {
        return taskRepository.findById(id).get();
    }
}
