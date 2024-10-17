package org.lowell.concert.domain.user.repository;

import org.lowell.concert.domain.user.dto.UserAccountCommand;
import org.lowell.concert.domain.user.dto.UserAccountHistoryCommand;

public interface UserAccountHistoryRepository {
    void insert(UserAccountCommand.Action command);
}
