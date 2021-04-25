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
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">
	$(function(){

		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		})

		//打开搜索市场活动窗口
		$("#openSearchModalBtn").click(function () {
			$("#searchActivityModal").modal("show");
		})

		$("#aname").keydown(function (event) {
			if(event.keyCode == 13) {
				$.get("clueServlet", {
					"action":"searchActivityByName",

					//按该名称查找市场活动
					"aname":$.trim($("#aname").val())
				},function (data) {
					//data[{act1}{act1}{act1}]

					var html = "";
					$.each(data, function (i,n) {
						//n是activity对象
						html += '<tr>';
						html += '<td><input type="radio" name="xz" value="'+n.id+'"/></td>';
						html += '<td id="'+n.id+'">'+n.name+'</td>';
						html += '<td>'+n.startDate+'</td>';
						html += '<td>'+n.endDate+'</td>';
						html += '<td>'+n.owner+'</td>';
						html += '</tr>';
					});

					$("#searchBody").html(html);
				}, "json")

			}
			return false;
		})

		$("#searchSubmitBtn").click(function () {


			var id = $(":input[name=xz]:checked").val();
			console.log(id);
			$("#activityId").val(id);
			$("#activityName").val($("#" + id).html());

			$("#searchActivityModal").modal("hide");
		})

		/**
		 * 当前转换线索为客户（公司）和公司联系人
		 * 前端需要什么：转换成功与否标志
		 * 前端需要做什么：转换成功与否提示，转换成功后跳转到线索的index页面
		 * 后端需要什么：当前线索id
		 */
		$("#convertBtn").click(function () {
			var $checked = $("#isCreateTransaction").prop("checked");

			if(confirm("你去定要转换该线索吗？")) {
				$.get("clueServlet",{
					"action":"convertClue",
					"clueId":"${param.id}",

					"flag":$checked,
					"money":$("#amountOfMoney").val(),
					"name":$("#tradeName").val(),
					"expectedDate":$("#expectedClosingDate").val(),
					"stage":$("#stage").val(),
					"activityName":$("#activityName").val(),
					"activityId":$("#activityId").val()
				}, function(data) {
					if(data.success) {
						alert("转换成功");
						window.location.href="workbench/clue/index.jsp"
					} else {
						alert("转换失败");
					}
				}, "json")
			}
		})
	});
</script>

</head>
<body>

	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="aname" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="searchBody">
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>

						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="searchSubmitBtn">提交</button>
				</div>
			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${param.fullname}${param.appellation}-${param.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${param.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${param.fullname}${param.appellation}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >

		<form id="tranForm" action="clueServlet" method="post">
<%--			用于后台识别是否是通过提交表单的形式发起的请求	--%>
			<input type="hidden" name="flag" value="a"/>
			<input type="hidden" name="action" value="convert"/>

		<%--	提交:线索id,交易金额,交易名称,交易日期,交易阶段,市场活动源的id--%>
			<input type="hidden" name="clueId" value="${param.id}"/>

			<div class="form-group" style="width: 400px; position: relative; left: 20px;">
				<label for="amountOfMoney">金额</label>
				<input type="text" class="form-control" id="amountOfMoney" name="money">
			</div>
			<div class="form-group" style="width: 400px;position: relative; left: 20px;">
				<label for="tradeName">交易名称</label>
				<input type="text" class="form-control" id="tradeName" name="name">
			</div>
			<div class="form-group" style="width: 400px;position: relative; left: 20px;">
				<label for="expectedClosingDate">预计成交日期</label>
				<input type="text" class="form-control time" id="expectedClosingDate" name="expectedDate">
			</div>
			<div class="form-group" style="width: 400px;position: relative; left: 20px;">
				<label for="stage">阶段</label>
				<select id="stage"  class="form-control" name="stage">
					<option></option>
					<c:forEach items="${stage}" var="s">
						<option value="${s.value}">${s.text}</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group" style="width: 400px;position: relative; left: 20px;">
				<label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="openSearchModalBtn" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
				<input type="text" class="form-control" id="activityName" placeholder="点击上面搜索" readonly>
				<input type="hidden" id="activityId" name="activityId"/>
			</div>
		</form>

	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${param.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" id="convertBtn" value="转换">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>
</html>