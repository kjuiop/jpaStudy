package com.gig.jpastudy.dto;

import com.gig.jpastudy.types.OrderStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : Jake
 * @date : 2021-05-28
 */
@Getter @Setter
public class OrderSearchDto {

    private String memberName;

    private OrderStatus orderStatus;
}
