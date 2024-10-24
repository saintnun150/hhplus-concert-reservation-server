package org.lowell.concert.application.user;

import org.lowell.concert.domain.user.model.UserAccountInfo;
import org.lowell.concert.infra.db.user.entity.UserAccountEntity;
import org.springframework.stereotype.Component;

@Component
public class UserAccountMapper {
    public UserAccountInfo toPojo(UserAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return UserAccountInfo.builder()
                              .userAccountId(entity.getUserAccountId())
                              .userId(entity.getUserId())
                              .balance(entity.getBalance())
                              .createdAt(entity.getCreatedAt())
                              .updatedAt(entity.getUpdatedAt())
                              .build();
    }
}
