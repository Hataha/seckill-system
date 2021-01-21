package com.kinoko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kinoko.dao.ActivityMapper;
import com.kinoko.dao.StudentMapper;
import com.kinoko.dao.TicketRecordMapper;
import com.kinoko.entity.Activity;
import com.kinoko.entity.TicketRecord;
import com.kinoko.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class SeckillSystemUserApplicationTests {

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TicketRecordMapper ticketRecordMapper;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    void contextLoads() {

        // TODO:创建秒杀失败页面，防止每次请求无论成功失败都会有额外查询任务
        // TODO:前端秒杀接口使用js加上重复点击的限制
        // TODO:前端秒杀接口到秒杀时间再显示，不需要动态显示

        List<Activity> activities = activityMapper.selectList(null);
        for (Activity activity : activities) {
            redisTemplate.opsForValue().set("kill" + activity.getId(), String.valueOf(activity.getStartTime().getTime()));
            System.out.println(String.valueOf(activity.getStartTime().getTime()));
        }
        System.out.println(new Date().getTime());
    }

}
