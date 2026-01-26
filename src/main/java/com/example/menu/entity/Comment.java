package com.example.menu.entity;

import com.example.menu.dto.CommentForm;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column
    private String text;

    public Comment(Long id, Menu menu, String text) {
        this.id = id;
        this.menu = menu;
        this.text = text;
    }

    public static Comment createComment(String requestText, Menu menuEntity) {
        return new Comment(null, menuEntity, requestText);
    }

    public static Comment createCommentFromForm(CommentForm form, Menu menuEntity) {
        return new Comment(null, menuEntity, form.getText());
    }
}
