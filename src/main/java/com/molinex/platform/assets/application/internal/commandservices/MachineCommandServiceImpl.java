package com.molinex.platform.assets.application.internal.commandservices;

import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.MachineCode;
import com.molinex.platform.assets.domain.model.valueobjects.MachineModel;
import com.molinex.platform.assets.domain.model.valueobjects.MachineName;
import com.molinex.platform.assets.domain.model.commands.RegisterMachineCommand;

import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import com.molinex.platform.assets.application.commandservices.MachineCommandService;
import com.molinex.platform.assets.domain.model.aggregates.Machine;
import com.molinex.platform.assets.domain.repositories.MachineRepository;

@Service
@RequiredArgsConstructor
public class MachineCommandServiceImpl implements MachineCommandService {
    private final MachineRepository repository;

    @Override
    public Result<Machine, ApplicationError> handle(RegisterMachineCommand command) {
        try {
            if (command.statusChangedAt() == null)
                return Result.failure(ApplicationError.validationError("statusChangedAt", "Required for new machines"));
            if (repository.existsByCode(new MachineCode(command.code())))
                return Result.failure(ApplicationError.conflict("Machine", "Machine code already exists"));
            var aggregate = new Machine(null, new MachineCode(command.code()), new MachineName(command.name()),
                    new MachineModel(command.model()), command.status(), command.statusChangedAt());
            return Result.success(repository.save(aggregate));
        } catch (IllegalArgumentException ex) {
            return Result.failure(ApplicationError.validationError("Machine", ex.getMessage()));
        } catch (DataIntegrityViolationException ex) {
            return Result.failure(ApplicationError.conflict("Machine", "Conflicting stored data"));
        } catch (RuntimeException ex) {
            return Result.failure(ApplicationError.unexpected("Machine", "The operation could not be confirmed"));
        }
    }
}
