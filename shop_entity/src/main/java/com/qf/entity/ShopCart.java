package com.qf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.org.glassfish.external.arc.Taxonomy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("shopcart")
public class ShopCart implements Serializable {
    //主键自增
    @TableId(type = IdType.AUTO)
    private int id;
    private int gid;
    private int uid;
    private int gnumber;//商品价格
    private BigDecimal allprice;//小计

    @TableField(exist = false)  //这个是数据库没有的
    private Goods goods;
}
