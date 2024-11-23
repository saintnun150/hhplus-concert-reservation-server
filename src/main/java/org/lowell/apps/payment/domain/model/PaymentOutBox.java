package org.lowell.apps.payment.domain.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_payment_outbox")
@Getter
@NoArgsConstructor
public class PaymentOutBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "event_id")
    private String eventId;

    @Column(name = "event_type")
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(name = "event_status")
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload")
    private ObjectNode payload;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public static PaymentOutBox create(String eventId, EventType eventType, ObjectNode payload) {
        PaymentOutBox eventOutBox = new PaymentOutBox();
        eventOutBox.eventId = eventId;
        eventOutBox.eventType = eventType;
        eventOutBox.payload = payload;
        eventOutBox.eventStatus = EventStatus.INIT;
        eventOutBox.createdAt = LocalDateTime.now();
        return eventOutBox;
    }

    public void success() {
        this.eventStatus = EventStatus.SUCCESS;
        this.updatedAt = LocalDateTime.now();
    }

}
