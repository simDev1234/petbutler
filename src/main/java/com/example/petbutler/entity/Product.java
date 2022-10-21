package com.example.petbutler.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class Product {

  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  @ManyToOne
  private User salesUser;

  @ManyToOne
  private Cart cart;

  @ManyToOne
  private Order order;

  @NotNull
  @ManyToOne
  private Category category;

  private String thumbnailLocalPath;

  private String thumbnailUrlPath;

  @NotBlank
  private String name;

  private String detailInfo;

  @Min(0)
  private long stock;

  @Min(0)
  private long numberOfsales;

  @Min(0)
  @Max(1_000_000)
  private int discount;

  @Min(0)
  @Max(1_000_000)
  private int price;

  @CreatedDate
  private LocalDateTime registeredAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

}
