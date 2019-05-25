package com.taotao.blog.controller.admin;

import com.taotao.blog.model.Topic;
import com.taotao.blog.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @author Taotao Ma
 */
@Controller
@RequestMapping("/admin")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping("/topics")
    public String topics(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)Pageable pageable, Model model) {
        model.addAttribute("page", topicService.listTopic(pageable));
        return "admin/topic_list";
    }

    @GetMapping("/topics/create")
    public String create(Model model) {
        model.addAttribute("topic", new Topic());
        return "admin/topic_edit";
    }

    @GetMapping("/topics/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("topic", topicService.getTopic(id));
        return "admin/topic_edit";
    }

    @PostMapping("/topics")
    public String add(@Valid Topic topic, BindingResult result, RedirectAttributes attributes) {
        Topic t = topicService.getTopicByName(topic.getName());
        if (t != null) {
            result.rejectValue("name", "nameError", "Name \"" + t.getName() + "\" already exists.");
        }
        if (result.hasErrors()) {
            return "admin/topic_edit";
        }
        t = topicService.saveTopic(topic);
        if (t == null) {
            attributes.addFlashAttribute("message", "Fail to create topic");
        } else {
            attributes.addFlashAttribute("message", "Create topic success");
        }
        return "redirect:/admin/topics";
    }

    @PostMapping("/topics/{id}")
    public String update(@Valid Topic topic, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {
        Topic t = topicService.getTopicByName(topic.getName());
        if (t != null) {
            result.rejectValue("name", "nameError", "Name \"" + t.getName() + "\" already exists.");
        }
        if (result.hasErrors()) {
            return "admin/topic_edit";
        }
        t = topicService.updateTopic(id, topic);
        if (t == null) {
            attributes.addFlashAttribute("message", "Fail to update topic");
        } else {
            attributes.addFlashAttribute("message", "Update topic success");
        }
        return "redirect:/admin/topics";
    }

    @GetMapping("/topics/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        topicService.deleteTopic(id);
        attributes.addFlashAttribute("message", "Delete topic success");
        return "redirect:/admin/topics";
    }
}
