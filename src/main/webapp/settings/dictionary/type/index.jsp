<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<%
		String base = request.getScheme()
				+ "://"
				+ request.getServerName()
				+ ":"
				+ request.getServerPort()
				+ request.getContextPath()
				+ "/";
	%>
	<base href="<%=base%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(function() {
		showAllDicTypes();
	})

	function showAllDicTypes() {
		$.get("dicServlet",{
			"action":"getAllDicTypes",
		}, function(data){
			console.log(data)
			var html=""
			$.each(data, function(i, n) {
				html += '<tr class="active">'
				html += '<td><input type="checkbox" id="'+n.code+'"/></td>'
				html += '<td>'+(i+1)+'</td>'
				html += '<td>'+n.code+'</td>'
				html += '<td>'+n.name+'</td>'
				html += '<td>'+n.description+'</td>'
				html += '</tr>'
			})

			$("#dicTypeBody").html(html);
		}, "json")
	}
</script>


</head>
<body>

	<div>
		<div style="position: relative; left: 30px; top: -10px;">
			<div class="page-header">
				<h3>字典类型列表</h3>
			</div>
		</div>
	</div>
	<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;left: 30px;">
		<div class="btn-group" style="position: relative; top: 18%;">
		  <button type="button" class="btn btn-primary" onclick="window.location.href='settings/dictionary/type/save.jsp'"><span class="glyphicon glyphicon-plus"></span> 创建</button>
		  <button type="button" class="btn btn-default" onclick="window.location.href='edit.jsp'"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
		  <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	<div style="position: relative; left: 30px; top: 20px;">
		<table class="table table-hover">
			<thead>
				<tr style="color: #B3B3B3;">
					<td><input type="checkbox" /></td>
					<td>序号</td>
					<td>编码</td>
					<td>名称</td>
					<td>描述</td>
				</tr>
			</thead>
			<tbody id="dicTypeBody">







			</tbody>
		</table>
	</div>
	
</body>
</html>