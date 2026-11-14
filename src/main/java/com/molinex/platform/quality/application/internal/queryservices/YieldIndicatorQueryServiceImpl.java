package com.molinex.platform.quality.application.internal.queryservices;

import com.molinex.platform.quality.application.queryservices.YieldIndicatorQueryService;
import com.molinex.platform.quality.domain.model.queries.GetYieldIndicatorsQuery;
import com.molinex.platform.quality.domain.model.valueobjects.IndicatorPeriod;
import com.molinex.platform.quality.domain.model.valueobjects.IndicatorValue;
import com.molinex.platform.quality.domain.model.valueobjects.ProductionRecordId;
import com.molinex.platform.quality.domain.repositories.QualityAssessmentRepository;
import com.molinex.platform.quality.domain.repositories.WasteRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YieldIndicatorQueryServiceImpl implements YieldIndicatorQueryService {
    private final QualityAssessmentRepository assessments;
    private final WasteRecordRepository waste;

    public List<IndicatorValue> handle(GetYieldIndicatorsQuery query) {
        var period = new IndicatorPeriod(query.from(), query.to());
        if (query.productionRecordId() != null) new ProductionRecordId(query.productionRecordId());
        var values = new ArrayList<IndicatorValue>();
        for (var a : assessments.findAll()) {
            if (!period.contains(a.getAssessedAt()) || query.productionRecordId() != null &&
                    !query.productionRecordId().equals(a.getProductionRecordId().value())) continue;
            for (var m : a.getMeasurements())
                values.add(new IndicatorValue(m.indicator().name(), m.value().value(), "%", true, null,
                        "RECORDED_ASSESSMENT", a.getId().value(), a.getProductionRecordId().value(),
                        a.getAssessedAt()));
        }
        for (var w : waste.findAll()) {
            if (!period.contains(w.getRecordedAt()) || query.productionRecordId() != null &&
                    !query.productionRecordId().equals(w.getProductionRecordId().value())) continue;
            values.add(
                    new IndicatorValue("WASTE_PERCENTAGE", w.getPercentage() == null ? null : w.getPercentage().value(),
                            "%", w.getPercentage() != null,
                            w.getPercentage() == null ? "Positive base weight is missing" : null, "CALCULATED_WASTE",
                            w.getId().value(), w.getProductionRecordId().value(), w.getRecordedAt()));
        }
        if (values.isEmpty()) values.add(
                new IndicatorValue("RECORDED_INDICATORS", null, "%", false, "No observations for the requested dataset",
                        "NONE", null, query.productionRecordId(), null));
        values.add(new IndicatorValue("CALCULATED_RICE_YIELD", null, "%", false,
                "Distinct milled output and paddy input weights are not available", "NONE", null,
                query.productionRecordId(), null));
        return List.copyOf(values);
    }
}
