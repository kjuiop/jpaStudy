package com.gig.jpastudy.model.item;

import com.gig.jpastudy.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long itemId;

    private String name;

    private int price;

    private int stockQuantity;

    @OneToMany(mappedBy = "item")
    private List<CategoryItem> categoryItems = new ArrayList<>();

    public void setCategoryItem(CategoryItem categoryItem) {
        categoryItems.add(categoryItem);
        categoryItem.setItem(this);
    }

}

/**
 * @Inheritance 로 상속 테이블 구조이며, 전략은 SingleTable 이다.
 * @DiscriminatorColumn 으로 Child 테이블의 구분자로 사용한다.
 */