package co.id.jalin.qrmapper.alto.controller;

import co.id.jalin.qrmapper.alto.dto.AltoPaymentCreditRequestDto;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentCreditResponseDto;
import co.id.jalin.qrmapper.alto.service.AltoPaymentCreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AltoPaymentCreditController extends AltoContextController{

    private final AltoPaymentCreditService altoPaymentCreditService;

    @PostMapping("${api.path.alto.qr.payment}")
    public ResponseEntity<AltoPaymentCreditResponseDto> paymentCredit(
            @RequestHeader Map<String, String> headers,
            @RequestBody AltoPaymentCreditRequestDto request
    ) {
        return ResponseEntity.ok(
                altoPaymentCreditService.paymentCredit(headers,request)
        );
    }
}
