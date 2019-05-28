package com.taotao.blog.controller.admin;

import com.taotao.blog.model.Post;
import com.taotao.blog.model.User;
import com.taotao.blog.service.PostService;
import com.taotao.blog.service.TagService;
import com.taotao.blog.service.TopicService;
import com.taotao.blog.util.PostSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author Taotao Ma
 */
@Controller
@RequestMapping("/admin")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private TagService tagService;

    @GetMapping("/posts")
    public String posts(@PageableDefault(size = 6, sort = {"updated"}, direction = Sort.Direction.DESC)Pageable pageable,
                        PostSearchCriteria criteria,
                        Model model) {

        model.addAttribute("topics", topicService.listTopic());
        model.addAttribute("page", postService.listPost(pageable, criteria));
        return "admin/post_list";

    }

    @PostMapping("/posts/search")
    public String search(@PageableDefault(size = 6, sort = {"updated"}, direction = Sort.Direction.DESC) Pageable pageable,
                         PostSearchCriteria criteria,
                         Model model) {

        model.addAttribute("page", postService.listPost(pageable, criteria));
        return "admin/post_list :: post_search_list";
    }

    @GetMapping("/posts/create")
    public String create(Model model) {
        setTopicAndTag(model);
        model.addAttribute("post", new Post());
        return "admin/post_edit";
    }

    @GetMapping("/posts/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        setTopicAndTag(model);
        Post post = postService.getPost(id);
        post.init();
        model.addAttribute("post", post);
        return "admin/post_edit";
    }

    @PostMapping("/posts")
    public String post(Post post, RedirectAttributes attributes, HttpSession session) {
        post.setUser((User) session.getAttribute("user"));
        post.setTopic(topicService.getTopic(post.getTopic().getId()));
        post.setTags(tagService.listTag(post.getTagIds()));

        Post p;
        if (post.getId() == null) {
            p = postService.savePost(post);
        } else {
            p = postService.updatePost(post.getId(), post);
        }

        if (p == null) {
            attributes.addAttribute("message", "Process Failed");
        } else {
            attributes.addAttribute("message", "Process Success");
        }

        return "redirect:/admin/posts";
    }

    @GetMapping("/posts/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        postService.deletePost(id);
        attributes.addAttribute("message", "Delete Success");
        return "redirect:/admin/posts";
    }

    private void setTopicAndTag(Model model) {
        model.addAttribute("topics", topicService.listTopic());
        model.addAttribute("tags", tagService.listTag());
    }
}
