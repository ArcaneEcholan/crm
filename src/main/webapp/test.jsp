<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String base = request.getScheme()
            + "://"
            + request.getServerName()
            + ":"
            + request.getServerPort()
            + request.getContextPath()
            + "/";
%>

<html>
<head>
    <base href="<%=base%>">
    <title>Title</title>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript">

        $(function(){

            $("#testSelect").change(function () {
                console.log($(this).val());
            })
        });

        //关联市场活动按钮
        function bund() {
            alert("hello");
        }


    </script>
</head>

<body>

<select id="testSelect">
    <option value="1">选项1</option>
    <option value="2">选项2</option>
    <option value="3">选项3</option>
</select>


<input type="radio" name="test" value="1">1<br>
<input type="radio" name="test" value="2">2<br>
<input type="radio" name="test" value="3">3<br>

<div>
    <a href="javascript:void(0);" onclick="bund()" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>关联市场活动</a>
</div>


</body>
</html>
