package com.zuzex.music.persistence.entity;

import com.zuzex.music.model.Album;
import com.zuzex.music.model.Track;
import liquibase.pro.packaged.B;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "artist")
public class ArtistEntity implements Serializable {

    @Id
    private Long id;

    private String name;

    private String mbid;

}
