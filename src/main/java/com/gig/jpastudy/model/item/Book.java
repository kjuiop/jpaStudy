package com.gig.jpastudy.model.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
@Getter @Setter
public class Book extends Item {

    private String author;

    private String isbn;
}

/**
 * 상속이 Single Table 전략일 때, 구분자로
 * @DiscriminatorValue 를 사용한다.
 */
