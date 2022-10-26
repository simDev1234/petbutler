package com.example.petbutler.domain.store;

import com.example.petbutler.domain.user.User;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
@Builder
@Getter
@Setter
public class Product {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  private User salesUser;

  @ManyToOne
  private Cart cart;

  @ManyToOne
  private Order order;

  @ManyToOne
  private Category category;

  private String thumbnailLocalPath;

  private String thumbnailUrlPath;

  private String name;

  private String detailInfo;

  private long stock;

  private long numberOfsales;

  private int discount;

  private int price;

  @CreatedDate
  private LocalDateTime registeredAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

}
