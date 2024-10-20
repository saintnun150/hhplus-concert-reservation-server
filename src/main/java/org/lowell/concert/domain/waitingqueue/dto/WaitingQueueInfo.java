package org.lowell.concert.domain.waitingqueue.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueue;

@Getter
@RequiredArgsConstructor
public class WaitingQueueInfo {
    private final WaitingQueue waitingQueue;
    private final Long order;
}
