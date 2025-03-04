package com.example.foodduck.menu.service;

import com.example.foodduck.common.config.jwt.JwtUtil;
import com.example.foodduck.exception.InvalidCredentialException;
import com.example.foodduck.menu.dto.request.MenuCreateRequest;
import com.example.foodduck.menu.dto.request.MenuUpdateRequest;
import com.example.foodduck.menu.dto.response.MenuCreateResponse;
import com.example.foodduck.menu.dto.response.MenuUpdateResponse;
import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.menu.repository.MenuRepository;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.store.repository.StoreRepository;
import com.example.foodduck.user.entity.User;
import com.example.foodduck.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.example.foodduck.menu.entity.MenuState.*;
import static com.example.foodduck.user.entity.UserRole.USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public MenuCreateResponse createMenu(HttpServletRequest servletRequest, Long storeId, MenuCreateRequest menuCreateRequest) {

        //Http 요청에서 토큰 빼오기
        String token = jwtUtil.parseJwt(servletRequest);
        if (token == null) {
            throw new InvalidCredentialException("jwt 토큰이 존재하지 않습니다.");
        }

        //유저 아이디 추출
        Long userIdFromJwtToken = jwtUtil.getUserIdFromJwtToken(token);

        User findUser = userRepository.findById(userIdFromJwtToken)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        //가게를 찾을 수 없는 경우
        Store findStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        //유저 권한 확인
        if (findUser.getRole() == USER) {
            throw new InvalidCredentialException("권한이 없습니다.");
        }

        //본인 가게가 아닌 경우
        if (!findUser.getId().equals(findStore.getOwner().getId())) {
             throw new InvalidCredentialException("본인 가게에만 메뉴 등록이 가능합니다.");
        }

        Menu menu = new Menu(menuCreateRequest.getMenuName(), menuCreateRequest.getPrice(), new Store(storeId));

        menuRepository.save(menu);
        return MenuCreateResponse.toDto(menu);
    }

    @Transactional
    public MenuUpdateResponse updateMenu(HttpServletRequest servletRequest, Long menuId, MenuUpdateRequest menuUpdateRequest) {
        //Http 요청에서 토큰 빼오기
        String token = jwtUtil.parseJwt(servletRequest);
        if (token == null) {
            throw new InvalidCredentialException("jwt 토큰이 존재하지 않습니다.");
        }

        //유저 아이디 추출
        Long userIdFromJwtToken = jwtUtil.getUserIdFromJwtToken(token);

        User findUser = userRepository.findById(userIdFromJwtToken)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        //유저 권한 확인
        if (findUser.getRole() == USER) {
            throw new InvalidCredentialException("권한이 없습니다.");
        }

        Menu findMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

        if (!findUser.getId().equals(findMenu.getStore().getOwner().getId())) {
            throw new InvalidCredentialException("본인 가게의 메뉴만 수정 가능합니다.");
        }

        if (StringUtils.hasText(menuUpdateRequest.getMenuName())) {
            findMenu.updateMenuName(menuUpdateRequest.getMenuName());
        }
        if (findMenu.getPrice() != menuUpdateRequest.getPrice()) {
            findMenu.updatePrice(menuUpdateRequest.getPrice());
        }
        if (!findMenu.getMenuState().toString().equals(menuUpdateRequest.getMenuState())) {
            if ("SOLD_OUT".equals(menuUpdateRequest.getMenuState())) {
                findMenu.updateMenuStatus(SOLD_OUT);
            }
            if ("ON_SALE".equals(menuUpdateRequest.getMenuState())) {
                findMenu.updateMenuStatus(ON_SALE);
            }
        }

        return MenuUpdateResponse.toDto(findMenu);
    }
}
