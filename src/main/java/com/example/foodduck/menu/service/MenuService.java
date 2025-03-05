package com.example.foodduck.menu.service;

import com.example.foodduck.exception.InvalidCredentialException;
import com.example.foodduck.menu.dto.request.MenuCreateRequest;
import com.example.foodduck.menu.dto.request.MenuUpdateRequest;
import com.example.foodduck.menu.dto.response.MenuCreateResponse;
import com.example.foodduck.menu.dto.response.MenuResponse;
import com.example.foodduck.menu.dto.response.MenuUpdateResponse;
import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.menu.repository.MenuRepository;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.store.repository.StoreRepository;
import com.example.foodduck.user.entity.User;
import com.example.foodduck.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.List;

import static com.example.foodduck.menu.entity.MenuState.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public MenuCreateResponse createMenu(Long storeId, MenuCreateRequest menuCreateRequest) {

        User findUser = getAuthenticatedUser();

        //가게를 찾을 수 없는 경우
        Store findStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        //본인 가게가 아닌 경우
        if (!findUser.getId().equals(findStore.getOwner().getId())) {
            throw new InvalidCredentialException("본인 가게에만 메뉴 등록이 가능합니다.");
        }

        if (menuRepository.existsByMenuName(menuCreateRequest.getMenuName())) {
            throw new IllegalArgumentException("이미 존재하는 메뉴입니다.");
        }

        Menu menu = new Menu(menuCreateRequest.getMenuName(), menuCreateRequest.getPrice(), menuCreateRequest.getCategory(), new Store(storeId));

        menuRepository.save(menu);
        return MenuCreateResponse.toDto(menu);
    }

    //TODO: 정렬 기준 추가하기 (리뷰 순, 주문 순)
    @Transactional(readOnly = true)
    public Page<MenuResponse> getMenus(Long storeId, int page, int size, String menuName, String category, String sortCondition) {

        int adjustPage = page <= 0 ? 1 : page - 1;

        Pageable pageable = PageRequest.of(adjustPage, size, Sort.by(sortCondition).descending());
        Page<Menu> pageMenus = menuRepository.findAllByStoreId(storeId, menuName, category, pageable);

        List<MenuResponse> menuList = pageMenus.getContent().stream()
                .map(MenuResponse::toDto)
                .toList();

        return new PageImpl<>(menuList, pageable,pageMenus.getTotalElements());
    }

    @Transactional(readOnly = true)
    public MenuResponse getMenu(Long menuId) {

        Menu findMenu = findMenuOrElseThrow(menuId);

        return MenuResponse.toDto(findMenu);
    }

    @Transactional
    public MenuUpdateResponse updateMenu(Long menuId, MenuUpdateRequest menuUpdateRequest) {

        User findUser = getAuthenticatedUser();

        Menu findMenu = findMenuOrElseThrow(menuId);

        if (!findUser.getId().equals(findMenu.getStore().getOwner().getId())) {
            throw new InvalidCredentialException("본인 가게의 메뉴만 수정 가능합니다.");
        }

        /**
         * (1): menuName이 입력된 경우
         * (2): price가 입력된 경우
         * (3): category가 입력된 경우
         * (4): menuState가 입력되고 해당 값이 SOLD_OUT인 경우
         * (5): menuState가 입력되고 해당 값이 ON_SALE인 경우
         */
        if (StringUtils.hasText(menuUpdateRequest.getMenuName())) {
            findMenu.updateMenuName(menuUpdateRequest.getMenuName()); // (1)
        }
        if (findMenu.getPrice() != menuUpdateRequest.getPrice()) {
            findMenu.updatePrice(menuUpdateRequest.getPrice()); // (2)
        }
        if (StringUtils.hasText(menuUpdateRequest.getCategory())) {
            findMenu.updateMenuCategory(menuUpdateRequest.getCategory()); //(3)
        }
        if (!findMenu.getMenuState().equals(menuUpdateRequest.getMenuState())) {
            if (SOLD_OUT.equals(menuUpdateRequest.getMenuState())) {
                findMenu.updateMenuStatus(SOLD_OUT); // (4)
            }
            if (ON_SALE.equals(menuUpdateRequest.getMenuState())) {
                findMenu.updateMenuStatus(ON_SALE); // (5)
            }
        }

        return MenuUpdateResponse.toDto(findMenu);
    }

    @Transactional
    public void deleteMenu(Long menuId) {

        User findUser = getAuthenticatedUser();

        Menu findMenu = findMenuOrElseThrow(menuId);

        if (!findUser.getId().equals(findMenu.getStore().getOwner().getId())) {
            throw new InvalidCredentialException("본인 가게의 메뉴만 삭제 가능합니다.");
        }

        findMenu.deleteMenu();
    }

    private Menu findMenuOrElseThrow(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));
    }

    private User getAuthenticatedUser() {
        //로그인한 유저 정보 추출
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String email = userDetails.getUsername();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}
