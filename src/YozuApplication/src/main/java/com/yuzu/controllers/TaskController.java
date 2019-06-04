package com.yuzu.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuzu.entities.Task;
import com.yuzu.entities.User;
import com.yuzu.repositories.TaskRepository;
import com.yuzu.repositories.UserRepository;

@Controller
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @GetMapping("/")
    public String showPage(Model model, @AuthenticationPrincipal UserDetails currentUser, @RequestParam(defaultValue = "0") int page) {
        model.addAttribute("data",
        		taskRepository.findAll(PageRequest.of(page, 4)));
        model.addAttribute("currentPage", page);
        
        if(currentUser !=null){
        //Can change to first name if desired rather than username
        User user = (User) userR.findByUsername(currentUser.getUsername());
        model.addAttribute("currentUser", user);}
        
        return "index";
    }

    
    @Autowired
    private UserRepository userR;
    
 
    /**Non-optimzed approach of injectecting user's name and date  rather than in the
    //index.html file, as cant check for duplicates at this point.*/
    @PostMapping("/save")
    public String save(Task task, @AuthenticationPrincipal UserDetails currentUser) {
    
    List<Task> allUpdates = taskRepository.findAll();
    User user = (User) userR.findByUsername(currentUser.getUsername());
    if(task.getUser() ==null){
    	task.setUser(user.getUsername());
    }
    
    if(task.getDate() ==null){
    	String DATE_FORMAT_NOW = "dd-MM-yyyy";
    	Calendar cal = Calendar.getInstance();SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
    	task.setDate(sdf.format(cal.getTime()));
    }
    for(Task aTask : allUpdates){
    	if(
    	aTask.getTitle().equals(task.getTitle()) && aTask.getUser().equals(task.getUser()) && aTask.getDate().equals(task.getDate())){
    		 return "redirect:/?duplicate";
    	}
    }
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
