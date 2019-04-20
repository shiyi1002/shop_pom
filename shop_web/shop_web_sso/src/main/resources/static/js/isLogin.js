
$(function(){
    $.ajax({
        url: "http://localhost:8084/sso/isLogin",
        success: function(data){
            alert("跨域的返回结果：" + data);
            if(data){
                // 已经登录
                $("#pid").html(JSON.parse(data).nickname + "您好，欢迎来到<b><a>ShopCZ商城</a> <a href='http://localhost:8084/sso/logout'>注销</a></b>");
            } else {
                // 未登录
                $("#pid").html("[<a href='javascript:login();'>登录</a>][<a href='http://localhost:8084/sso/toRegister'>注册</a>]");
            }
        },
        dataType:"jsonp",
        jsonpCallback: "isLogin"
    });
});
function login() {
    //获得当前浏览器页面的地址
    var returnUrl=location.href;
    alert(returnUrl);
    returnUrl=encodeURI(returnUrl);
    //把当前浏览器页面的地址通过参数传到登录页面
    location.href="http://localhost:8084/sso/toLogin?returnUrl="+returnUrl;
}


