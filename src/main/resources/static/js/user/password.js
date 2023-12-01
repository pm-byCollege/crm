layui.use(['element', 'layer', 'layuimini','jquery','jquery_cookie'], function () {
    var $ = layui.jquery,
        layer = layui.layer,
        $ = layui.jquery_cookie($);

    form.on('submit(saveBtn)', function (data){
        $.ajax({
            type: "post",
            url: ctx + "/user/updatePwd",
            data: {
                oldPassword: data.field.old_password,
                newPassword: data.field.new_password,
                repeatPassword: data.field.again_password
            },
            success: function (res) {
                if (res.code == 200) {
                    // 清空cookies 跳转登录界面
                    layer.msg("用户密码修改成功，三秒后退出", function () {
                        $.removeCookie("userIdStr", {domain: "localhost", path: "/crm"});
                        $.removeCookie("userName", {domain: "localhost", path: "/crm"});
                        $.removeCookie("trueName", {domain: "localhost", path: "/crm"});

                        window.location.href = ctx + "/index";
                    })
                } else {
                    layer.msg(res.msg, {icon: 5})
                }
            }
        });
    });

});