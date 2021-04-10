package io.github.slotap.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>>handleException(
            HttpServletRequest request, MethodArgumentNotValidException e) {

        var fieldErrors = e.getFieldErrors();

        List<Map<String,String>> fieldErrorsMap = fieldErrors.stream()
                .map(fieldError ->  Map.ofEntries(
                            Map.entry("field", fieldError.getField()),
                            Map.entry("message", Optional.ofNullable(fieldError.getDefaultMessage()).orElse("null"))))
                .collect(Collectors.toList());

        Map<String,Object> errorResponsePayload =
                Map.of("path", request.getServletPath(), "errors", fieldErrorsMap);

        return ResponseEntity.badRequest().body(errorResponsePayload);
    }
}