package usercase;

import com.zuzex.music.model.Artist;
import com.zuzex.music.usecase.artist.ArtistServiceImpl;
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
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class FindArtistImplTest {

    @Mock
    private ArtistStorageService artistStorageService;

    @InjectMocks
    private ArtistServiceImpl artistService;

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
    void getById() {
        when(artistStorageService.getById(1L)).thenReturn(Mono.just(artist));
        Mono<Artist> byId = artistService.getById(1L);
        StepVerifier
                .create(byId)
                .consumeNextWith(newArtist -> {
                    assertNotNull(newArtist);
                    assertEquals(newArtist.getId(), artist.getId());
                    assertEquals(newArtist.getName(), artist.getName());
                    assertEquals(newArtist.getMbid(), artist.getMbid());
                })
                .verifyComplete();


    }

    @Test
    void getArtistByName() {
        when(artistStorageService.getByName("Papa Rome")).thenReturn(Mono.just(artist));
        Mono<Artist> papa_rome = artistService.getArtistByName("Papa Rome");
        StepVerifier
                .create(papa_rome)
                .consumeNextWith(newArtist -> {
                    assertNotNull(newArtist);
                    assertEquals(newArtist.getId(), artist.getId());
                    assertEquals(newArtist.getName(), artist.getName());
                    assertEquals(newArtist.getMbid(), artist.getMbid());
                })
                .verifyComplete();
    }
}