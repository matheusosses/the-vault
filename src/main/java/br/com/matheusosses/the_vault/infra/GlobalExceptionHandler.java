package br.com.matheusosses.the_vault.infra;

import br.com.matheusosses.the_vault.infra.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponseDto>> handleValidationErrors(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors().stream()
            .map(error -> new ErrorResponseDto(error.getField(), error.getDefaultMessage()))
            .toList();
        return ResponseEntity.badRequest().body(errors);
    }
}
