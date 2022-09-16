package usercase;

import com.zuzex.music.model.Album;
import com.zuzex.music.model.Artist;
import com.zuzex.music.model.Genre;
import com.zuzex.music.model.Track;
import com.zuzex.music.usecase.track.CreateTrackImpl;
import com.zuzex.music.usecase.track.port.TrackRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CreateTrackImplTest {

    @Mock
    private TrackRepositoryService trackRepositoryService;

    @InjectMocks
    private CreateTrackImpl createTrack;

    private Track track;

    @BeforeEach
    void init() {
        track = Track.builder()
                .id(1L)
                .name("Track")
                .mbid("Track")
                .listeners(100)
                .playcount(100)
                .artist(Artist.builder().id(1L).name("Artist").mbid("Artist").build())
                .album(Album.builder().id(1L).name("Album").mbid("Album").build())
                .genres(List.of(Genre.builder().id(1L).name("Pop").build()))
                .build();
    }

    @Test
    void createTrack() {
        when(trackRepositoryService.saveTrack(any(Track.class))).thenReturn(Mono.just(track));
        Mono<Track> trackMono = createTrack.createTrack(Track.builder().build());
        StepVerifier.create(trackMono)
                .consumeNextWith(newTrack -> {
                    assertNotNull(newTrack);
                    assertEquals(newTrack.getId(),track.getId());
                    assertEquals(newTrack.getName(),track.getName());
                    assertEquals(newTrack.getMbid(),track.getMbid());
                    assertEquals(newTrack.getListeners(),track.getListeners());
                    assertEquals(newTrack.getPlaycount(),track.getPlaycount());
                    assertEquals(newTrack.getArtist().getId(),track.getArtist().getId());
                    assertEquals(newTrack.getArtist().getName(),track.getArtist().getName());
                    assertEquals(newTrack.getArtist().getMbid(),track.getArtist().getMbid());
                    assertEquals(newTrack.getAlbum().getId(),track.getAlbum().getId());
                    assertEquals(newTrack.getAlbum().getName(),track.getAlbum().getName());
                    assertEquals(newTrack.getAlbum().getMbid(),track.getAlbum().getMbid());
                    assertEquals(newTrack.getGenres().get(0).getId(),track.getGenres().get(0).getId());
                    assertEquals(newTrack.getGenres().get(0).getName(),track.getGenres().get(0).getName());
                }).verifyComplete();
    }

    @Test
    void createTracksByAlbum() {
        when(trackRepositoryService.saveAll(any())).thenReturn(Flux.just(track));
        Flux<Track> tracksByAlbum = createTrack.createTracksByAlbum(List.of(Track.builder().build()));
        StepVerifier.create(tracksByAlbum)
                .consumeNextWith(newTrack -> {
                    assertNotNull(newTrack);
                    assertEquals(newTrack.getId(),track.getId());
                    assertEquals(newTrack.getName(),track.getName());
                    assertEquals(newTrack.getMbid(),track.getMbid());
                    assertEquals(newTrack.getListeners(),track.getListeners());
                    assertEquals(newTrack.getPlaycount(),track.getPlaycount());
                    assertEquals(newTrack.getArtist().getId(),track.getArtist().getId());
                    assertEquals(newTrack.getArtist().getName(),track.getArtist().getName());
                    assertEquals(newTrack.getArtist().getMbid(),track.getArtist().getMbid());
                    assertEquals(newTrack.getAlbum().getId(),track.getAlbum().getId());
                    assertEquals(newTrack.getAlbum().getName(),track.getAlbum().getName());
                    assertEquals(newTrack.getAlbum().getMbid(),track.getAlbum().getMbid());
                    assertEquals(newTrack.getGenres().get(0).getId(),track.getGenres().get(0).getId());
                    assertEquals(newTrack.getGenres().get(0).getName(),track.getGenres().get(0).getName());
                }).verifyComplete();
    }
}