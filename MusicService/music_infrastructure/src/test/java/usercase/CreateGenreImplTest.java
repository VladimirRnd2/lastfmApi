package usercase;

import com.zuzex.music.model.Genre;
import com.zuzex.music.usecase.genre.GenreService;
import com.zuzex.music.usecase.genre.port.GenreStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class CreateGenreImplTest {

    @Mock
    private GenreStorageService genreStorageService;

    @InjectMocks
    private GenreService genreService;

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
        Mockito.when(genreStorageService.create(any(Genre.class))).thenReturn(Mono.just(genre));
        Mono<Genre> genreMono = genreService.createGenre(Genre.builder().build());
        StepVerifier.create(genreMono)
                .consumeNextWith(newGenre -> {
                    assertNotNull(newGenre);
                    assertEquals(newGenre.getId(),genre.getId());
                    assertEquals(newGenre.getName(),genre.getName());
                })
                .verifyComplete();
    }
}