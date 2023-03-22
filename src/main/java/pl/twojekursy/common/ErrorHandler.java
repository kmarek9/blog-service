package pl.twojekursy.common;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationEx(MethodArgumentNotValidException ex){
        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError -> System.out.println(fieldError.getField() + ": " + fieldError.getDefaultMessage()));

        Map<String, String> fieldsErrorMap = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            if (fieldsErrorMap.put(fieldError.getField(), fieldError.getDefaultMessage()) != null) {
                throw new IllegalStateException("Duplicate key");
            }
        }

        return ResponseEntity.badRequest().body(fieldsErrorMap);
    }

    //obsluzyc wyjątek HttpMessageNotReadableException , 400 , i np komunikat Json jest niepoprawy,

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleValidationEx(HttpMessageNotReadableException ex){
        System.out.println(ex.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse("Invalid JSON"));
    }
}
