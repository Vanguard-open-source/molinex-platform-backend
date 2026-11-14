package com.molinex.platform.assets.application.internal.commandservices;

import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.AnomalyReference;
import com.molinex.platform.assets.domain.model.valueobjects.CorrectiveMaintenanceDetails;
import com.molinex.platform.assets.domain.model.valueobjects.MachineId;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceDescription;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceType;
import com.molinex.platform.assets.domain.model.valueobjects.TechnicianReference;
import com.molinex.platform.assets.domain.model.commands.RecordCorrectiveMaintenanceCommand;
import com.molinex.platform.assets.domain.model.commands.RecordPreventiveMaintenanceCommand;

import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import com.molinex.platform.assets.application.commandservices.MaintenanceRecordCommandService;
import com.molinex.platform.assets.domain.model.aggregates.MaintenanceRecord;
import com.molinex.platform.assets.domain.repositories.MachineRepository;
import com.molinex.platform.assets.domain.repositories.MaintenanceRecordRepository;

@Service
@RequiredArgsConstructor
public class MaintenanceRecordCommandServiceImpl implements MaintenanceRecordCommandService {
    private final MaintenanceRecordRepository repository;
    private final MachineRepository machines;

    @Override
    public Result<MaintenanceRecord, ApplicationError> handle(RecordPreventiveMaintenanceCommand command) {
        try {
            var id = new MachineId(command.machineId());
            if (machines.findById(id).isEmpty())
                return Result.failure(ApplicationError.notFound("Machine", id.value().toString()));
            var aggregate = new MaintenanceRecord(null, new MachineId(command.machineId()), MaintenanceType.PREVENTIVE,
                    command.performedAt(), new MaintenanceDescription(command.description()),
                    new TechnicianReference(command.responsiblePrincipalId(), command.responsibleDisplayName()), null,
                    null);
            return Result.success(repository.save(aggregate));
        } catch (IllegalArgumentException ex) {
            return Result.failure(ApplicationError.validationError("MaintenanceRecord", ex.getMessage()));
        } catch (DataIntegrityViolationException ex) {
            return Result.failure(ApplicationError.conflict("MaintenanceRecord", "Conflicting stored data"));
        } catch (RuntimeException ex) {
            return Result.failure(
                    ApplicationError.unexpected("MaintenanceRecord", "The operation could not be confirmed"));
        }
    }

    @Override
    public Result<MaintenanceRecord, ApplicationError> handle(RecordCorrectiveMaintenanceCommand command) {
        try {
            var id = new MachineId(command.machineId());
            if (machines.findById(id).isEmpty())
                return Result.failure(ApplicationError.notFound("Machine", id.value().toString()));
            var aggregate = new MaintenanceRecord(null, new MachineId(command.machineId()), MaintenanceType.CORRECTIVE,
                    command.performedAt(), new MaintenanceDescription(command.description()),
                    new TechnicianReference(command.responsiblePrincipalId(), command.responsibleDisplayName()),
                    command.anomalyId() == null ? null : new AnomalyReference(command.anomalyId()),
                    new CorrectiveMaintenanceDetails(command.failure(), command.cause(), command.actionTaken(),
                            command.downtimeMinutes()));
            return Result.success(repository.save(aggregate));
        } catch (IllegalArgumentException ex) {
            return Result.failure(ApplicationError.validationError("MaintenanceRecord", ex.getMessage()));
        } catch (DataIntegrityViolationException ex) {
            return Result.failure(ApplicationError.conflict("MaintenanceRecord", "Conflicting stored data"));
        } catch (RuntimeException ex) {
            return Result.failure(
                    ApplicationError.unexpected("MaintenanceRecord", "The operation could not be confirmed"));
        }
    }
}
