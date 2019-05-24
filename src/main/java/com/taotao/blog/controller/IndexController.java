package com.taotao.blog.controller;

import com.taotao.blog.model.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Taotao Ma
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/", method = GET)
    public String indexPage(Model model) {
        return "index";
    }

    @RequestMapping(value = "/post", method = GET)
    public String postPage() {
        return "post";
    }


    @RequestMapping(value = "/topic", method = GET)
    public String topicPage() {
        return "topic";
    }


    @RequestMapping(value = "/tag", method = GET)
    public String tagPage() {
        return "tag";
    }

    @RequestMapping(value = "/about", method = GET)
    public String aboutPage() {
        return "about";
    }


}
