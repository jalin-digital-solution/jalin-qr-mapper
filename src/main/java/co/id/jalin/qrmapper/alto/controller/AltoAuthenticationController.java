package co.id.jalin.qrmapper.alto.controller;

import co.id.jalin.qrmapper.alto.dto.AltoAuthTokenRequest;
import co.id.jalin.qrmapper.alto.dto.AltoAuthTokenResponse;
import co.id.jalin.qrmapper.alto.service.AltoAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AltoAuthenticationController extends AltoContextController{

    private final AltoAuthenticationService altoAuthenticationService;

    @PostMapping("${api.path.alto.auth.token}")
    public ResponseEntity<AltoAuthTokenResponse> token(
            @RequestHeader("Authorization") String authorization,
            @RequestBody AltoAuthTokenRequest request
    ) {
        return ResponseEntity.ok(
                altoAuthenticationService.authenticate(authorization, request)
        );
    }
}
