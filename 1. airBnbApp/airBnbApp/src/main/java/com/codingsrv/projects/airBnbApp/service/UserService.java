package com.codingsrv.projects.airBnbApp.service;

import com.codingsrv.projects.airBnbApp.dto.ProfileUpdateRequestDto;
import com.codingsrv.projects.airBnbApp.dto.UserDto;
import com.codingsrv.projects.airBnbApp.entity.User;
import org.jspecify.annotations.Nullable;

public interface UserService {
    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();
}
