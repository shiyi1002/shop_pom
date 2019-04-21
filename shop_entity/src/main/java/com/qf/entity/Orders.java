package com.qf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;
    private String orderid;//订单号
    private String person;//收货人
    private String address;//收货地址
    private String phone;
    private String code;
    private BigDecimal allprice;//总价
    private Date createtime;
    private int status;
    private int uid;//用户id

    @TableField(exist = false)
    private List<OrderDetils> orderDetils;//订单详情
}