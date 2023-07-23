package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.entity.BoxAwards;
import com.example.demo.entity.BoxRecords;
import com.example.demo.entity.GameArenaBox;
import com.example.demo.entity.GameArenaUsers;
import com.example.demo.mapper.GameBattleMapper;
import com.example.demo.mapper.LuckyBoxMapper;
import com.example.demo.service.GameBattleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class GameBattleServiceImpl implements GameBattleService {

    @Resource
    private GameBattleMapper gamebattlemapper;
    @Resource
    private LuckyBoxMapper mapper;

    @Override
    @Transactional
    public int createEvent(GameArenasSaveDto info) {
        int i = gamebattlemapper.createEvent(info);
        if (i > 0) {
            //记录箱子
            info.getListBox().stream().forEach(e -> {
                GameArenaBox bx = new GameArenaBox();
                bx.setBoxId(e);
                bx.setGameArenaId(info.getId());
                gamebattlemapper.insertArenaBox(bx);
            });
        }
        //更新对战用户
        GameArenaUsers us = new GameArenaUsers();
        us.setGameArenaId(info.getId());
        us.setSeat(0);
        us.setUserId(info.getCreateUserId());
        us.setWorth(info.getTotalBean());
        int numb = gamebattlemapper.insertArenaUsers(us);
        return numb;
    }

    @Override
    public PageInfo<GameArenasDto> getEventList(BasePage base) {
        PageHelper.startPage(base.getPageNo(), base.getPageSize());
        List<GameArenasDto> list = gamebattlemapper.getEventList(base);
        PageInfo<GameArenasDto> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public GameArenasDto getGameArenasDetail(int id) {
        return gamebattlemapper.getGameArenasDetail(id);
    }

    @Override
    public List<BoxRecords> joinEvent(int id, UserDto user) throws Exception {
        //查询活动
        GameArenasDto dto = gamebattlemapper.getGameArenasDetail(id);
        //判断余额
        if (dto.getTotalBean().compareTo(user.getBean()) == 1) {
            throw new Exception("余额不足");
        }
        if (CollectionUtils.isEmpty(dto.getListUser())) {
            for (GameArenasUserDto gameArenasUserDto : dto.getListUser()) {
                if (gameArenasUserDto.getGameUserId() == user.getId()) {
                    throw new Exception("您已经加入，等待活动开始");
                }
            }
        }
        //判断是否满足开启条件
        int numb = (dto.getListUser().size() + 1);
        if (dto.getUserNum() < numb) {
            throw new Exception("活动已结束");
        } else {
            //人员加入
            GameArenaUsers us = new GameArenaUsers();
            us.setGameArenaId(dto.getId());
            us.setSeat(dto.getListUser().size());
            us.setUserId(user.getId());
            us.setWorth(dto.getTotalBean());
            gamebattlemapper.insertArenaUsers(us);
        }
        if (dto.getUserNum() == numb) {
            //对战开始
            for (GameArenasBoxDto gameArenasBoxDto : dto.getListBox()) {
                //获取宝箱下的武器列表
                List<BoxAwards> listAward = mapper.getIndexBoxList(gameArenasBoxDto.getBoxId());
                //洗牌
                Collections.shuffle(listAward);

            }
        }

        return null;
    }
}
