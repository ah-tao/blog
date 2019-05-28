package com.taotao.blog.controller.admin;

import com.taotao.blog.model.Tag;
import com.taotao.blog.service.TagService;
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
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 6, sort = {"id"}, direction = Sort.Direction.DESC)Pageable pageable, Model model) {
        model.addAttribute("page", tagService.listTag(pageable));
        return "admin/tag_list";
    }

    @GetMapping("/tags/create")
    public String create(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tag_edit";
    }

    @GetMapping("/tags/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tag_edit";
    }

    @PostMapping("/tags")
    public String add(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {
        Tag t = tagService.getTagByName(tag.getName());
        if (t != null) {
            result.rejectValue("name", "nameError", "Name \"" + t.getName() + "\" already exists.");
        }
        if (result.hasErrors()) {
            return "admin/tag_edit";
        }
        t = tagService.saveTag(tag);
        if (t == null) {
            attributes.addFlashAttribute("message", "Fail to create tag");
        } else {
            attributes.addFlashAttribute("message", "Create tag success");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}")
    public String update(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {
        Tag t = tagService.getTagByName(tag.getName());
        if (t != null) {
            result.rejectValue("name", "nameError", "Name \"" + t.getName() + "\" already exists.");
        }
        if (result.hasErrors()) {
            return "admin/tag_edit";
        }
        t = tagService.updateTag(id, tag);
        if (t == null) {
            attributes.addFlashAttribute("message", "Fail to update tag");
        } else {
            attributes.addFlashAttribute("message", "Update tag success");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "Delete tag success");
        return "redirect:/admin/tags";
    }
}
