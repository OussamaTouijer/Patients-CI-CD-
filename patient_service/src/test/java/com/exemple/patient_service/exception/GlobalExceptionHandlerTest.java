package com.exemple.patient_service.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).thenReturn("uri=/patients");
    }

    @Test
    void handleResourceNotFoundException_ShouldReturnNotFoundResponse() {
        // Given
        ResourceNotFoundException exception = new ResourceNotFoundException("Patient", "id", 1L);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceNotFoundException(exception, webRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Patient");
        assertThat(response.getBody().getMessage()).contains("id");
        assertThat(response.getBody().getMessage()).contains("1");
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getPath()).isEqualTo("uri=/patients");
    }

    @Test
    void handleBadRequestException_ShouldReturnBadRequestResponse() {
        // Given
        BadRequestException exception = new BadRequestException("Invalid data provided");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBadRequestException(exception, webRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid data provided");
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getPath()).isEqualTo("uri=/patients");
    }

    @Test
    void handleMethodArgumentNotValid_ShouldReturnBadRequestWithFieldErrors() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        HttpHeaders headers = new HttpHeaders();
        HttpStatusCode status = HttpStatus.BAD_REQUEST;
        
        FieldError fieldError1 = new FieldError("patientDTO", "nom", "Le nom est obligatoire");
        FieldError fieldError2 = new FieldError("patientDTO", "email", "Le format de l'email est invalide");
        List<FieldError> fieldErrors = Arrays.asList(fieldError1, fieldError2);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));

        // When
        ResponseEntity<Object> response = globalExceptionHandler.handleMethodArgumentNotValid(exception, headers, status, webRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(ValidationErrorResponse.class);
        
        ValidationErrorResponse validationResponse = (ValidationErrorResponse) response.getBody();
        assertThat(validationResponse.getMessage()).isEqualTo("Erreur de validation");
        assertThat(validationResponse.getStatus()).isEqualTo(400);
        assertThat(validationResponse.getValidationErrors()).hasSize(2);
        assertThat(validationResponse.getValidationErrors().get("nom")).isEqualTo("Le nom est obligatoire");
        assertThat(validationResponse.getValidationErrors().get("email")).isEqualTo("Le format de l'email est invalide");
    }

    @Test
    void handleGlobalException_ShouldReturnInternalServerErrorResponse() {
        // Given
        Exception exception = new RuntimeException("Unexpected error occurred");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGlobalException(exception, webRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Unexpected error occurred");
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getPath()).isEqualTo("uri=/patients");
    }
}