package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class UUResDataDto {
    private List<UUAwardDto> saleTemplateByCategoryResponseList;
    private int currentPage;
    private boolean newPageIsHaveContent;
}
