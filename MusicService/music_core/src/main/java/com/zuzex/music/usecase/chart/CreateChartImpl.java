package com.zuzex.music.usecase.chart;

import com.zuzex.music.model.Chart;
import com.zuzex.music.usecase.track.port.TrackStorageService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class CreateChartImpl implements CreateChart {

    TrackStorageService trackStorageService;

    @Override
    public Mono<Chart> createChart(String method) {
        Mono<Chart> playcounts = trackStorageService.createChartByPlayCounts().collectList().map(tracks -> Chart.builder().trackList(tracks).build());

        return method.equals("listeners") ? trackStorageService.createChartByListeners().collectList().map(tracks -> Chart.builder().trackList(tracks).build())
                : trackStorageService.createChartByPlayCounts().collectList().map(tracks -> Chart.builder().trackList(tracks).build());
    }
}
