package com.qf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods implements Serializable{
 //加这个注解告诉需要使用mybatisplus要开主键回填的主键
  @TableId(type = IdType.AUTO)
  private int  id;
   private String gname;
   private BigDecimal gprice;
   private int gsave;
    private String ginfo;
   private String gimage;
   private int status;
   private Date createtime;
   private int tid;
}
