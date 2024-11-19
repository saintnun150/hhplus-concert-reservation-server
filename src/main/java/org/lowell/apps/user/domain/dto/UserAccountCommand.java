package org.lowell.apps.user.domain.dto;

public class UserAccountCommand {
    public record Action(Long userId, long amount) {}
}
