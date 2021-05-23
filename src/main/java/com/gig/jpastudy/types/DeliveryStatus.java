package com.gig.jpastudy.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {

    READY("ready", "준비"),
    COMPLETE("complete", "완료");

    final private String type;
    final private String description;
}
