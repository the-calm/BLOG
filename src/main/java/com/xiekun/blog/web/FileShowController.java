package com.xiekun.blog.web;

import com.xiekun.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FileShowController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/file")
    public String file(Model model) {
        model.addAttribute("fileMap",blogService.fileBlog());
        model.addAttribute("countBlog",blogService.countBlog());
        return "file";
    }
}
