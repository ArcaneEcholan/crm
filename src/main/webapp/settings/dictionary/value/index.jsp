<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/commons/projectPath.jsp"%>
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(function() {

			$("#createBtn").click(function () {
				window.location.href = "dicServlet?action=getAllDicTypeCode";
			})

			//展示所有字典类型
			showAllDicValues();
		})

		function showAllDicValues() {
			$.get("dicServlet",{
				"action":"getAllDicValues",
			}, function(data){
				var html=""
				$.each(data, function(i, n) {
					html += '<tr class="active">'
					html += '<td><input type="checkbox" value="'+n.id+'"/></td>'
					html += '<td>'+(i+1)+'</td>'
					html += '<td>'+n.value+'</td>'
					html += '<td>'+n.text+'</td>'
					html += '<td>'+n.orderNo+'</td>'
					html += '<td>'+n.typeCode+'</td>'
					html += '</tr>'
				})
				$("#dicValueBody").html(html);
			}, "json")
		}
	</script>
</head>
<body>

	<div>
		<div style="position: relative; left: 30px; top: -10px;">
			<div class="page-header">
				<h3>字典值列表</h3>
			</div>
		</div>
	</div>
	<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;left: 30px;">
		<div class="btn-group" style="position: relative; top: 18%;">
		  <button type="button" class="btn btn-primary" id="createBtn""><span class="glyphicon glyphicon-plus"></span> 创建</button>
		  <button type="button" class="btn btn-default" onclick="window.location.href='settings/dictionary/value/edit.jsp'"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
		  <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	<div style="position: relative; left: 30px; top: 20px;">
		<table class="table table-hover">
			<thead>
				<tr style="color: #B3B3B3;">
					<td><input type="checkbox" /></td>
					<td>序号</td>
					<td>字典值</td>
					<td>文本</td>
					<td>排序号</td>
					<td>字典类型编码</td>
				</tr>
			</thead>
			<tbody id="dicValueBody">











			</tbody>
		</table>
	</div>
	
</body>
</html>