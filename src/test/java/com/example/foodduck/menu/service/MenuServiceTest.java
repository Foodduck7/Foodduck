package com.example.foodduck.menu.service;

import com.example.foodduck.exception.custom.InvalidCredentialException;
import com.example.foodduck.menu.dto.request.MenuCreateRequest;
import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.menu.repository.MenuRepository;
import com.example.foodduck.store.entity.BreakState;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.store.repository.StoreRepository;
import com.example.foodduck.user.entity.User;
import com.example.foodduck.user.entity.UserRole;
import com.example.foodduck.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MenuService menuService;

    // 공통 설정을 위한 필드
    private UserDetails userDetails;
    private UsernamePasswordAuthenticationToken authenticationToken;

    @BeforeEach
    void setUp() {
        // 로그인한 유저 설정
        userDetails = org.springframework.security.core.userdetails.User
                .builder()
                .username("1@1.com")
                .password("password")
                .roles("OWNER")
                .build();

        authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, "password", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Test
    void 로그인한_유저_조회_성공() {
        // given
        User expectedUser = new User("이름", "1@1.com", "password", UserRole.OWNER);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(expectedUser));

        // when
        User actualUser = menuService.getAuthenticatedUser();

        // then
        assertNotNull(actualUser);
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getName(), actualUser.getName());
    }

    @Test
    void 로그인한_유저_조회_실패() {
        // given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.getAuthenticatedUser());

        // then
        assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void 가게를_ID로_찾을_수_없다 () {
        //given
        User expectedUser = new User("이름", "1@1.com", "password", UserRole.OWNER);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(expectedUser));

        Long storeId = 1L;
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest();

        ReflectionTestUtils.setField(menuCreateRequest, "menuName", "테스트 이름");
        ReflectionTestUtils.setField(menuCreateRequest, "price", 3000);
        ReflectionTestUtils.setField(menuCreateRequest, "category", "테스트 카테고리");

        given(storeRepository.findById(storeId)).willReturn(Optional.empty());

        //when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.createMenu(storeId, menuCreateRequest));

        assertEquals("가게를 찾을 수 없습니다.",exception.getMessage());
    }

    @Test
    void 로그인한_유저의_ID와_가게에_등록된_유저가_다르다() {
        // given
        User expectedUser = new User("이름", "1@1.com", "password", UserRole.OWNER);
        User anotherUser = new User("다른이름", "2@2.com", "password", UserRole.OWNER);

        ReflectionTestUtils.setField(expectedUser, "id", 1L);
        ReflectionTestUtils.setField(anotherUser, "id", 2L);

        // 현재 인증된 사용자 mocking
        when(userRepository.findByEmail("1@1.com")).thenReturn(Optional.of(expectedUser)); // getAuthenticatedUser()가 호출할 이메일

        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", 1L);
        ReflectionTestUtils.setField(store, "owner", anotherUser); // 가게 주인은 anotherUser
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

        MenuCreateRequest menuCreateRequest = new MenuCreateRequest();
        ReflectionTestUtils.setField(menuCreateRequest, "menuName", "테스트 이름");
        ReflectionTestUtils.setField(menuCreateRequest, "price", 3000);
        ReflectionTestUtils.setField(menuCreateRequest, "category", "테스트 카테고리");

        // when & then
        InvalidCredentialException exception = assertThrows(InvalidCredentialException.class,
                () -> menuService.createMenu(store.getId(), menuCreateRequest));

        assertEquals("본인 가게에만 메뉴 등록이 가능합니다.", exception.getMessage());
    }

    @Test
    void 로그인한_유저의_ID와_가게에_등록된_유저가_같습니다() {
        // given
        User expectedUser = new User("이름", "1@1.com", "password", UserRole.OWNER);
        ReflectionTestUtils.setField(expectedUser, "id", 1L);

        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", 1L);
        ReflectionTestUtils.setField(store, "owner", expectedUser); // owner 필드에 User 객체 설정

        // then
        assertEquals(expectedUser, store.getOwner()); // owner가 동일한지 확인
        assertEquals(expectedUser.getId(), store.getOwner().getId()); // ID가 동일한지 확인
        assertEquals(expectedUser.getEmail(), store.getOwner().getEmail()); // 이메일 확인
        assertEquals(expectedUser.getName(), store.getOwner().getName()); // 이름 확인
    }

    @Test
    void 메뉴가_존재_따라서_등록_실패() {
        // given
        User expectedUser = new User("이름", "1@1.com", "password", UserRole.OWNER);
        ReflectionTestUtils.setField(expectedUser, "id", 1L); // id 설정 추가

        when(userRepository.findByEmail(expectedUser.getEmail())).thenReturn(Optional.of(expectedUser)); // 구체적인 이메일로 수정

        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", 1L);
        ReflectionTestUtils.setField(store, "owner", expectedUser);
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

        Long storeId = 1L;
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest();
        ReflectionTestUtils.setField(menuCreateRequest, "menuName", "테스트 이름");
        ReflectionTestUtils.setField(menuCreateRequest, "price", 3000);
        ReflectionTestUtils.setField(menuCreateRequest, "category", "테스트 카테고리");

        given(menuRepository.existsByMenuName(menuCreateRequest.getMenuName())).willReturn(true);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.createMenu(storeId, menuCreateRequest));

        assertEquals("이미 존재하는 메뉴입니다.", exception.getMessage());
    }

    @Test
    void 메뉴_등록_성공() {
        // given
        User expectedUser = new User("이름", "1@1.com", "password", UserRole.OWNER);
        ReflectionTestUtils.setField(expectedUser, "id", 1L); // id 설정 추가

        when(userRepository.findByEmail(expectedUser.getEmail())).thenReturn(Optional.of(expectedUser)); // 구체적인 이메일로 수정

        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", 1L);
        ReflectionTestUtils.setField(store, "owner", expectedUser);
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

        Long storeId = 1L;
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest();
        ReflectionTestUtils.setField(menuCreateRequest, "menuName", "테스트 이름");
        ReflectionTestUtils.setField(menuCreateRequest, "price", 3000);
        ReflectionTestUtils.setField(menuCreateRequest, "category", "테스트 카테고리");

        given(menuRepository.existsByMenuName(menuCreateRequest.getMenuName())).willReturn(false);

        // when & then
        menuService.createMenu(storeId,menuCreateRequest);

        ArgumentCaptor<Menu> menuCaptor = ArgumentCaptor.forClass(Menu.class);
        verify(menuRepository).save(menuCaptor.capture());

        Menu savedMenu = menuCaptor.getValue();
        assertEquals("테스트 이름", savedMenu.getMenuName());
        assertEquals(3000, savedMenu.getPrice());
        assertEquals("테스트 카테고리", savedMenu.getCategory());
    }

    @Test
    void 메뉴를_ID로_찾을_수_없다 () {
        //given
        Long menuId = 1L;
        given(menuRepository.findById(menuId)).willReturn(Optional.empty());

        //when
        assertThrows(IllegalArgumentException.class,
                () -> menuService.findMenuOrElseThrow(menuId),
                "메뉴를 찾을 수 없습니다."
        );
    }



}