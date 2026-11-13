package com.molinex.platform.quality.interfaces.rest.transform;

import com.molinex.platform.quality.domain.model.valueobjects.IndicatorValue;
import com.molinex.platform.quality.interfaces.rest.resources.IndicatorResource;

public final class IndicatorResourceFromValueAssembler {
    private IndicatorResourceFromValueAssembler() {
    }

    public static IndicatorResource toResourceFromValue(IndicatorValue v) {
        return new IndicatorResource(v.indicator(), v.value(), v.unit(), v.available(), v.reason(), v.source(),
                v.sourceId(), v.productionRecordId(), v.observedAt());
    }
}
