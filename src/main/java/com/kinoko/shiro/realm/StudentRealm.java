package com.kinoko.shiro.realm;

import com.kinoko.entity.Student;
import com.kinoko.service.StudentService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class StudentRealm extends AuthorizingRealm {

    @Autowired
    private StudentService studentService;
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //认证，把登录得student信息保存起来
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        Student student = studentService.getById(token.getUsername());

        if(student == null){
            return null;
        }
        else{
            return new SimpleAuthenticationInfo(student,student.getPassword(),getName());
        }
    }
}
