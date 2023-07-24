package com.example.demo.dto;

import com.example.demo.entity.BoxRecords;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BattleDto {
    private int UserId;
    private BigDecimal bean;
    private List<BoxRecords> listAward;
    private int win;
}
