package usercase;

import com.zuzex.music.model.Artist;
import com.zuzex.music.usecase.artist.ArtistService;
import com.zuzex.music.usecase.artist.port.ArtistStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CreateArtistImplTest {

    @Mock
    private ArtistStorageService artistStorageService;

    @InjectMocks
    private ArtistService artistService;

    private Artist artist;

    @BeforeEach
    void init() {
        artist = Artist.builder()
                .id(1L)
                .name("Papa Rome")
                .mbid("Papa Rome")
                .build();
    }

    @Test
    void createArtist() {
        when(artistStorageService.createArtist(any(Artist.class))).thenReturn(Mono.just(artist));
        Mono<Artist> artistMono = artistService.createArtist(Artist.builder().build());
        StepVerifier.create(artistMono)
                .consumeNextWith(newArtist -> {
                    assertNotNull(newArtist);
                    assertEquals(newArtist.getId(),artist.getId());
                    assertEquals(newArtist.getName(),artist.getName());
                    assertEquals(newArtist.getMbid(),artist.getMbid());
                }).verifyComplete();
    }
}