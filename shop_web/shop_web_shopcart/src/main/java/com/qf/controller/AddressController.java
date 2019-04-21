package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.aspect.ISLogin;
import com.qf.entity.Address;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/address")
public class AddressController {
    @Reference
    private IAddressService addressService;

    //添加地址
    @RequestMapping("/insertAddress")
    @ResponseBody
    @ISLogin(mustLogin = true)
    public int queryAdderss(Address address, User user){
        address.setUid(user.getId());
        int result=addressService.insertAddress(address);
        System.out.println(address);
        return result;
    }


}
