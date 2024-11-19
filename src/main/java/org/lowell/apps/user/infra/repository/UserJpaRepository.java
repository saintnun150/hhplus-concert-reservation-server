package org.lowell.apps.user.infra.repository;

import org.lowell.apps.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
