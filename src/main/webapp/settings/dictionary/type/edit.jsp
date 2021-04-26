<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/commons/projectPath.jsp"%>
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
				"action":"editDicType",

				"code":$("#code").val(),
				"originalCode":$("#originalCode").val(),
				"name":$("#name").val(),
				"description":$("#description").val()
			}, function (data) {
				if(data.success) {
					alert("修改成功")
				} else {
					alert(data.message);
				}
			}, "json")
		})
	})
</script>
</head>
<body>

	<div style="position:  relative; left: 30px;">
		<h3>修改字典类型</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" id="editBtn" class="btn btn-primary">更新</button>
			<button type="button" class="btn btn-default" onclick="window.location.href='settings/dictionary/type/index.jsp'">返回</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form">

<%--		保存修改前的编码，以便发送后台检测编码是否改变--%>
		<input type="hidden" id="originalCode" value="${dicType.code}">
					
		<div class="form-group">
			<label for="create-code" class="col-sm-2 control-label">编码<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="code" style="width: 200%;" placeholder="编码作为主键，不能是中文" value="${dicType.code}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-name" class="col-sm-2 control-label">名称</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="name" style="width: 200%;" value="${dicType.name}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 300px;">
				<textarea class="form-control" rows="3" id="description" style="width: 200%;">${dicType.description}</textarea>
			</div>
		</div>
	</form>
	
	<div style="height: 200px;"></div>
</body>
</html>