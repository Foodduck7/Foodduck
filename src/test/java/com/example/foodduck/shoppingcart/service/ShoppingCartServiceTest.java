import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.menu.repository.MenuRepository;
import com.example.foodduck.shoppingcart.dto.request.ShoppingCartAddRequest;
import com.example.foodduck.shoppingcart.dto.request.ShoppingCartCreateRequest;
import com.example.foodduck.shoppingcart.dto.response.ShoppingCartGetResponse;
import com.example.foodduck.shoppingcart.dto.response.ShoppingCartResponse;
import com.example.foodduck.shoppingcart.entity.ShoppingCart;
import com.example.foodduck.shoppingcart.repository.ShoppingCartRepository;
import com.example.foodduck.shoppingcart.service.ShoppingCartService;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.store.repository.StoreRepository;
import com.example.foodduck.user.entity.User;
import com.example.foodduck.user.entity.UserRole;
import com.example.foodduck.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    private User user;
    private Store store;
    private Menu menu;
    private ShoppingCart shoppingCart;

    @BeforeEach
    void setUp() {
        user = new User("name", "email", "pasword", UserRole.USER);
        store = new Store(1L);
        menu = new Menu(1L);
        shoppingCart = new ShoppingCart(user, store, LocalDateTime.now());
    }

    @Test
    void createShoppingCart_Success() {
        ShoppingCartCreateRequest request = new ShoppingCartCreateRequest(1L, 1L, 1, 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(shoppingCartRepository.save(any())).thenReturn(shoppingCart);

        ShoppingCartResponse response = shoppingCartService.createShoppingCart(request);

        assertNotNull(response);
        assertEquals(1L, response.storeId());
        verify(shoppingCartRepository, times(1)).save(any());
    }

    @Test
    void createShoppingCart_UserNotFound() {
        ShoppingCartCreateRequest request = new ShoppingCartCreateRequest(1L, 1L, 1, 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.createShoppingCart(request));
    }

    @Test
    void findShoppingCart_Success() {
        when(shoppingCartRepository.findByUserIdWithMenu(1L)).thenReturn(Optional.of(shoppingCart));
        ShoppingCartGetResponse response = shoppingCartService.findShoppingCart(1L);

        assertNotNull(response);
        assertEquals(BigDecimal.ZERO, response.getTotalAmount());
    }

    @Test
    void findShoppingCart_NotFound() {
        when(shoppingCartRepository.findByUserIdWithMenu(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.findShoppingCart(1L));
    }

    @Test
    void addMenuToShoppingCart_ShoppingCartNotFound() {
        ShoppingCartAddRequest request = new ShoppingCartAddRequest(1L, 2);
        when(shoppingCartRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.addMenuToShoppingCart(1L, request));
    }

    @Test
    void removeMenuToShoppingCart_ShoppingCartNotFound() {
        when(shoppingCartRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.removeMenuToShoppingCart(1L, 1L));
    }
}
