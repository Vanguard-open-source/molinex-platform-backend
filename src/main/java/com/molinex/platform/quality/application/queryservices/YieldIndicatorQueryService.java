package com.molinex.platform.quality.application.queryservices;

import com.molinex.platform.quality.domain.model.queries.GetYieldIndicatorsQuery;
import com.molinex.platform.quality.domain.model.valueobjects.IndicatorValue;

import java.util.List;

public interface YieldIndicatorQueryService {
    List<IndicatorValue> handle(GetYieldIndicatorsQuery query);
}
