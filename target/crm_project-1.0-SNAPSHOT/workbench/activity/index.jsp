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
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<%--	分页插件--%>
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){

		//每次刷新活动列表都将全选框的勾取消
		$("#qx").prop("checked", false);

		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});


		$("#addBtn").click(function () {
			$.get("activityServlet",
					"action=getUserList",
			function(data) {
				//data:[{user1}{user2}{user3}]
				//从后台获取数据铺到页面上
				var html="";
				$.each(data, function(i, n) {
					//n:{user}
					html += "<option value='"+n.id+"'>"+n.name+"</option>";
				})
				$("#create-owner").html(html);

				//将当前用户设置成下拉框默认的选项
				$("#create-owner").val("${sessionScope.user.id}");

				//展现模态窗口
				$("#createActivityModal").modal("show");
			}, "json")
		})

        //保存市场活动
        $("#create-saveBtn").click(function() {
            $.post("activityServlet", {
                    "action":"createActivity",

					//提交表单内容
                    "name" : $.trim($("#create-name").val()),
					"owner" : $.trim($("#create-owner").val()), 	//create-owner是列表，值是选中的选项的value
                    "startDate" : $.trim($("#create-startTime").val()),
                    "endDate" : $.trim($("#create-endTime").val()),
                    "cost" : $.trim($("#create-cost").val()),
                    "description" : $.trim($("#create-description").val())
                }, function (data) {
            		//data:{success:true/false}
                    if(data.success) {
                        $("#createForm")[0].reset();  //清空模态窗口的所有已经填写的数据
                        $("#createActivityModal").modal("hide");  //关闭模态窗口
						pageList(1,2);	//刷新市场活动列表
                    } else {
                        alert("添加失败");
                    }
                },"json")
        })

        //打开修改活动模态窗口
        $("#editBtn").click(function () {
            var checks = $(":input[name=xz]:checked");

            if(checks.length == 0) {
                alert("请选择需要修改的活动！");
            } else if(checks.length == 1){
                var params = "";
                params += ("id=" + checks.val());

                // alert(params);

                $.getJSON("activityServlet", params + "&action=openUpdateModal", function (data) {

                    //data.uList用户集合
                    //data.activity对象
                    var html = "<option></option>"
                    $.each(data.uList, function (i, n) {
                        html += "<option value='"+n.id+"'>"+n.name+"</option>";
                    })

                    $("#edit-owner").html(html);

                    $("#edit-name").val(data.activity.name);
                    $("#edit-cost").val(data.activity.cost);
                    $("#edit-startDate").val(data.activity.startDate);
                    $("#edit-endDate").val(data.activity.endDate);
                    $("#edit-describe").val(data.activity.description);

                    $("#editActivityModal").modal("show");
                })
            } else {
                alert("一次只能修改一个活动");
            }
        })

		//点击更新修改活动
		$("#edit-updateBtn").click(function() {

		    // alert($("#edit-owner option:selected").val());

			$.getJSON("activityServlet", {
						"action":"editActivity",
						"id" : $.trim($(":input[name=xz]:checked").val()),
						"owner" : $.trim($("#edit-owner option:selected").val()),
						"name" : $.trim($("#edit-name").val()),
						"startDate" : $.trim($("#edit-startDate").val()),
						"endDate" : $.trim($("#edit-endDate").val()),
						"cost" : $.trim($("#edit-cost").val()),
						"description" : $.trim($("#edit-description").val())
					}, function (data) {
						if(data.success) {
							$("#editActivityModal").modal("hide");
                            pageList(1,2);
						} else {
							alert("修改失败");
						}
					}
			)
		})

        //点击删除活动
        $("#delBtn").click(function () {

            var checks = $(":input[name=xz]:checked");

            if(checks.length == 0) {
                alert("请选择需要删除的活动！");
            } else {
                var params = "";
                for (let i = 0; i < checks.length; i++) {
                    params += ("id=" + $(checks[i]).val());

                    if(i < checks.length - 1) {
                        params += "&";
                    }
                }

                if(confirm("你确定要删除吗")) {
                    $.getJSON("activityServlet", params + "&action=deleteActivity", function (data) {
                        if(data.success) {
                            pageList(1,2);
                        } else {
                            alert("删除失败");
                        }
                    })
                }
            }
        })

		//点击查询活动
		$("#searchBtn").click(function () {
			pageList(1,2);
		})

		$(":input[name=xz]").click(function () {
			if($(this).prop("checked")) {
				var checks = $(":input[name=xz]");

				checks.prop("checked", true);
			} else {
				var checks = $(":input[name=xz]");

				checks.prop("checked", false);
			}
		})

		pageList(1,2);
	});

	//分页函数
	function pageList(pageNo, pageSize) {

		$.get("activityServlet", {
			"action":"page",
			//发送页码，每页大小
			"pageNo":pageNo,
			"pageSize":pageSize,

            //以下参数来自查询框，可有可无
			"name":$("#name").val(),
			"owner":$("#owner").val(),
			"startTime":$("#startTime").val(),
			"endTime":$("#endTime").val()
		},function (data) {
			//来自服务器：data:{totalCount:总记录数 ,totalPages：总页数， list:[{act1}{act2}{act3}]}
			//将数据展示在页面上
			var html = "";
			$.each(data.list, function (i,n) {
				//n:{act}
				html += '<tr>';
				html += '<td> <input type="checkbox" name="xz" value="'+n.id+'"/> </td>';
				html += '<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href=\'activityServlet?action=showDetail&id=' +n.id+ ' \';\">' +n.name+ '</a></td>';
				html += '<td>'+n.owner+'</td>';	//owner是所有者名字
				html += '<td>'+n.startDate+'</td>';
				html += '<td>'+n.endDate+'</td>';
				html += '</tr>';
			});
			$("#activityBody").html(html);

			//前端分页插件
			$("#activityPage").bs_pagination({
				currentPage: pageNo, // 页码
				rowsPerPage: pageSize, // 每页显示的记录条数
				maxRowsPerPage: 20, // 每页最多显示的记录条数
				totalPages: data.totalPages, // 总页数
				totalRows: data.totalCount, // 总记录条数

				visiblePageLinks: 3, // 显示几个卡片

				showGoToPage: true,
				showRowsPerPage: true,
				showRowsInfo: true,
				showRowsDefaultInfo: true,

				onChangePage : function(event, data){
					pageList(data.currentPage , data.rowsPerPage);
				}
			});

		}, "json")
	}
</script>
</head>
<body>
<%--<button id="searchBtn1" class="btn btn-default">查询</button>--%>
<%--<button id = "testBtn"> 测试 </button>--%>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
	<div class="modal-dialog" role="document" style="width: 85%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
			</div>
			<div class="modal-body">

				<form id="" class="form-horizontal" role="form">


					<div class="form-group">
						<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
						<div class="col-sm-10" style="width: 300px;">
							<select class="form-control" id="edit-owner">

							</select>
						</div>
						<label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="edit-name" >
						</div>
					</div>

					<div class="form-group">
						<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="edit-startDate">
						</div>
						<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="edit-endDate">
						</div>
					</div>

					<div class="form-group">
						<label for="edit-cost" class="col-sm-2 control-label">成本</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="edit-cost">
						</div>
					</div>

					<div class="form-group">
						<label for="edit-describe" class="col-sm-2 control-label">描述</label>
						<div class="col-sm-10" style="width: 81%;">
							<textarea class="form-control" rows="3" id="edit-description"></textarea>
						</div>
					</div>

				</form>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" id="edit-closeBtn">关闭</button>
				<button type="button" class="btn btn-primary" id="edit-updateBtn">更新</button>
			</div>
		</div>
	</div>
</div>

<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id = "createForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">

								<select class="form-control" id="create-owner">

								</select>

							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" id="create-closeBtn">关闭</button>
					<button type="button" class="btn btn-primary" id="create-saveBtn">保存按钮</button>
				</div>
			</div>
		</div>
	</div>
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="name"/>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="owner"/>
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="startTime"/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="endTime">
				    </div>
				  </div>
				  
				  <button type = "button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
<%--					打开模态窗口按钮--%>
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="delBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input id="qx" type="checkbox" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
<%--					分页展示活动--%>
					<tbody id="activityBody">
<%--											<tr class='active'>--%>
<%--												<td><input type='checkbox' /></td>--%>
<%--												<td><a style='text-decoration: none; cursor:pointer;'onclick='window.location.href="workbench/activity/detail.jsp";'>???</a></td>--%>
<%--												<td>1212</td>--%>
<%--												<td>null</td>--%>
<%--												<td>null</td>--%>
<%--											</tr>--%>

<%--						<tr class='active'>--%>
<%--							<td><input type='checkbox' /></td>--%>
<%--							<td><a style='text-decoration: none; cursor:pointer;'onclick='window.location.href="workbench/activity/detail.jsp";'>???</a></td>--%>
<%--							<td>1212</td>--%>
<%--							<td>null</td>--%>
<%--							<td>null</td>--%>
<%--						</tr>--%>
<%--						<tr class='active'>--%>
<%--							<td><input type='checkbox' /></td>--%>
<%--							<td><a style='text-decoration: none; cursor: pointer;' onclick='window.location.href="workbench/activity/detail.jsp";'>???</a></td>--%>
<%--							<td>1212</td>--%>
<%--							<td>null</td>--%>
<%--							<td>null</td>--%>
<%--						</tr>--%>
<%--						<tr class='active'>--%>
<%--							<td><input type='checkbox' /></td>--%>
<%--							<td><a style='text-decoration: none; cursor: pointer;' onclick='window.location.href="workbench/activity/detail.jsp";'>???</a>--%>
<%--							</td>--%>
<%--							<td>1212</td>--%>
<%--							<td>null</td>--%>
<%--							<td>null</td>--%>
<%--						</tr>--%>

					</tbody>
				</table>
			</div>



<%--			分页组件--%>
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>