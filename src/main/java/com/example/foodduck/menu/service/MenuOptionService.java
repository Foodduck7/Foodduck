package com.example.foodduck.menu.service;

import com.example.foodduck.exception.InvalidCredentialException;
import com.example.foodduck.menu.dto.request.MenuOptionCreateRequest;
import com.example.foodduck.menu.dto.response.MenuOptionCreateResponse;
import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.menu.entity.MenuOption;
import com.example.foodduck.menu.repository.MenuOptionRepository;
import com.example.foodduck.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                menuOptionCreateRequest.getPrice(),
                new Menu(menuId))
        );

        return  MenuOptionCreateResponse.toDto(savedMenuOption);
    }
}
