package com.molinex.platform.assets.application.commandservices;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.molinex.platform.assets.domain.model.commands.RegisterMachineCommand;

import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import com.molinex.platform.assets.domain.model.aggregates.Machine;

public interface MachineCommandService {
    Result<Machine, ApplicationError> handle(RegisterMachineCommand command);
}
