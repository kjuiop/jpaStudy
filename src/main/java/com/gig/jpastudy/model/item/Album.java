package com.gig.jpastudy.model.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
@Getter @Setter
public class Album extends Item {

    private String artist;

    private String etc;
}

/**
 * 상속이 Single Table 전략일 때, 구분자로
 * @DiscriminatorValue 를 사용한다.
 */