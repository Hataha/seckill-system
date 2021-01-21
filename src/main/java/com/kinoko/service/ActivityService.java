package com.kinoko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kinoko.entity.Activity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityService extends IService<Activity> {
    /**
     * 查询所有活动
     * @return 返回符合条件得活动集合
     */
    @Deprecated
    List<Activity> showActivityList();

    /**
     * 通过id查找活动
     * @param id 活动id
     * @return 活动
     */
    @Deprecated
    Activity selectActivityById(int id);

    /**
     * 查询学生是否选择了此活动
     * @param sid 学生id
     * @param aid 活动id
     * @return 如果判断学生已经选取活动，则返回true，未选取则返回false
     */
    boolean judgeSelected(String sid,int aid);

    /**
     * 秒杀方法
     * 1、根据活动id校验库存
     * 2、扣除库存
     * 3、创建抢票记录信息
     *
     * @return 返回要抢票的活动
     */
    Activity killTicket(int aid);

    /**
     * 检查当前时间是否符合抢票时间
     * @param aid 活动id
     * @return 如果当前时间可以抢票，则返回true
     */
    boolean checkTime(int aid);
}
