package com.zuzex.music.controller;

import com.zuzex.music.model.Chart;
import reactor.core.publisher.Mono;

public interface ChartController {

    Mono<Chart> createChartByListeners();

    Mono<Chart> createChartByPlayCounts();
}
