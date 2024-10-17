package org.lowell.concert.application.user;

import org.lowell.concert.domain.user.model.UserInfo;
import org.lowell.concert.infra.db.user.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserInfo toPojo(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return UserInfo.builder()
                       .userId(userEntity.getUserId())
                       .username(userEntity.getUsername())
                       .createdAt(userEntity.getCreatedAt())
                       .updatedAt(userEntity.getUpdatedAt())
                       .build();
    }
}
