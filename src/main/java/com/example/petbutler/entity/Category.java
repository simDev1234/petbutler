package com.example.petbutler.entity;

import com.example.petbutler.type.CategoryLevel;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
@Setter
public class Category {

  @Id
  @GeneratedValue
  private Long id;

  @Enumerated(value = EnumType.STRING)
  private CategoryLevel categoryDepth;

  private String name;

  @CreatedDate
  private LocalDateTime registeredAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

}
