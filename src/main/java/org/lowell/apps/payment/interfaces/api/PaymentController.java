package org.lowell.apps.payment.interfaces.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lowell.apps.payment.application.PaymentFacade;
import org.lowell.apps.payment.application.PaymentInfo;
import org.lowell.apps.common.api.ApiResponse;
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
