package co.id.jalin.qrmapper.alto.controller;

import co.id.jalin.qrmapper.alto.dto.AltoPaymentStatusRequestDto;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentStatusResponseDto;
import co.id.jalin.qrmapper.alto.service.AltoPaymentStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AltoPaymentStatusController extends AltoContextController{

    private final AltoPaymentStatusService altoPaymentStatusService;

    @PostMapping("${api.path.alto.qr.status}")
    public ResponseEntity<AltoPaymentStatusResponseDto> paymentStatus(
            @RequestHeader Map<String, String> headers,
            @RequestBody AltoPaymentStatusRequestDto request
    ) {
        return ResponseEntity.ok(
                altoPaymentStatusService.paymentStatus(headers,request)
        );
    }
}
