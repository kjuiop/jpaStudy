package com.gig.jpastudy.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    ORDER("order", "주문"),
    CANCEL("cancel", "취소");

    final private String type;
    final private String description;
}
