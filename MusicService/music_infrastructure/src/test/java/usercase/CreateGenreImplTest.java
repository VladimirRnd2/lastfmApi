package usercase;

import com.zuzex.music.model.Genre;
import com.zuzex.music.usecase.genre.CreateGenreImpl;
import com.zuzex.music.usecase.genre.port.GenreRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class CreateGenreImplTest {

    @Mock
    private GenreRepositoryService genreRepositoryService;

    @InjectMocks
    private CreateGenreImpl createGenre;

    private Genre genre;

    @BeforeEach
    void init() {
        genre = Genre.builder()
                .id(1L)
                .name("Pop")
                .build();
    }

    @Test
    void createGenre() {
        Mockito.when(genreRepositoryService.create(any(Genre.class))).thenReturn(Mono.just(genre));
        Mono<Genre> genreMono = createGenre.createGenre(Genre.builder().build());
        StepVerifier.create(genreMono)
                .consumeNextWith(newGenre -> {
                    assertNotNull(newGenre);
                    assertEquals(newGenre.getId(),genre.getId());
                    assertEquals(newGenre.getName(),genre.getName());
                })
                .verifyComplete();
    }
}