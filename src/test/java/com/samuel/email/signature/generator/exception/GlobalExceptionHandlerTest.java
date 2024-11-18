package com.samuel.email.signature.generator.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleValidationExceptions() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("objectName", "field1", "Field1 is invalid");
        FieldError fieldError2 = new FieldError("objectName", "field2", "Field2 is required");
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Field1 is invalid", response.getBody().get("field1"));
        assertEquals("Field2 is required", response.getBody().get("field2"));
    }

    @Test
    void testHandleGenericExceptionWithMessageContainingColon() {
        Exception exception = new Exception("Error: Something went wrong");
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Something went wrong", response.getBody().get("error"));
    }

    @Test
    void testHandleGenericExceptionWithoutMessageContainingColon() {
        Exception exception = new Exception("A generic error occurred");
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("A generic error occurred", response.getBody().get("error"));
    }

    @Test
    void testHandleGenericExceptionWithNullMessage() {
        Exception exception = new Exception((String) null);
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(null, response.getBody().get("error"));
    }
}
