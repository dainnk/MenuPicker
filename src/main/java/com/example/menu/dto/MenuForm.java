package com.example.menu.dto;

import com.example.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@ToString
public class MenuForm {
    private Long id;
    private String title;
    private String content;

    public MenuForm() {}

//    public MenuForm(Long id, String title, String content) {
//        this.id = id;
//        this.title = title;
//        this.content = content;
//    }
//
//
//    @Override
//    public String toString() {
//        return "MenuForm{" +
//                "title='" + title + '\'' +
//                ", content='" + content + '\'' +
//                '}';
//    }

    public Menu toEntity() {
        return new Menu(id, title, content);
    }
}
