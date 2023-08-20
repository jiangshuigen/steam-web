package com.example.demo.service;

import com.example.demo.dto.UserDto;

public interface RedPackageService {

    String snatchPackage(UserDto dto, int redId) throws Exception;
}
