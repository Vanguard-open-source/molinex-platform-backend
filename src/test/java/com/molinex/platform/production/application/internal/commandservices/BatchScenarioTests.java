package com.molinex.platform.production.application.internal.commandservices;

import com.molinex.platform.production.domain.model.aggregates.ProductionBatch;
import com.molinex.platform.production.domain.model.commands.RegisterProductionBatchCommand;
import com.molinex.platform.production.domain.model.valueobjects.BatchCode;
import com.molinex.platform.production.domain.model.valueobjects.ProductionBatchId;
import com.molinex.platform.production.domain.model.valueobjects.RawMaterialReceptionId;
import com.molinex.platform.production.domain.repositories.ProductionBatchRepository;
import com.molinex.platform.production.domain.repositories.RawMaterialReceptionRepository;
import com.molinex.platform.shared.application.result.ErrorType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BatchScenarioTests {

    private static final String CODE = "BATCH-01";
    private static final RawMaterialReceptionId RECEPTION = new RawMaterialReceptionId(4L);
    private static final Instant DATE = Instant.parse("2026-11-13T12:00:00Z");

    private final RawMaterialReceptionRepository receptions = mock(RawMaterialReceptionRepository.class);
    private final ProductionBatchRepository batches = mock(ProductionBatchRepository.class);
    private final ProductionCommandServiceImpl service = new ProductionCommandServiceImpl(receptions, batches);

    @Test
    void registersBatchAndReturnsItsAssignedId() {
        when(receptions.existsById(RECEPTION)).thenReturn(true);
        var id = new ProductionBatchId(12L);
        when(batches.save(any())).thenAnswer(invocation -> {
            ProductionBatch batch = invocation.getArgument(0);
            assertEquals(new BatchCode(CODE), batch.code());
            assertEquals(RECEPTION, batch.receptionId());
            assertEquals(DATE, batch.registeredAt().value());
            batch.assignId(id);
            return batch;
        });

        var result = service.handle(validCommand());

        assertTrue(result.isSuccess());
        assertEquals(id, result.value());
        verify(batches).save(any());
    }

    @Test
    void rejectsDuplicateCodeWithoutSaving() {
        when(receptions.existsById(RECEPTION)).thenReturn(true);
        when(batches.existsByCode(new BatchCode(CODE))).thenReturn(true);

        assertEquals(ErrorType.CONFLICT, service.handle(validCommand()).error().type());
        verify(batches, never()).save(any());
    }

    @Test
    void rejectsUnknownReceptionWithoutSaving() {
        assertEquals(ErrorType.NOT_FOUND, service.handle(validCommand()).error().type());
        verifyNoInteractions(batches);
    }

    @ParameterizedTest
    @MethodSource("incompleteCommands")
    void rejectsMissingFieldsBeforeAccessingRepositories(RegisterProductionBatchCommand command) {
        assertEquals(ErrorType.VALIDATION, service.handle(command).error().type());
        verifyNoInteractions(receptions, batches);
    }

    private static Stream<RegisterProductionBatchCommand> incompleteCommands() {
        return Stream.of(
                new RegisterProductionBatchCommand(null, RECEPTION.value(), DATE),
                new RegisterProductionBatchCommand(CODE, null, DATE),
                new RegisterProductionBatchCommand(CODE, RECEPTION.value(), null));
    }

    private static RegisterProductionBatchCommand validCommand() {
        return new RegisterProductionBatchCommand(CODE, RECEPTION.value(), DATE);
    }
}
