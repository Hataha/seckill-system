package com.kinoko.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("student_activity")
public class TicketRecord {
    @TableField(value = "s_id")
    private String sid;
    @TableField(value = "a_id")
    private int aid;
}
