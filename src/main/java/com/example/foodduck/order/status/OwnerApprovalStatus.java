package com.example.foodduck.order.status;

import org.apache.coyote.BadRequestException;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * 사장님 승인값 저장 enum
 * @author 이호수
 * @version 승인 상태값 구현, 요청과 일치하는 상태값 리턴 메소드 구현
 * 일치하지 않는 상태값일 경우 예외처리
 */
public enum OwnerApprovalStatus {
    // 수락
    ACCEPT,
    // 거절
    REJECT;
    // 입력받은 string 과 일치하는 enum 값 리턴
    public static OwnerApprovalStatus of(String ownerApprovalStatus) throws BadRequestException {
        return Arrays.stream(OwnerApprovalStatus.values())
                .filter(o -> String.valueOf(o).equalsIgnoreCase(ownerApprovalStatus))
                .findFirst()
                .orElseThrow(()
                        -> new BadRequestException(MessageFormat
                        .format("Invalid Owner Approval Status: {0}", ownerApprovalStatus)));
    }
}
