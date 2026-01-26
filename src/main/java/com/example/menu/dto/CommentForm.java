package com.example.menu.dto;

import com.example.menu.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.lang.ref.PhantomReference;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommentForm {
    private Long id;
    private Long menuId;
    private String text;

    public CommentForm(String text) {
        this.text = text;
    }

    public CommentForm(Long id, Long menuId, String text) {
        this.id = id;
        this.menuId = menuId;
        this.text = text;
    }

    public static CommentForm fromEntity(Comment comment) {
        return new CommentForm(comment.getId(), comment.getMenu().getId(), comment.getText());
    }
}
