package com.kinoko.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.kinoko.entity.Activity;
import com.kinoko.entity.Student;
import com.kinoko.service.ActivityService;
import com.kinoko.service.StudentService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private StudentService studentService;

    //创建令牌桶实例，一次可以进来20个请求
    private RateLimiter rateLimiter = RateLimiter.create(30);

    /**
     *跳转活动列表页面，查询所有活动
     */
    @GetMapping("/showActivities")
    public String showActivities(Model model){

        List<Activity> activities = activityService.list();

        //List<Activity> activities = activityService.showActivityList();
        model.addAttribute("activities",activities);
        //标记信息，标记此页面是否属于学生自己的活动列表
        model.addAttribute("studentList",false);
        return "activity/activityList";
    }

    /**
     * 根据活动id跳转活动详情页面
     */
    @GetMapping("/toDetailPage/{id}")
    public String toDetailPage(@PathVariable("id") int id,
                               Model model){
        Subject subject = SecurityUtils.getSubject();
        Student student = (Student) subject.getPrincipal();
        //判断此活动是否被学生选过，以便在前端决定显示抢票按钮还是退票按钮
        model.addAttribute("selected", activityService.judgeSelected(student.getId(), id));
        //Activity activity = activityService.selectActivityById(id);
        Activity activity = activityService.getById(id);
        model.addAttribute("activity",activity);
        return "activity/activityDetail";
    }

    /**
     * 学生已选活动列表
     */
    @GetMapping("/showActivitiesOfStudent")
    public String showActivitiesOfStudent(Model model){
        Subject subject = SecurityUtils.getSubject();
        Student student = (Student) subject.getPrincipal();
        List<Activity> activities = studentService.showSelectedActivities(student.getId());
        model.addAttribute("activities",activities);
        //标记信息，标记此页面是否属于学生自己的活动列表
        model.addAttribute("studentList",true);
        return "activity/activityList";
    }

    @GetMapping("/doKill/{aid}")
    public String doKill(@PathVariable("aid") int aid,
                         RedirectAttributes model){
        //加入令牌桶算法限流
        //如果五秒内尝试获得令牌失败，则直接过滤请求
        if(!rateLimiter.tryAcquire(5, TimeUnit.SECONDS)){
            model.addFlashAttribute("msg","抢票失败，请稍后重试请重试");
            return "redirect:/activity/toDetailPage/" + aid;
        }
        //方法中已经防止用户偷鸡，提前抢票
        if(!activityService.checkTime(aid)){
            model.addFlashAttribute("msg","对不起，还未到抢票时间");
            return "redirect:/activity/toDetailPage/" + aid;
        }
        try{
            activityService.killTicket(aid);
//            model.addFlashAttribute("activity",activity);
            model.addFlashAttribute("msg","订单提交成功，请前往我的活动页面刷新查看");
            return "redirect:/activity/toDetailPage/" + aid;
        }catch (RuntimeException e){
            //正常情况下的用户不会进入此异常，除非直接对抢票的API进行调用
            if (e.getMessage().equals("重复抢票")){
                model.addFlashAttribute("msg","您已经拥有门票");
            }
            else{
                model.addFlashAttribute("msg","对不起，当前已经没有剩余门票");
            }
            return "redirect:/activity/toDetailPage/" + aid;
        }
    }

    @GetMapping("/doRefund/{aid}")
    public String doRefund(@PathVariable("aid") int aid,
                           RedirectAttributes model){
        if (studentService.refundTicket(aid)){
            model.addFlashAttribute("msg","退票成功");
        }
        else{
            //用户通过正常手段并不会走到此语句，除非程序员直接请求API接口
            model.addFlashAttribute("msg","您尚未拥有门票");
        }
        return "redirect:/activity/showActivitiesOfStudent";
    }
}
