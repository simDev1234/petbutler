package com.example.petbutler.admin.model;

import com.example.petbutler.model.constants.UserRole;
import com.example.petbutler.model.constants.UserStatus;
import com.example.petbutler.persist.converter.StringListConverter;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminUserListForm {

  // 검색 조건
  private String searchKey;
  private String searchValue;

  // 결과
  Page<AdminUserInfo> pageResult;

}
