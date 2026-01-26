package com.example.menu.controller;

import com.example.menu.dto.CommentForm;
import com.example.menu.dto.MenuForm;
import com.example.menu.entity.Comment;
import com.example.menu.entity.Menu;
import com.example.menu.repository.CommentRepository;
import com.example.menu.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Slf4j
@Controller
public class MenuController {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/menus")
    public String list (Model model)  {
        Iterable<Menu> menuEntityList = menuRepository.findAll();
        model.addAttribute("menuList", menuEntityList);
        return "menus/list";
    }

    @GetMapping("/menus/new")
    public String newMenuForm() {
        return "menus/new";
    }

    @PostMapping("/menus/create")
    public String create(MenuForm form, Model model) {
        log.info(form.toString());

        Menu menu = form.toEntity();
        log.info(menu.toString());

        Menu saved = menuRepository.save(menu);
        log.info(saved.toString());

        model.addAttribute("title", form.getTitle());
        model.addAttribute("content", form.getContent());

        return "menus/result";
    }

    @GetMapping("/menus/{id}")
    public String show (@PathVariable("id") Long id, Model model) {
        log.info("id = "+id);
        Menu menuEntity = menuRepository.findById(id).orElse(null);
        List<Comment> commentEntityList = commentRepository.findByMenuId(id);

        model.addAttribute("menus", menuEntity);
        model.addAttribute("commentList", commentEntityList);
        model.addAttribute("newComment", new CommentForm());

        return "menus/show";
    }

    @PostMapping("/menus/{menuId}/comments")
    public String createComment(@PathVariable("menuId") Long menuId, CommentForm commentForm, RedirectAttributes redirectAttributes) {
        Menu menuEntity = menuRepository.findById(menuId).orElse(null);
        Comment newComment = Comment.createCommentFromForm(commentForm, menuEntity);

        try {
            Comment savedComment = commentRepository.save(newComment);
            redirectAttributes.addFlashAttribute("successMessage", "댓글이 성공적으로 등록되었습니다.");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "댓글 등록 중 오류가 발생했습니다. 다시 시도해주세요.");
        }

        return "redirect:/menus/" + menuId;

    }


    @GetMapping("/menus/{id}/edit")
    public String edit (@PathVariable("id") Long id, Model model) {
        Menu menuEntity = menuRepository.findById(id).orElse(null);
        model.addAttribute("id", menuEntity.getId());
        model.addAttribute("title", menuEntity.getTitle());
        model.addAttribute("content", menuEntity.getContent());

        return "menus/edit";
    }

    @PostMapping("/menus/update")
    public String update(MenuForm form) {
        Menu menuEntity = form.toEntity();
        log.info(menuEntity.toString());
        Menu target = menuRepository.findById(menuEntity.getId()).orElse(null);

        if (target != null) {
            target.setTitle(form.getTitle());
            target.setContent(form.getContent());
            menuRepository.save(target);
        }

        return "redirect:/menus/"+menuEntity.getId();
    }

    @Transactional
    @GetMapping("/menus/{id}/delete")
    public String delete (@PathVariable("id") Long id, RedirectAttributes rrtr) {
        log.info("삭제 요청");
        Menu target = menuRepository.findById(id).orElse(null);
        log.info(target.toString());

        if(target != null) {
            List<Comment> comments = commentRepository.findByMenuId(id);

            if(!comments.isEmpty()) {
                commentRepository.deleteAll(comments);
                rrtr.addFlashAttribute("msg", "삭제되었습니다.");
            }

        }
        menuRepository.delete(target);
        return "redirect:/menus";
    }
}
