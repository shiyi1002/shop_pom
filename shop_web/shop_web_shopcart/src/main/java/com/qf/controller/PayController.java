package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.qf.entity.Orders;
import com.qf.service.IOrdersService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.alipay.api.AlipayConstants.APP_ID;
import static com.alipay.api.AlipayConstants.FORMAT;

@Controller
@RequestMapping("/pay")
public class PayController {
    @Reference
    private IOrdersService ordersService;
    @RequestMapping("/toPay")
    @ResponseBody
    public void toPay(String oid, HttpServletResponse response) throws IOException {
        //获得订单
        Orders orders=ordersService.queryOrderById(oid);
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", "2016093000631909", "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDdv8zf7SZa96BdhuJkFLSzaUQWjCTGpPN63hjssTQoyHbLVvz0IhtjlbvVgqsO9HwbZFzE0a4gNwlTDTqNIEwH8L5WFGLJoddu1mxUVlapiiotZczPrEjctv3nJ9ttZ54k0ol0KyBcHcwUX5211h6+ZI0G1mc++NPHCB8R9gaPKw35OnrNzbVETlME3IvaU0UHIOmyBGEEgaoDZEtS5sxGU4LdA/zPbnSvMTYo0ugcWIq3dR7oV+2zQKZU3T5H0yWLfZTW5v/1cZBjzR5QTLBMGo0c+jI4fCVfZCRG5H48XxU1+yNZ49bBuQ7mhpPfxDg9VrMOIMcRoEZkFWHVneytAgMBAAECggEAAVfhGNYp7lEjso1Xn72UXqXbmLtemal0JCFmS0DDjR+xwIeVXrE6Mz879Q4mlR+cBNp1yWdvw5gxwUQKP8KbU0z9Ci5zbr9BO9WmsFgZjEte+Alu1S84/cVKAnV2WdJP3e88j+yW9JflnZX6sZhLdWHMjOcNlw8Q0VrWPVidL47TUsxNa0tyEViW/tpboRe4WS/rhQ1+UUHNlqx0NyPQTmvYCOvMnshPoEi3b4mOhPDrsWdIi19Estla99bHx64TbAjI3P7WNA3ZpIHj7QwmA6qlT73J7qdfjTp/kAZ+cnyRhDLZ2Dwnn7JKSe84j71OJkzO4ogLQgAATEgjRnfOzQKBgQDxhVPrDSja4VZZJdAF3rr6L2Neku0o19WLdIjLVrQVtqurG9e3unjT2KnVJdxwz2iVNCcyXKOPCp2c6KtGQ7l5NGyla2+fd4A38E5G7PxlY4k5kX0DWbBurwATQfeUH4L7GMo3pAYM6u9dDUaSRsLiWnHP06AVgI1TKyPLeBqI6wKBgQDrCwiZCj4XIrSxXTV7X/bMg70WbHZqofRwyiephD1jOcuo1UURw8EiEPzrfNd6NCZcekq7wqLp3h+brNdK5yzz2IuFKeLvt0x2hyRI61N6GctgMSWyIalnoYcFuCdKNLVo7yu77+boPgRiY4+laV+NmMPhuEqpzOB18RX729j6xwKBgFMKOWk2tBZVpb+u/ZAXHYiJpIvOzpXdZ8G0BpiLLg3NKccCIrA7//EurG3pv5pV5apvLQ7mxSGYT8q0QLc+79VZjgVuJAnbJ42UJQXD05/0kijl+eqd+siDiO4WH06hawJ0aoqo7Z+hYz1GJD6E79qtqDYBmCfsdCrlSUBbAhyTAoGBAIwWznAQt8MoT1QrPtYi80Ef/9JeoRGbJ8sV4h/aDpCWgPlG69zTtMfuIiDlDrTqYjbaOLLCJkGHv4UUtlkRgixwU2/x8C16LZuznn/LqrxqpVnW0SXjhEmc75IMGXbUBY/ehU2FhjXDyO8vcg1fybDmbVGaev3Le7kwES9E8IU/AoGAR8XHDNG5qGSpOqenLalVaCD0KhPL9Hsr+SPsTFqjDMUfuGtGig4S/rrOsQfmGKmg8N6pO4UIYCRzXaVXdhBdLbz0AfCfEnfNn7SsE66dhReUwDzDHcPyjs8xd7Oi+SAapoRFOTeLQe4T1BcGkSo7oeYd43d8KwmHAs4PYzrjZMQ=", "json", "UTF-8", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwo09PSidjnvC651aDW+OWI4RWhpJU+4gO8AAjfiD0YgF4A2VRrQos2KQ0+MoeO4THsbHcrOQ3X5aRJdA4lckorYM23B6beXnkZ07jk9sS72RBUQvq0Ica3bsDwy6V8xzDTGsdfJFiKn2hFT1oBbWJKdnKVZEFgzgnezYDHHoLxgm4cd4E62Q7xcHtT5B5aMjk3F84mnR+L19zuzOP4Fu+fBCEecXqXgH32rT3JTcREqpJ3m6PlZc2DZxzakG2W3SRplC0LIt5uVXveOG0kV+3zfnVYA5hh04PJE4i5ZMx047hqRmDxzAChL6A3jLgN9c68q3RHkxd1Nlbj7FrQ8SlwIDAQAB", "RSA2"); //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://www.baidu.com");
        alipayRequest.setNotifyUrl("http://245760fu14.zicp.vip/pay/aliPay");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+orders.getOrderid()+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":"+orders.getAllprice().doubleValue()+"," +
                "    \"subject\":\""+orders.getOrderid()+"\"," +
                "    \"body\":\""+orders.getOrderid()+"\"," +

                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=" + "UTF-8");
        response.getWriter().write(form);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
    }

    @RequestMapping("/aliPay")
    @ResponseBody
    public void aliPay(String out_trade_no,String trade_status){
        Orders orders = ordersService.queryOrderById(out_trade_no);
            if(trade_status.equals("TRADE_SUCCESS")){//交易成功
                    orders.setStatus(1);//改变状态,更新订单对象
                ordersService.updateOrders(orders);
            }
        System.out.println("支付成功");
    }
}
