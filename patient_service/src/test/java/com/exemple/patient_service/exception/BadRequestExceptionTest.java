package com.exemple.patient_service.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BadRequestExceptionTest {

    @Test
    void constructor_ShouldCreateExceptionWithMessage() {
        // Given
        String message = "Un patient avec ce numéro de sécurité sociale existe déjà";

        // When
        BadRequestException exception = new BadRequestException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void constructor_ShouldHandleNullMessage() {
        // When
        BadRequestException exception = new BadRequestException(null);

        // Then
        assertThat(exception.getMessage()).isNull();
    }

    @Test
    void constructor_ShouldHandleEmptyMessage() {
        // Given
        String message = "";

        // When
        BadRequestException exception = new BadRequestException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}