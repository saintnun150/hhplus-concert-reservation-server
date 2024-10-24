package org.lowell.concert.interfaces.api.payment;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.application.payment.PaymentFacade;
import org.lowell.concert.application.payment.PaymentInfo;
import org.lowell.concert.interfaces.api.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController implements PaymentApiDocs{
    private final PaymentFacade paymentFacade;

    @SecurityRequirement(name = "queueToken")
    @PostMapping("")
    public ApiResponse<PaymentResponse.Info> payment(@RequestBody PaymentRequest.Create request,
                                                     @RequestHeader("X-QUEUE-TOKEN") String token) {
        PaymentInfo.Info payment = paymentFacade.payment(request.getReservationId(), token);
        PaymentResponse.Info info = PaymentResponse.Info.builder()
                                                        .paymentId(payment.getPaymentId())
                                                        .reservationId(payment.getReservationId())
                                                        .payAmount(payment.getPayAmount())
                                                        .createdAt(payment.getCreatedAt())
                                                        .build();
        return ApiResponse.createOk(info);
    }
}
