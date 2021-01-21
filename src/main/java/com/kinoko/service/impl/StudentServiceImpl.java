package com.kinoko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kinoko.dao.ActivityMapper;
import com.kinoko.dao.StudentMapper;
import com.kinoko.dao.TicketRecordMapper;
import com.kinoko.entity.Activity;
import com.kinoko.entity.Student;
import com.kinoko.entity.TicketRecord;
import com.kinoko.service.StudentService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper,Student> implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private TicketRecordMapper ticketRecordMapper;

    @Override
    public void login(String id, String password) throws AuthenticationException{
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(id,password);
        subject.login(token);
    }

    @Override
    @Deprecated
    public Student selectStudentById(String id) {
        return studentMapper.selectById(id);
    }

    @Override
    public List<Activity> showSelectedActivities(String sid) {
        QueryWrapper<TicketRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("s_id",sid);
        List<TicketRecord> records = ticketRecordMapper.selectList(wrapper);
        //将TicketRecord集合中的aid属性抽取出来作为list
        List<Integer> aids = records.stream().map(TicketRecord::getAid).collect(Collectors.toList());
        //如果不加判断，当aids为空的时候selectBatchIds会报错
        if(aids.size() == 0)
            return null;
        return activityMapper.selectBatchIds(aids);
    }


    /**
     * 如果查到订单记录则代表有票，直接删除即可，删除语句如果生效会返回修改行数
     * 删除成功库存+1
     */
    @Override
    public boolean refundTicket(int aid) {
        Subject subject = SecurityUtils.getSubject();
        Student student = (Student) subject.getPrincipal();
        if(ticketRecordMapper.delete(new QueryWrapper<TicketRecord>()
                .eq("s_id", student.getId())
                .eq("a_id", aid)) == 0){
            return false;
        }
        //库存+1
        Activity activity = activityMapper.selectById(aid);
        activity.setRest(activity.getRest() + 1);
        activityMapper.updateById(activity);
        return true;
    }
}
