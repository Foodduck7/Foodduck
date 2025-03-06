package com.example.foodduck.shoppingcart.service;

import com.example.foodduck.exception.custom.StoreMismatchException;
import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.menu.repository.MenuRepository;
import com.example.foodduck.shoppingcart.dto.request.ShoppingCartAddRequest;
import com.example.foodduck.shoppingcart.dto.request.ShoppingCartCreateRequest;
import com.example.foodduck.shoppingcart.dto.response.ShoppingCartGetResponse;
import com.example.foodduck.shoppingcart.dto.response.ShoppingCartResponse;
import com.example.foodduck.shoppingcart.entity.ShoppingCart;
import com.example.foodduck.shoppingcart.entity.ShoppingCartMenu;
import com.example.foodduck.shoppingcart.repository.ShoppingCartRepository;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.store.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.valueOf;

@RequiredArgsConstructor
@Service
public class ShoppingCartService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    // 쇼핑카트 생성(메뉴 쇼핑카트에 추가)
    @Transactional
    public ShoppingCartResponse createShoppingCart(ShoppingCartCreateRequest shoppingCartCreateRequest) {
        Store foundStore = getStoreById(shoppingCartCreateRequest.getStoreId());
        Menu foundMenu = getMenuById(shoppingCartCreateRequest.getMenuId());
        ShoppingCart shoppingCart = new ShoppingCart(foundStore, LocalDateTime.now());
        shoppingCart.addMenu(foundMenu, shoppingCartCreateRequest.getQuantity());
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);
        return new ShoppingCartResponse(savedShoppingCart.getId(), savedShoppingCart.getStore().getId(), savedShoppingCart.getModifiedAt());
    }

    // 쇼핑카트 조회
    // FETCH JOIN 또는 @EntityGraph 사용해 ShoppingCart 조회시 Menu 도 함께 조회
    @Transactional
    public ShoppingCartGetResponse findShoppingCart(long id) {
        ShoppingCart foundShoppingCart = getShoppingCartById(id);
        // 마지막 수정 이후로 1일 초과에 대한 예외처리
        markShoppingCartAsDeleted(foundShoppingCart);
        List<ShoppingCartMenu> cartMenus = foundShoppingCart.getShoppingCartMenus();
        //메뉴 총 가격
        BigDecimal totalAmount = cartMenus.stream()
                .filter(cartMenu -> cartMenu.getMenu() != null)
                .map(cartMenu -> BigDecimal.valueOf(cartMenu.getMenu().getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 배송비
        BigDecimal deliveryFee = foundShoppingCart.getDeliveryFee();
        return new ShoppingCartGetResponse(foundShoppingCart, totalAmount, deliveryFee);
    }

    // 쇼핑카트 메뉴 업데이트 - 추가
    @Transactional
    public ShoppingCartResponse addMenuToShoppingCart(long id, ShoppingCartAddRequest shoppingCartAddRequest) {
        ShoppingCart foundShoppingCart = getShoppingCartById(id);
        Menu foundMenu = getMenuById(shoppingCartAddRequest.getMenuId());
        // 쇼핑카트와 메뉴의 가게 일치하지 않는 경우 예외처리
        validateStoreMatch(foundShoppingCart,foundMenu);
        foundShoppingCart.addMenu(foundMenu, shoppingCartAddRequest.getQuantity());
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(foundShoppingCart);
        return new ShoppingCartResponse(savedShoppingCart.getId(), savedShoppingCart.getStore().getId(), savedShoppingCart.getModifiedAt());
    }

    // 쇼핑카트 메뉴 삭제
    @Transactional
    public ShoppingCartResponse removeMenuToShoppingCart(long id, long menuId) {
        ShoppingCart foundShoppingCart = getShoppingCartById(id);
        Menu foundMenu = getMenuById(menuId);
        // 쇼핑카트와 메뉴의 가게 일치하지 않는 경우 예외처리
        validateStoreMatch(foundShoppingCart, foundMenu);
        // 쇼핑카트 내 메뉴 0개 -> 쇼핑카트 삭제
        if (foundShoppingCart.getShoppingCartMenus().isEmpty()) {
            foundShoppingCart.delete();
            return new ShoppingCartResponse(foundShoppingCart.getId(), foundShoppingCart.getStore().getId(), foundShoppingCart.getModifiedAt());
        }
        foundShoppingCart.removeMenu(foundMenu);
        return new ShoppingCartResponse(foundShoppingCart.getId(), foundShoppingCart.getStore().getId(), foundShoppingCart.getModifiedAt());
    }

    // 가게 조회 후 리턴 메서드
    private Store getStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store Not Found"));
    }

    // 메뉴 조회 후 리턴 메서드
    private Menu getMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("Menu Not Found"));
    }

    // 장바구니 조회 후 리턴 메서드
    private ShoppingCart getShoppingCartById(Long cartId) {
        return shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("ShoppingCart Not Found"));
    }

    // 장바구니와 메뉴의 가게 값 일치여부 리턴 메서드
    private void validateStoreMatch(ShoppingCart shoppingCart, Menu menu) {
        if (!shoppingCart.getStore().getId().equals(menu.getStore().getId())) {
            throw new StoreMismatchException("Menu and Shopping Cart Store Do Not Match");
        }
    }

    @Transactional
    public void markShoppingCartAsDeleted(ShoppingCart shoppingCart) {
        shoppingCart.delete();
        shoppingCartRepository.save(shoppingCart);
    }

}
