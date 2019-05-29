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
import org.springframework.web.bind.annotation.*;

import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.List;

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
    public String post(@PathVariable Long id, Model model) {
        Post post = postService.getAndConvertPost(id);
        model.addAttribute("post", post);
        showLink(post, model);
        return "post";
    }

    private void showLink(Post post, Model model) {
        List<Post> postList = new ArrayList<>();
        int count = 0;
        for (Long postId : post.getRelatedPostIds()) {
            if (count < 3) {
                postList.add(postService.getPost(postId));
                count++;
            } else {
                break;
            }
        }

        model.addAttribute("postList", postList);
        model.addAttribute("showLink", !postList.isEmpty());
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 6, sort = {"updated"}, direction = Sort.Direction.DESC)Pageable pageable,
                         @RequestParam String query,
                         Model model) {
        model.addAttribute("page", postService.listPost(pageable, "%"+query+"%"));
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping(value = "/topics")
    public String topics() {
        return "topics";
    }


    @GetMapping(value = "/tags")
    public String tags() {
        return "tags";
    }

    @GetMapping(value = "/about")
    public String about() {
        return "about";
    }

}
