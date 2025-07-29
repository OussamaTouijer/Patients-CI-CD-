package com.exemple.patient_service.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenreSexeTest {

    @Test
    void enum_ShouldHaveCorrectValues() {
        // When & Then
        GenreSexe[] values = GenreSexe.values();
        
        assertThat(values).hasSize(4);
        assertThat(values).contains(GenreSexe.HOMME);
        assertThat(values).contains(GenreSexe.FEMME);
        assertThat(values).contains(GenreSexe.AUTRE);
        assertThat(values).contains(GenreSexe.NON_SPECIFIE);
    }

    @Test
    void valueOf_ShouldReturnCorrectEnum_WhenValidStringProvided() {
        // When & Then
        assertThat(GenreSexe.valueOf("HOMME")).isEqualTo(GenreSexe.HOMME);
        assertThat(GenreSexe.valueOf("FEMME")).isEqualTo(GenreSexe.FEMME);
        assertThat(GenreSexe.valueOf("AUTRE")).isEqualTo(GenreSexe.AUTRE);
        assertThat(GenreSexe.valueOf("NON_SPECIFIE")).isEqualTo(GenreSexe.NON_SPECIFIE);
    }

    @Test
    void toString_ShouldReturnCorrectString() {
        // When & Then
        assertThat(GenreSexe.HOMME.toString()).isEqualTo("HOMME");
        assertThat(GenreSexe.FEMME.toString()).isEqualTo("FEMME");
        assertThat(GenreSexe.AUTRE.toString()).isEqualTo("AUTRE");
        assertThat(GenreSexe.NON_SPECIFIE.toString()).isEqualTo("NON_SPECIFIE");
    }

    @Test
    void ordinal_ShouldReturnCorrectOrder() {
        // When & Then
        assertThat(GenreSexe.HOMME.ordinal()).isEqualTo(0);
        assertThat(GenreSexe.FEMME.ordinal()).isEqualTo(1);
        assertThat(GenreSexe.AUTRE.ordinal()).isEqualTo(2);
        assertThat(GenreSexe.NON_SPECIFIE.ordinal()).isEqualTo(3);
    }
}