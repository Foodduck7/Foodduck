package com.example.foodduck.menu.entity;

import com.example.foodduck.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "menu_options")
public class MenuOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionName;

    private String contents;

    private int optionPrice;

    @Enumerated(EnumType.STRING)
    private OptionStatus optionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public MenuOption() {

    }

    public MenuOption(String optionName, String contents, int optionPrice, Menu menu) {
        this.optionName = optionName;
        this.contents = contents;
        this.optionPrice = optionPrice;
        this.optionStatus = OptionStatus.ON_SALE;
        this.menu = menu;
    }
}
