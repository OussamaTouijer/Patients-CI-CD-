package com.exemple.patient_service.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceNotFoundExceptionTest {

    @Test
    void constructor_ShouldCreateExceptionWithCorrectMessage_WhenStringFieldProvided() {
        // Given
        String resourceName = "Patient";
        String fieldName = "nom";
        String fieldValue = "Ahmed";

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

        // Then
        String expectedMessage = String.format("%s non trouvé avec %s : '%s'", resourceName, fieldName, fieldValue);
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    void constructor_ShouldCreateExceptionWithCorrectMessage_WhenLongFieldProvided() {
        // Given
        String resourceName = "Patient";
        String fieldName = "id";
        Long fieldValue = 1L;

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

        // Then
        String expectedMessage = String.format("%s non trouvé avec %s : '%s'", resourceName, fieldName, fieldValue);
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    void constructor_ShouldCreateExceptionWithCorrectMessage_WhenObjectFieldProvided() {
        // Given
        String resourceName = "Patient";
        String fieldName = "status";
        Object fieldValue = Boolean.TRUE;

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

        // Then
        String expectedMessage = String.format("%s non trouvé avec %s : '%s'", resourceName, fieldName, fieldValue);
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    void constructor_ShouldHandleNullValues() {
        // Given
        String resourceName = "Patient";
        String fieldName = "email";
        String fieldValue = null;

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

        // Then
        String expectedMessage = String.format("%s non trouvé avec %s : '%s'", resourceName, fieldName, fieldValue);
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }
}