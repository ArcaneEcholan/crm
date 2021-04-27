<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<!DOCTYPE html>
<html>
<head>
	<base href="<%=base%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(function () {

			//更新数据
			//前端需要做:将更新信息发送到后台
			$("#editBtn").click(function () {
				$.get("dicServlet",{
					"action":"editDicValue",

					"id":$("#valueId").val(),
					"value":$("#edit-dicValue").val(),
					"text":$("#edit-text").val(),
					"orderNo":$("#edit-orderNo").val()
				}, function (data) {
					if(data.success) {
						alert("修改成功")
					} else {
						alert("修改失败");
					}
				}, "json")
			})
		})
	</script>
</head>
<body>

	<div style="position:  relative; left: 30px;">
		<h3>修改字典值</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="editBtn">更新</button>
			<button type="button" class="btn btn-default" onclick="window.location.href='settings/dictionary/value/index.jsp';">返回</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form">

		<input type="hidden" class="form-control" id="valueId" value="${dicValue.id}">
		<div class="form-group">
			<label for="edit-dicTypeCode" class="col-sm-2 control-label">字典类型编码</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-dicTypeCode" style="width: 200%;" value="${dicValue.typeCode}" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-dicValue" class="col-sm-2 control-label">字典值<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-dicValue" style="width: 200%;" value="${dicValue.value}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-text" class="col-sm-2 control-label">文本</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-text" style="width: 200%;" value="${dicValue.text}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-orderNo" class="col-sm-2 control-label">排序号</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-orderNo" style="width: 200%;" value="${dicValue.orderNo}">
			</div>
		</div>
	</form>
	
	<div style="height: 200px;"></div>
</body>
</html>