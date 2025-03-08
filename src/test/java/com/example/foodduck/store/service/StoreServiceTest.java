package com.example.foodduck.store.service;

import com.example.foodduck.menu.dto.response.MenuResponse;
import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.menu.repository.MenuRepository;
import com.example.foodduck.store.dto.request.StoreSaveRequestDto;
import com.example.foodduck.store.dto.request.StoreUpdateRequestDto;
import com.example.foodduck.store.dto.response.*;
import com.example.foodduck.store.entity.BreakState;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.store.entity.StoreState;
import com.example.foodduck.store.repository.StoreRepository;
import com.example.foodduck.user.entity.User;
import com.example.foodduck.user.entity.UserRole;
import com.example.foodduck.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private StoreService storeService;

    //  StoreSave 메서드
    @Test
    void Owner가_가게를_생성할_수_있다() {
        //  given
        long userId = 1L;
        User user = new User("OwnerTest", "test@test.com", "test123!@#", UserRole.OWNER);
        ReflectionTestUtils.setField(user, "id", userId);

        StoreSaveRequestDto requestDto = new StoreSaveRequestDto("테스트가게", 10000, LocalTime.of(10, 0), LocalTime.of(20, 0), BreakState.ACTIVE);
        Store store = new Store(user, requestDto.getName(), requestDto.getMinOrderPrice(), requestDto.getOpenTime(), requestDto.getCloseTime(), requestDto.getBreakState());

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(storeRepository.findByOwnerAndStoreState(any(User.class), any(StoreState.class))).willReturn(List.of());
        given(storeRepository.save(any(Store.class))).willReturn(store);

        //  when
        StoreSaveResponseDto responseDto = storeService.save(userId, requestDto);

        //  then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getId()).isEqualTo(store.getId());
        assertThat(responseDto.getOwner().getId()).isEqualTo(store.getOwner().getId());
    }

    @Test
    void User는_가게를_생성할_수_없다(){
        //  given
        long userId = 1L;
        User user = new User("testUser", "test@test.com", "test123!@#", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        StoreSaveRequestDto requestDto = new StoreSaveRequestDto("testStore", 200000, LocalTime.of(10, 0), LocalTime.of(20, 0), BreakState.ACTIVE);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        //  when & then
        assertThrows(IllegalArgumentException.class,
                () -> storeService.save(userId, requestDto),
                "가게는 OWNER 권한을 가진 사용자만 생성할 수 있습니다."
        );
    }

    @Test
    void Owner의_가게는_3개를_넘을_수_없다() {
        //  given
        long userId = 1L;
        User owner = new User("Ownertest", "test@test.com", "test123!@3", UserRole.OWNER);
        ReflectionTestUtils.setField(owner, "id", userId);

        StoreSaveRequestDto requestDto = new StoreSaveRequestDto("테스트가게", 10000, LocalTime.of(10, 0), LocalTime.of(20, 0), BreakState.ACTIVE);
        List<Store> activeStores = List.of(
                new Store(owner, requestDto.getName(), requestDto.getMinOrderPrice(), requestDto.getOpenTime(), requestDto.getCloseTime(), requestDto.getBreakState()),
                new Store(owner, requestDto.getName(), requestDto.getMinOrderPrice(), requestDto.getOpenTime(), requestDto.getCloseTime(), requestDto.getBreakState()),
                new Store(owner, requestDto.getName(), requestDto.getMinOrderPrice(), requestDto.getOpenTime(), requestDto.getCloseTime(), requestDto.getBreakState())
        );

        given(userRepository.findById(anyLong())).willReturn(Optional.of(owner));
        given(storeRepository.findByOwnerAndStoreState(any(User.class), any(StoreState.class))).willReturn(activeStores);

        //  when & then
        assertThrows(IllegalArgumentException.class,
                () -> storeService.save(userId, requestDto),
                "사장님은 ACTIVE 상태인 가게를 최대 3개까지만 운영할 수 있습니다."
        );
    }

    //  Read 메서드
    @Test
    void 다건조회() {
        //  given
        String name = "test";
        User owner = new User("OwnerTest", "test@test.com", "test123!@#", UserRole.OWNER);
        ReflectionTestUtils.setField(owner, "id", 1L);

        Store store1 = new Store(owner, "test1", 10000, LocalTime.of(10, 0), LocalTime.of(20, 0), BreakState.ACTIVE);
        Store store2 = new Store(owner, "test2", 10000, LocalTime.of(10, 0), LocalTime.of(20, 0), BreakState.ACTIVE);
        Store store3 = new Store(owner, "test3", 10000, LocalTime.of(10, 0), LocalTime.of(20, 0), BreakState.ACTIVE);
        ReflectionTestUtils.setField(store1, "id", 1L);
        ReflectionTestUtils.setField(store2, "id", 2L);
        ReflectionTestUtils.setField(store3, "id", 3L);

        List<Store> stores = List.of(store1, store2, store3);

        given(storeRepository.findByNameContaining(name)).willReturn(stores);

        //  when
        List<StoreSimpleResponseDto> result = (List<StoreSimpleResponseDto>) storeService.searchStoresByName(name);

        //  then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getName()).isEqualTo(store1.getName());
        assertThat(result.get(1).getName()).isEqualTo(store2.getName());
        assertThat(result.get(2).getName()).isEqualTo(store3.getName());
    }

    @Test
    void 단건조회() {
        // given
        String name = "test";

        User user = new User("OwnerTest", "test@test.com", "test123!@#", UserRole.OWNER);
        ReflectionTestUtils.setField(user, "id", 1L);

        Store store = new Store(user, "테스트가게", 10000, LocalTime.of(10, 0), LocalTime.of(20, 0), BreakState.ACTIVE);
        ReflectionTestUtils.setField(store, "id", 1L);

        List<Store> storeList = Collections.singletonList(store);
        given(storeRepository.findByNameContaining(name)).willReturn(storeList);

        List<Menu> menus = Arrays.asList(
                new Menu("Menu1", 1000, "test", store),
                new Menu("Menu2", 1000, "test", store)
        );

        given(menuRepository.findByStore(store)).willReturn(menus);

        // when
        StoreDetailResponseDto responseDto = (StoreDetailResponseDto) storeService.searchStoresByName(name);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getId()).isEqualTo(store.getId());
        List<MenuResponse> responseMenus = responseDto.getMenus();
        assertThat(responseMenus.get(0).getMenuName()).isEqualTo("Menu1");
        assertThat(responseMenus.get(1).getMenuName()).isEqualTo("Menu2");
    }

    @Test
    void 매칭되는_가게가_없을때(){
        //  given
        String name = "test";
        given(storeRepository.findByNameContaining(name)).willReturn(List.of());

        //  when
        List<?> result = (List<?>) storeService.searchStoresByName(name);

        //  then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    // update 메서드
    @Test
    void 업데이트_성공(){
        //  given
        long ownerId = 1L;
        long storeId = 1L;
        User user = new User("OwnerTest", "test@test.com", "test123!@#", UserRole.OWNER);
        ReflectionTestUtils.setField(user, "id", ownerId);

        Store store = new Store(user, "테스트가게", 10000, LocalTime.of(19, 0), LocalTime.of(20, 0), BreakState.ACTIVE);
        ReflectionTestUtils.setField(store, "id", storeId);
        StoreUpdateRequestDto requestDto = new StoreUpdateRequestDto("updatedName", 20000, LocalTime.of(9,0), LocalTime.of(21,0), BreakState.INACTIVE, StoreState.INACTIVE);

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        //  when
        StoreResponseDto responseDto = storeService.update(storeId, ownerId, requestDto);

        //  then
        assertThat(store.getName()).isEqualTo("updatedName");
        assertThat(store.getMinOrderPrice()).isEqualTo(20000);
        assertThat(store.getOpenTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(store.getCloseTime()).isEqualTo(LocalTime.of(21, 0));
        assertThat(store.getBreakState()).isEqualTo(BreakState.INACTIVE);
        assertThat(store.getStoreState()).isEqualTo(StoreState.INACTIVE);

        assertThat(responseDto.getId()).isEqualTo(storeId);
        assertThat(responseDto.getName()).isEqualTo("updatedName");
        assertThat(responseDto.getMinOrderPrice()).isEqualTo(20000);
        assertThat(responseDto.getOpenTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(responseDto.getCloseTime()).isEqualTo(LocalTime.of(21, 0));
        assertThat(responseDto.getBreakState()).isEqualTo(BreakState.INACTIVE);
        assertThat(responseDto.getStoreState()).isEqualTo(StoreState.INACTIVE);
    }

    @Test
    void 업데이트_실패_가게없음(){
        //  given
        long ownerId = 1L;
        long storeId = 1L;
        StoreUpdateRequestDto requestDto = new StoreUpdateRequestDto("test", 300000, LocalTime.of(10,0), LocalTime.of(20,0), BreakState.INACTIVE, StoreState.INACTIVE);

        given(storeRepository.findById(storeId)).willReturn(Optional.empty());

        //  when & then
        assertThrows(IllegalArgumentException.class,
                () -> storeService.update(storeId, ownerId, requestDto),
                "해당 가게가 존재하지 않습니다."
        );
    }

    @Test
    void 업데이트_실패_가게주인아님(){
        //  given
        long ownerId = 1L;
        long storeId = 1L;
        User user = new User("OwnerTest", "test@test.com", "test123!@#", UserRole.OWNER);
        User owner = new User("OwnerTest", "test@test.com", "test123!@#", UserRole.OWNER);
        ReflectionTestUtils.setField(user, "id", 2L);
        ReflectionTestUtils.setField(owner, "id", ownerId);

        Store store = new Store(owner, "테스트가게", 10000, LocalTime.of(19, 0), LocalTime.of(20, 0), BreakState.ACTIVE);
        ReflectionTestUtils.setField(store, "id", storeId);
        StoreUpdateRequestDto requestDto = new StoreUpdateRequestDto("updatedName", 20000, LocalTime.of(9,0), LocalTime.of(21,0), BreakState.INACTIVE, StoreState.INACTIVE);

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        // when& then
        assertThrows(IllegalArgumentException.class,
                () -> storeService.update(storeId, 2L, requestDto),
                "가게 주인만 가게 정보를 수정할 수 있습니다."
        );
    }

    // noticeUpdate 메서드
    @Test
    void 공지_업데이트_성공(){
        // given
        Long storeId = 1L;
        long ownerId = 1L;
        String notice = "공지사항입니다. 졸려요";

        User owner = new User("Owner", "test@test.com", "test123!@#", UserRole.OWNER);
        ReflectionTestUtils.setField(owner, "id", ownerId);

        Store store = new Store(owner, "testStore", 100000, LocalTime.of(10,0), LocalTime.of(20,0),BreakState.ACTIVE);
        ReflectionTestUtils.setField(store, "id", storeId);

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        // when
        NoticeUpdateResponseDto responseDto = storeService.updateNotice(storeId, ownerId, notice);

        // then
        assertThat(store.getNoticeContent()).isEqualTo(notice);
        assertThat(responseDto.getStoreId()).isEqualTo(storeId);
        assertThat(responseDto.getNoticeContent()).isEqualTo(notice);
    }

    @Test
    void 공지_업데이트_실패_가게없음(){
        // given
        long storeId = 1L;
        long ownerId = 1L;
        String notice = "공지사항입니다. 졸려요";

        // storeRepository.findById()가 빈 Optional 반환
        given(storeRepository.findById(storeId)).willReturn(Optional.empty());

        // when& then
        assertThrows(IllegalArgumentException.class,
                () -> storeService.updateNotice(storeId, ownerId, notice),
                "해당 가게가 존재하지 않습니다."
        );
    }

    @Test
    void 공지_업데이트_실패_주인아님(){
        // given
        long storeId = 1L;
        long ownerId = 1L;
        long notOwnerId = 2L;
        String notice = "공지사항입니다. 졸려요";

        User owner = new User("OwnerTest", "test@test.com", "test123!@#", UserRole.OWNER);
        ReflectionTestUtils.setField(owner, "id", ownerId);

        Store store = new Store(owner, "테스트가게", 10000, LocalTime.of(10, 0), LocalTime.of(20, 0), BreakState.ACTIVE);
        ReflectionTestUtils.setField(store, "id", storeId);
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        //  when & then
        assertThrows(IllegalArgumentException.class,
                () -> storeService.updateNotice(storeId, notOwnerId, notice),
                "가게 주인만 삭제할 수 있습니다."
        );
    }
}