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

    @Min(0)
    private Long id;

    @NotBlank
    private String name;

    @JsonIgnore
    private String mbid;

    @NotNull
    @Positive
    private Integer listeners;

    @NotNull
    @Positive
    private Integer playcount;

    @NotEmpty
    private Artist artist;

    private Album album;

    private List<Genre> genres;
}
