package com.gig.jpastudy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gig.jpastudy.model.embedded.Address;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member extends BaseEntity {

    @Id @GeneratedValue
    private Long memberId;

    private String username;

    private String name;

    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
