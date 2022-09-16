package usercase;

import com.zuzex.music.model.Genre;
import com.zuzex.music.usecase.genre.FindGenreImpl;
import com.zuzex.music.usecase.genre.port.GenreRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class FindGenreImplTest {

    @Mock
    private GenreRepositoryService genreRepositoryService;

    @InjectMocks
    private FindGenreImpl findGenre;

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
        when(genreRepositoryService.getById(anyLong())).thenReturn(Mono.just(genre));
        Mono<Genre> byId = findGenre.getById(1L);
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
        when(genreRepositoryService.getByName(anyString())).thenReturn(Mono.just(genre));
        Mono<Genre> pop = findGenre.getByName("pop");
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
        when(genreRepositoryService.findAllByTrackId(anyLong())).thenReturn(Flux.just(genre));
        Flux<Genre> allByTrackId = findGenre.findAllByTrackId(1L);
        StepVerifier.create(allByTrackId)
                .consumeNextWith(newGenre -> {
                    assertNotNull(newGenre);
                    assertEquals(newGenre.getId(), genre.getId());
                    assertEquals(newGenre.getName(), genre.getName());
                })
                .verifyComplete();
    }
}