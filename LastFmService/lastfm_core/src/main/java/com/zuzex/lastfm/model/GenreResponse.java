package com.zuzex.lastfm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenreResponse {

    private Long id;

    private String name;

    private List<TrackResponse> tracks;
}
