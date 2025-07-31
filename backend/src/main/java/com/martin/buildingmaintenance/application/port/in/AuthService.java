package com.martin.buildingmaintenance.application.port.in;

import com.martin.buildingmaintenance.application.dto.CredentialsDto;
import com.martin.buildingmaintenance.application.dto.LogInResultDto;
import com.martin.buildingmaintenance.application.dto.LogOutResponseDto;

public interface AuthService {
    LogInResultDto authenticate(CredentialsDto credentialsDto);
    LogOutResponseDto logout(String token);
}
