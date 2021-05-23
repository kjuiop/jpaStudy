package com.gig.jpastudy.model;

import com.sun.istack.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class BaseEntity {

    @NotNull
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    private String lastModifier;

    private String createdBy;

    @PrePersist
    public void beforePersist() {
        LocalDateTime dateTime = LocalDateTime.now();
        this.createdAt = dateTime;
        this.updatedAt = dateTime;
    }

    @PreUpdate
    public void beforeUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
