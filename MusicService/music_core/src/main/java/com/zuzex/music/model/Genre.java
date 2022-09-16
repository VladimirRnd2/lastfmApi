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
public class Genre implements Serializable {

    @Min(0)
    @JsonIgnore
    private Long id;

    @NotBlank
    private String name;

    @JsonIgnore
    private List<Track> tracks;
}
