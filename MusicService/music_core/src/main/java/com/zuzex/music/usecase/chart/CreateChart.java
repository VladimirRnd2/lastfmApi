package com.zuzex.music.usecase.chart;

import com.zuzex.music.model.Chart;
import reactor.core.publisher.Mono;

public interface CreateChart {

    Mono<Chart> createChart(String method);
}
