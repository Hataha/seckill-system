package com.kinoko.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * 这个类可以用来处理创建更新操作时数据库的一些自动行为，比如自动更新字段
 * 自动插入字段等
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //新增活动数据时版本号字段自动设置为0
        this.setFieldValByName("version",0,metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
