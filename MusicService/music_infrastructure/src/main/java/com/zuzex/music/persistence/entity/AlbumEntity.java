package com.zuzex.music.persistence.entity;

import com.zuzex.music.model.Artist;
import com.zuzex.music.model.Track;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "album")
public class AlbumEntity implements Serializable {

    @Id
    private Long id;

    private String name;

    private String mbid;

    @Column(value = "artist_id")
    private Long artistId;
}
