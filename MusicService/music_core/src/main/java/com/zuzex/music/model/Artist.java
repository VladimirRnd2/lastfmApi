package com.zuzex.music.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Artist implements Serializable {

    private Long id;

    private String name;

    @JsonIgnore
    private String mbid;

    @JsonIgnore
    private List<Album> albums;

    @JsonIgnore
    private List<Track> tracks;

}
