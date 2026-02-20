package co.id.jalin.qrmapper.controller.transaction;

import co.id.jalin.qrmapper.dto.transaction.PaymentRefundRequestDto;
import co.id.jalin.qrmapper.dto.transaction.PaymentRefundResponseDto;
import co.id.jalin.qrmapper.service.transaction.PaymentRefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PaymentRefundController {

    private final PaymentRefundService paymentRefundService;

    @PostMapping("${api.path.esb.qr.refund}")
    public ResponseEntity<PaymentRefundResponseDto> paymentRefund(
            @RequestHeader Map<String, String> headers,
            @RequestBody PaymentRefundRequestDto request
    ) {
        return ResponseEntity.ok(
                paymentRefundService.paymentRefund(headers,request)
        );
    }
}
