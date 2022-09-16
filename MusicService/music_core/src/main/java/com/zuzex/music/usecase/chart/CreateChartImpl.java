package com.zuzex.music.usecase.chart;

import com.zuzex.music.model.Chart;
import com.zuzex.music.model.Track;
import com.zuzex.music.usecase.chart.port.ChartRepositoryService;
import com.zuzex.music.usecase.track.port.TrackRepositoryService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class CreateChartImpl implements CreateChart {

    TrackRepositoryService trackRepositoryService;

    @Override
    public Mono<Chart> createChart(String method) {
        Mono<Chart> playcounts = trackRepositoryService.createChartByPlayCounts().collectList().map(tracks -> Chart.builder().trackList(tracks).build());

        return method.equals("listeners") ? trackRepositoryService.createChartByListeners().collectList().map(tracks -> Chart.builder().trackList(tracks).build())
                : trackRepositoryService.createChartByPlayCounts().collectList().map(tracks -> Chart.builder().trackList(tracks).build());
    }
}
