package com.zuzex.music.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Track implements Serializable {

    private Long id;

    private String name;

    @JsonIgnore
    private String mbid;

    private Integer listeners;

    private Integer playcount;

    private Artist artist;

    private Album album;

    private List<Genre> genres;
}
