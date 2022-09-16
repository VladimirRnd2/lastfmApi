package com.zuzex.music.usecase.chart.port;

import com.zuzex.music.model.Chart;
import reactor.core.publisher.Mono;

public interface ChartRepositoryService {

    Mono<Chart> createChart(String method);
}
