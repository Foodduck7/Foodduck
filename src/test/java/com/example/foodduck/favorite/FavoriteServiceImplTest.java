package com.example.foodduck.favorite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.foodduck.favorite.dto.response.FavoriteResponseDto;
import com.example.foodduck.favorite.entity.Favorite;
import com.example.foodduck.favorite.repository.FavoriteRepository;
import com.example.foodduck.favorite.service.FavoriteServiceImpl;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.store.entity.BreakState;
import com.example.foodduck.store.repository.StoreRepository;
import com.example.foodduck.user.entity.User;
import com.example.foodduck.user.entity.UserRole;
import com.example.foodduck.user.repository.UserRepository;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @Test
    void 좋아요_추가_성공() {
        // given
        Long userId = 1L;
        Long storeId = 1L;

        User user = new User("UserTest", "user@test.com", "password", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        Store store = new Store(user, "Test Store", 10000, LocalTime.of(10, 0), LocalTime.of(20, 0), BreakState.ACTIVE);
        ReflectionTestUtils.setField(store, "id", storeId);
        store.setLikeCount(0);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(favoriteRepository.findByUserAndStore(user, store)).willReturn(Optional.empty());

        // when
        favoriteService.addFavorite(userId, storeId);

        // then
        assertThat(store.getLikeCount()).isEqualTo(1);
    }

    @Test
    void 좋아요_실패_사용자없음() {
        // given
        Long userId = 1L;
        Long storeId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> favoriteService.addFavorite(userId, storeId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void 좋아요_실패_가게없음() {
        // given
        Long userId = 1L;
        Long storeId = 1L;

        User user = new User("UserTest", "user@test.com", "password", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(storeRepository.findById(storeId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> favoriteService.addFavorite(userId, storeId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Store not found");
    }


    @Test
    void 좋아요_삭제_성공() {
        // given
        Long userId = 1L;
        Long storeId = 1L;

        User user = new User("UserTest", "user@test.com", "password", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        Store store = new Store(user, "Test Store", 10000, LocalTime.of(10, 0), LocalTime.of(20, 0), BreakState.ACTIVE);
        ReflectionTestUtils.setField(store, "id", storeId);
        store.setLikeCount(5);

        Favorite favorite = new Favorite(user, store);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(favoriteRepository.findByUserAndStore(user, store)).willReturn(Optional.of(favorite));

        // when
        favoriteService.removeFavorite(userId, storeId);

        // then
        verify(favoriteRepository).delete(favorite);
        // 좋아요 수가 5에서 4로 감소했는지 확인
        assertThat(store.getLikeCount()).isEqualTo(4);
    }

    @Test
    void 좋아요_삭제_실패_사람없음() {
        // given
        Long userId = 1L;
        Long storeId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> favoriteService.removeFavorite(userId, storeId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void 좋아요_삭제_실패_가게없음() {
        // given
        Long userId = 1L;
        Long storeId = 1L;

        User user = new User("UserTest", "user@test.com", "password", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(storeRepository.findById(storeId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> favoriteService.removeFavorite(userId, storeId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Store not found");
    }

    @Test
    void 좋아요_조회_성공() {
        // given
        Long userId = 1L;
        User user = new User("UserTest", "user@test.com", "password", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        Store store1 = new Store(user, "Store1", 10000, LocalTime.of(10, 0), LocalTime.of(20, 0), BreakState.ACTIVE);
        ReflectionTestUtils.setField(store1, "id", 1L);
        Store store2 = new Store(user, "Store2", 15000, LocalTime.of(11, 0), LocalTime.of(21, 0), BreakState.ACTIVE);
        ReflectionTestUtils.setField(store2, "id", 2L);

        Favorite favorite1 = new Favorite(user, store1);
        ReflectionTestUtils.setField(favorite1, "id", 101L);
        Favorite favorite2 = new Favorite(user, store2);
        ReflectionTestUtils.setField(favorite2, "id", 102L);

        List<Favorite> favorites = List.of(favorite1, favorite2);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(favoriteRepository.findAllByUser(user)).willReturn(favorites);

        // when
        List<FavoriteResponseDto> responseDtos = favoriteService.getFavoritesByUser(userId);

        // then
        assertThat(responseDtos).hasSize(2);
        FavoriteResponseDto dto1 = responseDtos.get(0);
        assertThat(dto1.getFavoriteId()).isEqualTo(101L);
        assertThat(dto1.getStoreId()).isEqualTo(1L);

        FavoriteResponseDto dto2 = responseDtos.get(1);
        assertThat(dto2.getFavoriteId()).isEqualTo(102L);
        assertThat(dto2.getStoreId()).isEqualTo(2L);
    }

    @Test
    void 좋아요_조회_실패_사용자없음() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> favoriteService.getFavoritesByUser(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }
}
