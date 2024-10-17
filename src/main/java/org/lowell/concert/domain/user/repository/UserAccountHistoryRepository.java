package org.lowell.concert.domain.user.repository;

import org.lowell.concert.domain.user.dto.UserAccountCommand;

public interface UserAccountHistoryRepository {
    void insert(UserAccountCommand.Action command);
}
