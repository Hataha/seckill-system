package com.kinoko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kinoko.entity.Activity;
import com.kinoko.entity.Student;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService extends IService<Student> {
    /**
     * 登录方法
     * @param id 学号
     * @param password 加密过的密码
     * @throws AuthenticationException 如果登录失败抛出异常，在controller中捕获
     */
    void login(String id, String password);

    /**
     * 通过学号查询学生
     * @param id 学号
     * @return 学生entity
     */
    @Deprecated
    Student selectStudentById(String id);

    /**
     * 查看已选择的活动列表
     * @param sid 学号
     * @return 已选活动列表
     */
    List<Activity> showSelectedActivities(String sid);

    /**
     * 退票业务，如果学生已经有票则退票，无票则提示退票失败
     * @param aid 活动id
     * @return 退票成功返回true，失败返回false
     */
    boolean refundTicket(int aid);
}
