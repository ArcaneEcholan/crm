$.post("userServlet", {"action":"login" ,"username":username,"password":password}, function (data) {
    //登录成功就跳转
    if(data.success) {
    window.location.href="workbench/index.jsp";
    }
    //否则打印错误信息
    else {
    $("#msg").html(data.msg);
    }
}, "json");

$(".time").datetimepicker({
    minView: "month",
    language:  'zh-CN',
    format: 'yyyy-mm-dd',
    autoclose: true,
    todayBtn: true,
    pickerPosition: "bottom-left"
});


<div class="form-group" style="width: 400px;position: relative; left: 20px;">
    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="openSearchModalBtn" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
    <input type="text" class="form-control" id="activityName" placeholder="点击上面搜索" readonly>
    <input type="hidden" id="activityId" name="activityId"/>
</div>