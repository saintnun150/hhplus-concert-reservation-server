package org.lowell.apps.payment.domain.dto;

import org.lowell.apps.payment.domain.model.EventStatus;

import java.time.LocalDateTime;

public class PaymentOutBoxQuery {
    public record Get(String eventId) { }
    public record GetList(EventStatus status, LocalDateTime targetDate) { }
}
