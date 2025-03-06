package com.example.foodduck.menu.service;

import com.example.foodduck.exception.custom.InvalidCredentialException;
import com.example.foodduck.menu.dto.request.MenuOptionCreateRequest;
import com.example.foodduck.menu.dto.request.MenuOptionUpdateRequest;
import com.example.foodduck.menu.dto.response.MenuOptionCreateResponse;
import com.example.foodduck.menu.dto.response.MenuOptionUpdateResponse;
import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.menu.entity.MenuOption;
import com.example.foodduck.menu.entity.OptionStatus;
import com.example.foodduck.menu.repository.MenuOptionRepository;
import com.example.foodduck.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class MenuOptionService {

    private final MenuOptionRepository menuOptionRepository;
    private final MenuService menuService;

    @Transactional
    public MenuOptionCreateResponse createMenuOption (Long menuId, MenuOptionCreateRequest menuOptionCreateRequest) {
        User findUser = menuService.getAuthenticatedUser();

        Menu findMenu = menuService.findMenuOrElseThrow(menuId);

        //본인 가게의 메뉴에만 옵션 추가 가능
        if (!findUser.getId().equals(findMenu.getStore().getOwner().getId())) {
            throw new InvalidCredentialException("본인 가게의 메뉴에만 옵션을 추가할 수 있습니다.");
        }

        //이미 존재하는 옵션 내용인 경우
        if (menuOptionRepository.existsByContents(menuOptionCreateRequest.getContents())) {
            throw new IllegalArgumentException("이미 존재하는 옵션 내용 입니다.");
        }

        MenuOption savedMenuOption = menuOptionRepository.save(new MenuOption(
                menuOptionCreateRequest.getOptionName(),
                menuOptionCreateRequest.getContents(),
                menuOptionCreateRequest.getOptionPrice(),
                new Menu(menuId))
        );

        return  MenuOptionCreateResponse.toDto(savedMenuOption);
    }

    @Transactional
    public MenuOptionUpdateResponse updateMenuOption(Long optionId, MenuOptionUpdateRequest menuOptionUpdateRequest) {
        User findUser = menuService.getAuthenticatedUser();

        MenuOption findMenuOption = menuOptionRepository.findById(optionId)
                        .orElseThrow(() -> new IllegalArgumentException("메뉴 옵션을 찾을 수 없습니다."));

        Menu findMenu = menuService.findMenuOrElseThrow(findMenuOption.getMenu().getId());

        /**
         * (1): option이 입력되지 않은 경우
         * (2): contents가 입력되지 않은 경우
         * (3): optionPrice가 입력되었고 변경된 경우
         * (4): optionStatus가 입력되고 해당 값이 SOLD_OUT인 경우
         * (5): optionStatus가 입력되고 해당 값이 ON_SALE인 경우
         */
        if (!StringUtils.hasText(menuOptionUpdateRequest.getOption())) {
            findMenuOption.updateMenuOption(menuOptionUpdateRequest.getOption()); // (1)
        }
        if (!StringUtils.hasText(menuOptionUpdateRequest.getContents())) {
            findMenuOption.updateMenuOptionContents(menuOptionUpdateRequest.getContents()); // (2)
        }
        if (findMenuOption.getOptionPrice() != menuOptionUpdateRequest.getOptionPrice()) {
            findMenuOption.updateMenuOptionPrice(menuOptionUpdateRequest.getOptionPrice()); // (3)
        }
        if (!findMenuOption.getOptionStatus().equals(menuOptionUpdateRequest.getOptionStatus())) {
            if (OptionStatus.SOLD_OUT.equals(menuOptionUpdateRequest.getOptionStatus())) {
                findMenuOption.updateOptionStatus(OptionStatus.SOLD_OUT); // (4)
            }
            if (OptionStatus.ON_SALE.equals(menuOptionUpdateRequest.getOptionStatus())) {
                findMenuOption.updateOptionStatus(OptionStatus.ON_SALE); // (5)
            }
        }

        return MenuOptionUpdateResponse.toDto(findMenuOption);
    }

    @Transactional
    public void deleteMenuOption(Long optionId) {
        User findUser = menuService.getAuthenticatedUser();

        MenuOption findMenuOption = menuOptionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴 옵션을 찾을 수 없습니다."));

        Menu findMenu = menuService.findMenuOrElseThrow(findMenuOption.getMenu().getId());

        //본인 가게의 메뉴 옵션만 수정 가능
        if (!findUser.getId().equals(findMenu.getStore().getOwner().getId())) {
            throw new InvalidCredentialException("본인 가게의 메뉴 옵션만 수정할 수 있습니다.");
        }

        findMenuOption.deleteMenuOption();
    }
}
