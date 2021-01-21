package com.kinoko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kinoko.dao.ActivityMapper;
import com.kinoko.dao.TicketRecordMapper;
import com.kinoko.entity.Activity;
import com.kinoko.entity.Student;
import com.kinoko.entity.TicketRecord;
import com.kinoko.service.ActivityService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.*;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper,Activity> implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private TicketRecordMapper ticketRecordMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    @Deprecated
    public List<Activity> showActivityList() {
        return activityMapper.selectList(null);
    }

    @Override
    @Deprecated
    public Activity selectActivityById(int id) {
        return activityMapper.selectById(id);
    }

    @Override
    public boolean judgeSelected(String sid, int aid) {
        QueryWrapper<TicketRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("s_id",sid)
                .eq("a_id",aid);
        TicketRecord ticketRecord = ticketRecordMapper.selectOne(wrapper);
        return ticketRecord != null;
    }

    @Override
    public Activity killTicket(int aid) {
        //校验库存
        Activity activity = checkRest(aid);
        //扣除库存
        updateRest(activity);
        //创建record信息
        creatRecord(activity);

        return activity;
    }

    /**
     * 通过redis查询是否到合法时间
     */
    @Override
    public boolean checkTime(int aid) {
        return Long.parseLong(Objects.requireNonNull(redisTemplate.opsForValue().get("kill" + aid))) <= new Date().getTime();
    }

    /**
     * 根据活动id校验库存
     */
    private Activity checkRest(int aid){
        Subject subject = SecurityUtils.getSubject();
        Student student = (Student) subject.getPrincipal();
        TicketRecord record = ticketRecordMapper.selectOne(new QueryWrapper<TicketRecord>()
                .eq("s_id", student.getId())
                .eq("a_id", aid));
        if(record != null){
            throw new RuntimeException("重复抢票");
        }
        Activity activity = activityMapper.selectById(aid);
        if(activity.getRest() <= 0){
            throw new RuntimeException("剩余票数不足");
        }
        else{
            return activity;
        }
    }

    /**
     * 扣除库存
     */
    private void updateRest(Activity activity){
        activity.setRest(activity.getRest() - 1);
        activityMapper.updateById(activity);
    }

    /**
     * 创建抢票记录
     */
    private void creatRecord(Activity activity){
        Subject subject = SecurityUtils.getSubject();
        Student student = (Student) subject.getPrincipal();
        TicketRecord ticketRecord = new TicketRecord(student.getId(),activity.getId());
        ticketRecordMapper.insert(ticketRecord);
    }
}
