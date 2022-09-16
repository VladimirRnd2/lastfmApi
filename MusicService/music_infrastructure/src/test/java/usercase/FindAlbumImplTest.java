package usercase;

import com.zuzex.music.model.Album;
import com.zuzex.music.model.Artist;
import com.zuzex.music.usecase.album.FindAlbumImpl;
import com.zuzex.music.usecase.album.port.AlbumRepositoryService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class FindAlbumImplTest {

    @Mock
    private AlbumRepositoryService albumRepositoryService;

    @InjectMocks
    private FindAlbumImpl findAlbum;

    private Album album;

    @BeforeEach
    void init() {
        album = Album.builder()
                .id(1L)
                .name("Album")
                .mbid("Album")
                .artist(Artist.builder().id(1L).name("Artist").mbid("Artist").build())
                .build();
    }

    @Test
    void getById() {
        when(albumRepositoryService.getById(1L)).thenReturn(Mono.just(album));
        Mono<Album> byId = findAlbum.getById(1L);
        StepVerifier.create(byId)
                .consumeNextWith(newAlbum -> {
                    assertNotNull(newAlbum);
                    assertEquals(newAlbum.getId(), album.getId());
                    assertEquals(newAlbum.getName(), album.getName());
                    assertEquals(newAlbum.getMbid(), album.getMbid());
                    assertEquals(newAlbum.getArtist().getId(), album.getArtist().getId());
                    assertEquals(newAlbum.getArtist().getName(), album.getArtist().getName());
                    assertEquals(newAlbum.getArtist().getMbid(), album.getArtist().getMbid());
                }).verifyComplete();
    }

    @Test
    void getByName() {
        when(albumRepositoryService.getByName(anyString())).thenReturn(Mono.just(album));
        Mono<Album> lalala = findAlbum.getByName("lalala");
        StepVerifier.create(lalala)
                .consumeNextWith(newAlbum -> {
                    assertNotNull(newAlbum);
                    assertEquals(newAlbum.getId(), album.getId());
                    assertEquals(newAlbum.getName(), album.getName());
                    assertEquals(newAlbum.getMbid(), album.getMbid());
                    assertEquals(newAlbum.getArtist().getId(), album.getArtist().getId());
                    assertEquals(newAlbum.getArtist().getName(), album.getArtist().getName());
                    assertEquals(newAlbum.getArtist().getMbid(), album.getArtist().getMbid());
                }).verifyComplete();
    }
}