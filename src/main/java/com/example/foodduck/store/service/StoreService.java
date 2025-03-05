package com.example.foodduck.store.service;

import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.menu.repository.MenuRepository;
import com.example.foodduck.store.dto.request.StoreSaveRequestDto;
import com.example.foodduck.store.dto.response.NoticeUpdateResponseDto;
import com.example.foodduck.store.dto.response.StoreResponseDto;
import com.example.foodduck.store.dto.response.StoreWithMenusResponseDto;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.store.repository.StoreRepository;
import com.example.foodduck.user.entity.User;
import com.example.foodduck.user.entity.UserRole;
import com.example.foodduck.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    // Create
    @Transactional
    public StoreResponseDto save(Long userId, StoreSaveRequestDto dto) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        if (owner.getRole() != UserRole.OWNER) {
            throw new IllegalArgumentException("가게는 OWNER 권한을 가진 사용자만 생성할 수 있습니다.");
        }

        Store store = new Store(
                owner,
                dto.getName(),
                dto.getMinOrderPrice(),
                dto.getOpenTime(),
                dto.getCloseTime(),
                dto.getBreakState()
        );
        storeRepository.save(store);

        return new StoreResponseDto(store);
    }

    // Read
    @Transactional(readOnly = true)
    public StoreWithMenusResponseDto findById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));
        List<Menu> menus = menuRepository.findByStore(store);

        return new StoreWithMenusResponseDto(store, menus);
    }

    @Transactional(readOnly = true)
    public List<StoreResponseDto> findAll() {
        return storeRepository.findAll()
                .stream()
                .map(StoreResponseDto::new)
                .collect(Collectors.toList());
    }

    // Update
    @Transactional
    public StoreResponseDto update(Long storeId, Long userId, StoreSaveRequestDto dto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        if (!store.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("가게 주인만 가게 정보를 수정할 수 있습니다.");
        }

        store.update(dto.getName(), dto.getMinOrderPrice(), dto.getOpenTime(), dto.getCloseTime(), dto.getBreakState());

        return new StoreResponseDto(store);
    }

    @Transactional
    public NoticeUpdateResponseDto updateNotice(Long storeId, Long userId, String noticeContent) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        if (!store.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("가게 주인만 공지사항을 수정할 수 있습니다.");
        }

        store.updateNoticeContent(noticeContent);

        return new NoticeUpdateResponseDto(store.getId(), store.getNoticeContent());
    }


    // Delete
    @Transactional
    public void delete(Long storeId, Long userId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        if (!store.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("가게 주인만 삭제할 수 있습니다.");
        }

        storeRepository.delete(store);
    }
}