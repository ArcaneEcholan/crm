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

		//前端需要：删除标志
		//前端需要做：删除提示，刷新页面
		//后端需要：删除的id号
		//后端需要做 ：删除dicType和所有关联的dicValue
		$("#delTypeBtn").click(function () {

			var $checked = $(":input:checked");

			if($checked.length == 0) {
				alert("请选择要删除的type")
			} else {
				if(confirm("你确定要删除吗？")) {
					var codes = "";
					for(var i = 0 ; i < $checked.length; i++) {
						codes += ("code=" + $($checked[i]).val())
						codes += "&"
					}
					console.log(codes)
					$.post("dicServlet", codes + "action=delDicTypes" , function(data){
						if(data.success) {
							showAllDicTypes()
						} else {
							alert("删除失败")
						}
					}, "json")
				}
			}
		})

		//编辑字典类型
		//前端需要做：跳转到编辑页面，并将选中的字典类型的代号发送过去
		$("#editTypeBtn").click(function () {
			var $input = $(":input:checked");
			if($input.length == 1) {
				var code = $input.val();
				window.location.href='dicServlet?action=openEditDicTypePage&code=' + code;
			} else if($input.length > 1){
				alert("每次只能修改一个type")
			} else {
				alert("请选择要编辑的type")
			}
		})

		//展示所有字典类型
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
				html += '<td><input type="checkbox" value="'+n.code+'"/></td>'
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
		  <button type="button" class="btn btn-default" id="editTypeBtn"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
		  <button type="button" class="btn btn-danger" id="delTypeBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	<div style="position: relative; left: 30px; top: 20px;">
		<table class="table table-hover">
			<thead>
				<tr style="color: #B3B3B3;">
					<td>选择</td>
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