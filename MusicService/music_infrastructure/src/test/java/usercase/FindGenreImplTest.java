package usercase;

import com.zuzex.music.model.Genre;
import com.zuzex.music.usecase.genre.GenreServiceImpl;
import com.zuzex.music.usecase.genre.port.GenreStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class FindGenreImplTest {

    @Mock
    private GenreStorageService genreStorageService;

    @InjectMocks
    private GenreServiceImpl genreService;

    private Genre genre;

    @BeforeEach
    void init() {
        genre = Genre.builder()
                .id(1L)
                .name("Pop")
                .build();
    }

    @Test
    void getById() {
        when(genreStorageService.getById(anyLong())).thenReturn(Mono.just(genre));
        Mono<Genre> byId = genreService.getById(1L);
        StepVerifier.create(byId)
                .consumeNextWith(newGenre -> {
                    assertNotNull(newGenre);
                    assertEquals(newGenre.getId(), genre.getId());
                    assertEquals(newGenre.getName(), genre.getName());
                })
                .verifyComplete();
    }

    @Test
    void getByName() {
        when(genreStorageService.getByName(anyString())).thenReturn(Mono.just(genre));
        Mono<Genre> pop = genreService.getByName("pop");
        StepVerifier.create(pop)
                .consumeNextWith(newGenre -> {
                    assertNotNull(newGenre);
                    assertEquals(newGenre.getId(), genre.getId());
                    assertEquals(newGenre.getName(), genre.getName());
                })
                .verifyComplete();
    }

    @Test
    void findAllByTrackId() {
        when(genreStorageService.findAllByTrackId(anyLong())).thenReturn(Flux.just(genre));
        Flux<Genre> allByTrackId = genreService.findAllByTrackId(1L);
        StepVerifier.create(allByTrackId)
                .consumeNextWith(newGenre -> {
                    assertNotNull(newGenre);
                    assertEquals(newGenre.getId(), genre.getId());
                    assertEquals(newGenre.getName(), genre.getName());
                })
                .verifyComplete();
    }
}