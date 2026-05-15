package br.com.matheusosses.the_vault.infra;

import br.com.matheusosses.the_vault.infra.dto.ErrorResponseDto;
import br.com.matheusosses.the_vault.infra.dto.StandardError;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        var fieldErrors = ex.getFieldErrors().stream()
            .map(ErrorResponseDto::new)
            .toList();

        var error = new StandardError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Erro de Validação",
            "Um ou mais campos estão inválidos",
            request.getRequestURI(),
            fieldErrors
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> handleItemNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        var error = new StandardError(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Recurso não encontrado",
            ex.getMessage(),
            request.getRequestURI(),
            null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> handleConflict(DataIntegrityViolationException ex, HttpServletRequest request) {
        var error = new StandardError(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            "Conflito de Integridade",
            "O registro já existe ou viola uma regra de negócio",
            request.getRequestURI(),
            null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleGlobal(Exception ex, HttpServletRequest request) {
        var error = new StandardError(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro Interno",
            "Ocorreu um erro inesperado no servidor",
            request.getRequestURI(),
            null
        );

        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        var error = new StandardError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Requisição Inválida",
            ex.getMessage(),
            request.getRequestURI(),
            null
        );

        return ResponseEntity.badRequest().body(error);
    }
}
