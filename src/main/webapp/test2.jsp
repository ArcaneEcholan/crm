<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript">
        //页面加载完成之后(才能用js取得页面标签)
        $(function(){
            $("#submitBtn").click(function () {
                //通过选中的单选框获取活动id
                var id = $(":input[name=xz]:checked").val();
                //根据id查得活动属性
                var name = $("#" + id).html();

                $("#id").val(id);
                $("#name").val(name);
            })
        });
    </script>
</head>
<body>

选择列表:<br>
<table id="activityTable">
    <thead>
    <tr>
        <td>选择</td>
        <td>名称</td>
        <td>所有者</td>
    </tr>
    </thead>
    <tbody id="tbody">
    <tr>
    <%--        将活动id埋在选框中,方便在选中按钮时用js获取id值--%>
        <td><input type="radio" name="xz" value="98a0s4712h123187s023rg0"/></td>
    <%--        用id标记活动属性,获取id后就可方便取得id属性--%>
        <td id="98a0s4712h123187s023rg0">做广告</td>
        <td>zhangsan</td>
    </tr>
    <tr>
        <td><input type="radio" name="xz" value="298hf938hfp9q38hfp3g0qw"/></td>
        <td id="298hf938hfp9q38hfp3g0qw">发传单</td>
        <td>lisi</td>
    </tr>
    <tr>
        <td><input type="radio" name="xz" value="28alsj3132h123187s02332"/></td>
        <td id="28alsj3132h123187s02332">搞特价</td>
        <td>wangwu</td>
    </tr>
    </tbody>
</table>

<div>
<%--点击按钮,将上面选项中的数据填入下面的数据框中--%>
<button type="button" id="submitBtn">提交</button>
</div>

<%--数据接收框--%>
    <div>
    <%--展示市场活动的名字,需要从给定的列表中选取--%>
    从上面选择活动填入该框:<input type="text" id="name" placeholder="从上面选择填入" readonly>
    <%--隐藏域,用于存放市场活动id,到时候需要过后台时方便发送--%>
    <input type="hidden" id="id"/>
</div>

</body>
</html>
