package com.taotao.blog.controller;

import com.taotao.blog.model.Post;
import com.taotao.blog.service.PostService;
import com.taotao.blog.service.TagService;
import com.taotao.blog.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    private PostService postService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private TagService tagService;

    @GetMapping(value = "/")
    public String index(@PageableDefault(size = 6, sort = {"updated"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {

        model.addAttribute("page", postService.listPost(pageable));
        model.addAttribute("topics", topicService.listTopTopic(6));
        model.addAttribute("tags", tagService.listTopTag(12));
        return "index";
    }

    @GetMapping(value = "/post/{id}")
    public String postPage(@PathVariable Long id, Model model) {
        return "posts";
    }

    @GetMapping(value = "/topics")
    public String topicPage() {
        return "topics";
    }


    @GetMapping(value = "/tags")
    public String tagPage() {
        return "tags";
    }

    @GetMapping(value = "/about")
    public String aboutPage() {
        return "about";
    }


}
