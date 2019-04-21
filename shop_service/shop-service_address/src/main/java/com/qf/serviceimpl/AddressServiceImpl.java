package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.AddressMapper;
import com.qf.entity.Address;
import com.qf.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService{
    @Autowired
    private AddressMapper addressMapper;
    @Override
    public List<Address> queryAddress(int uid) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("uid",uid);

        return  addressMapper.selectList(queryWrapper);

    }

    /**
     * 新增地址
     * @param address
     * @return
     */
    @Override
    public int insertAddress(Address address) {
        //添加地址要判断是不是设置为默认,如果是就要把其他的默认设置为非默认
        //这里创建存储过程,直接调用即可
        //调用存储过程
        //System.out.println("调用存储过程");
        int result=addressMapper.insertAddress(address);

        return result;
    }

    @Override
    public Address queryById(int aid) {
        return addressMapper.selectById(aid);
    }

}
