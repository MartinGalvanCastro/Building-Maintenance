package com.martin.buildingmaintenance.application.port.in;

import com.martin.buildingmaintenance.application.dto.UserInfoDto;

public interface UserInfoService {
    UserInfoDto getCurrentUserInfo(String authHeader);
}
