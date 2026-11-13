package com.molinex.platform.commercial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.math.BigDecimal;

import com.molinex.platform.commercial.domain.model.aggregates.CommercialInquiry;
import com.molinex.platform.commercial.domain.model.valueobjects.CommercialInquiryId;
import com.molinex.platform.commercial.domain.model.commands.SubmitCommercialInquiryCommand;

import com.molinex.platform.commercial.domain.repositories.CommercialInquiryRepository;
import com.molinex.platform.commercial.application.internal.commandservices.CommercialInquiryCommandServiceImpl;


class InquiryScenarioTests {
    static final Instant TIME = Instant.parse("2026-10-03T10:00:00Z");
    CommercialInquiryRepository repository = mock(CommercialInquiryRepository.class);
    CommercialInquiryCommandServiceImpl service = new CommercialInquiryCommandServiceImpl(repository);

    @BeforeEach
    void setup() {
        when(repository.save(any())).thenAnswer(i -> {
            CommercialInquiry a = i.getArgument(0);
            a.assignId(new CommercialInquiryId(1L));
            return a;
        });
    }

    SubmitCommercialInquiryCommand valid() {
        return new SubmitCommercialInquiryCommand("Ana Torres", "ana@example.com", null, "Rice Mill",
                "Please send information");
    }

    @Test
    @DisplayName("US-36 E1: Inquiry registered")
    void us36Scenario1() throws Exception {
        var result = service.handle(valid());
        assertTrue(result.isSuccess());
        assertNotNull(result.value().getId());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("US-36 E2: Empty required fields")
    void us36Scenario2() throws Exception {
        assertFalse(
                service.handle(new SubmitCommercialInquiryCommand("", "ana@example.com", null, "Rice Mill", "Question"))
                        .isSuccess());
        assertFalse(service.handle(new SubmitCommercialInquiryCommand("Ana", "ana@example.com", null, "Rice Mill", ""))
                .isSuccess());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("US-36 E3: Invalid email format")
    void us36Scenario3() throws Exception {
        assertFalse(service.handle(new SubmitCommercialInquiryCommand("Ana", "invalid", null, "Rice Mill", "Question"))
                .isSuccess());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("US-36 E4: Inquiry persistence failure")
    void us36Scenario4() throws Exception {
        doThrow(new IllegalStateException("Database unavailable")).when(repository).save(any());
        var command = valid();
        var result = service.handle(command);
        assertFalse(result.isSuccess());
        assertEquals("UNEXPECTED_ERROR", result.error().code());
        assertEquals("Please send information", command.message());
    }

}
