package com.zuzex.music.controller.impl;

import com.zuzex.music.controller.ChartController;
import com.zuzex.music.model.Chart;
import com.zuzex.music.usecase.chart.CreateChart;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chart")
@RequiredArgsConstructor
public class ChartControllerImpl implements ChartController {

    private final CreateChart createChart;

    @Override
    @GetMapping("/listeners")
    public Mono<Chart> createChartByListeners() {
        return createChart.createChart("listeners");
    }

    @Override
    @GetMapping("/playcounts")
    public Mono<Chart> createChartByPlayCounts() {
        return createChart.createChart("playcounts");
    }
}
