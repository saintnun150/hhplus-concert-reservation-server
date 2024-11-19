package org.lowell.apps.payment.interfaces.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.lowell.apps.common.api.ApiResponse;

@Tag(name = "결제 API", description = "결제 관련 API")
public interface PaymentApiDocs {

    @Operation(summary = "결제 요청", description = "예약 Id를 통해 결제를 완료한다.")
    ApiResponse<PaymentResponse.Info> payment(PaymentRequest.Create request,
                                              @Parameter(hidden = true) String token);

}
