package com.example.petbutler.domain.posting;

import com.example.petbutler.domain.user.User;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
@Setter
public class Review {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  private User user;

  private String contents;

}
