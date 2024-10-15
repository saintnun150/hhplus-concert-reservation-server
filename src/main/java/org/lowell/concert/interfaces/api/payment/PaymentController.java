package org.lowell.concert.interfaces.api.payment;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.lowell.concert.interfaces.api.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController implements PaymentApiDocs{

    @SecurityRequirement(name = "queueToken")
    @PostMapping("")
    public ApiResponse<PaymentResponse.Info> payment(@RequestBody PaymentRequest.Create request,
                                                     @RequestHeader("X-QUEUE-TOKEN") String token) {
        return ApiResponse.createOk(PaymentResponse.Info.builder()
                                                        .paymentId(1L)
                                                        .concertId(10L)
                                                        .concertName("싸이 콘서트")
                                                        .seatNo(15L)
                                                        .price(50_000L)
                                                        .completedAt(LocalDateTime.now())
                                                        .build());
    }
}
