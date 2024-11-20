package com.fpoly.java5.controllers;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fpoly.java5.entities.Comment;
import com.fpoly.java5.entities.User;
import com.fpoly.java5.jpa.CommentJPA;
import com.fpoly.java5.jpa.ProductJPA;
import com.fpoly.java5.utils.UserUtil;


@Controller
public class CommentController {
    @Autowired
    private CommentJPA commentJPA;
    @Autowired 
    private ProductJPA productJPA;
    @Autowired
    private UserUtil userUtil;

    @PostMapping("/user/add-notes")
    public String postMethodName(Model model, @RequestParam("id") Integer id, @RequestParam("noted") String noted,
            RedirectAttributes redirectAttributes) {
        Optional<User> userIslogin = userUtil.getUserCookie();
        if (userIslogin.isPresent()) {
            Comment comment = new Comment();
            comment.setProduct(productJPA.findById(id).get());
            comment.setUser(userIslogin.get());
            comment.setNoted(noted);
            comment.setUpdateAt(new Date());
            comment.setCreateAt(new Date());
            commentJPA.save(comment);
            redirectAttributes.addFlashAttribute("message", "Bình luận của bạn đã được đăng thành công");
        }

        return "redirect:/productdetail?id=" + id;
    }
    @PostMapping("/user/edit-notes")
    public String postEditComment(Model model,
            @RequestParam("id") Integer id, @RequestParam("noted") String noted,
            RedirectAttributes redirectAttributes,@RequestParam("currentUrl") String currentUrl) {
        Optional<Comment> optionalComment = commentJPA.findById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setNoted(noted);
            comment.setUpdateAt(new Date());
            commentJPA.save(comment);
            redirectAttributes.addFlashAttribute("message", "Cập nhật bình luận thành công");
        }
        return "redirect:"+currentUrl ;
    }
    @PostMapping("/user/remove-notes")
    public String postRemoveComment( @RequestParam("id") Integer id,@RequestParam("currentUrl") String currentUrl,RedirectAttributes redirectAttributes) {
        commentJPA.delete(commentJPA.findById(id).get());
        redirectAttributes.addFlashAttribute("message", "Xóa thành công");
        return "redirect:"+currentUrl ;
    }
    
}
