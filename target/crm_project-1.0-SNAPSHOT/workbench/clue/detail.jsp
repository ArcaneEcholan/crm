<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;

	<%--//为关联按钮绑定事件，执行关联表的添加操作--%>
	<%--$("#bundBtn").click(function () {--%>
	<%--	var $xz = $("input[name=xz]:checked");--%>
	<%--	if($xz.length==0){--%>
	<%--		alert("请选择需要关联的市场活动");--%>
	<%--	}else{--%>
	<%--		var param = "cid=${c.id}&";--%>
	<%--		for(var i=0;i<$xz.length;i++){--%>
	<%--			param += "aid="+$($xz[i]).val();--%>
	<%--			if(i<$xz.length-1){--%>
	<%--				param += "&";--%>
	<%--			}--%>
	<%--		}--%>
	<%--		$.ajax({--%>
	<%--			url : "workbench/clue/bund.do",--%>
	<%--			data : param,--%>
	<%--			type : "post",--%>
	<%--			dataType : "json",--%>
	<%--			success : function (data) {--%>
	<%--				if(data.success){--%>

	<%--					//关联成功--%>
	<%--					//刷新关联市场活动的列表--%>
	<%--					showActivityList();--%>

	<%--					//清除搜索框中的信息  复选框中的√干掉 清空activitySearchBody中的内容--%>

	<%--					//关闭模态窗口--%>
	<%--					$("#bundModal").modal("hide");--%>
	<%--				}else{--%>
	<%--					alert("关联市场活动失败");--%>
	<%--				}--%>
	<%--			}--%>
	<%--		})--%>
	<%--	}--%>
	<%--})--%>
	$(function(){

		//绑定选中的活动（多个）
		//前端需要什么：关联成功与否标志
		//前端需要做：成功则刷新活动列表关闭窗口，失败提示
		//后端需要什么：需要产生关联的活动id号，当前线索id号
		$("#bundBtn").click(function (){
			//获取选中的id号
			var $checked = $(":input[name=xz]:checked");
			if($checked.length==0)  {
				alert("请选择关联的活动")
			} else {
				var param = ""
				for(var i = 0; i < $checked.length; i++) {
					param += ("id=" + $($checked[i]).val());
					if(i < $checked.length - 1) {
						param += "&";
					}
				}

				$.post("clueServlet", "action=bundActs&clueId=${clue.id}&" + param, function(data) {
					if(data.success) {
						alert("关联成功");
						$("#bundModal").modal("hide")
						showRelatedActivityList();
					} else{
						alert("关联失败");
						$("#bundModal").modal("hide")
					}
				},"json")
			}
		})




		//打开关联活动模态窗口
		//前端需要数据：数据库中所有 没绑定到此线索的 活动
		$("#openBundModal").click(function() {
			$.get("clueServlet",{
				"action":"getAllNotBundedActivities"
			}, function(data) {
				var html="";
				$.each(data, function (i,n) {
					html += '<tr>';
					html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
					html += '<td>'+n.name+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '<td>'+n.owner+'</td>';
					html += '</tr>';
				})
				$("#activitySearchBody").html(html);
			}, "json")
			$("#bundModal").modal("show");
		})


		//为aname绑定搜索操作
		//前端需要数据：符合查询条件的结果集（活动列表）
		//后端需要数据：查询条件（名称）
		$("#aname").keydown(function (event) {
			if (event.keyCode == 13){
				$.get("clueServlet", {
					"action": "getNotBundedActivitiesByName",

					"aname": $(this).val()
				}, function (data) {
					$("#activitySearchBody").empty();
					var html = "";
					$.each(data, function (i, n) {
						html += '<tr>';
						html += '<td><input type="checkbox" name="xz" value="' + n.id + '"/></td>';
						html += '<td>' + n.name + '</td>';
						html += '<td>' + n.startDate + '</td>';
						html += '<td>' + n.endDate + '</td>';
						html += '<td>' + n.owner + '</td>';
						html += '</tr>';
					})
					$("#activitySearchBody").html(html);
				}, "json")
			}
			return false
		})

		$("#aname").keydown(function (event) {
			return false
		})

		/**
		 * 修改线索备注
		 * 前端需要做什么：修改完后刷新备注列表
		 * 后端需要什么：当前线索的id，修改后的内容
		 */
		$("#updateRemarkBtn").click(function () {
			$.get("clueServlet",{
				"action":"editClueRemarkContentByRemarkId",

				"id":$("#clueRemarkId").val(),
				"noteContent":$("#noteContent").val()
			}, function (data) {
				if(data.success) {
					$("#editRemarkModal").modal("hide");
					showClueRemarkList();
				} else {
					alert("修改失败");
				}
			}, "json")
		})


		$("#remarkBody").on("mouseover",".remarkDiv",function(){
			$(this).children("div").children("div").show();
		})
		$("#remarkBody").on("mouseout",".remarkDiv",function(){
			$(this).children("div").children("div").hide();
		})

		//页面加载完毕即显示备注表
		showClueRemarkList();

		/**
		 * 添加备注
		 * 前端需要什么：保存成功标记
		 * 后端需要什么：线索备注信息，线索id
		 */
		$("#remarkSaveBtn").click(function () {
			$.get("clueServlet", {
				"action":"saveRemark",

				"clueId":"${clue.id}",
				"noteContent":$("#remark").val()
			}, function (data) {
				if(data.success) {
					showClueRemarkList()
				} else {
					alert("添加失败")
				}
			}, "json")
		})


		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});


		// //页面加载完毕后，取出关联的市场活动信息列表
		// showActivityList();

		showRelatedActivityList();
	});











	/**
	 * 解除市场活动的关联
	 * 前端需要什么：解除关联成功的标志
	 * 前端需要做什么：提示客户是否需要解除，如果解除失败，提示解除失败;成功则刷新列表
	 * 后端需要什么：关联的线索id和活动id
	 */
	function unbund(activityId) {
		if(confirm("你确定要解除此关联？")){
			$.get("clueServlet",{
				"action":"unbund",

				"clueId":"${clue.id}",
				"activityId":activityId
			},function (data) {
				if(data.success) {
					showRelatedActivityList();
				}
				else {
					alert("关联解除失败")
				}
			},"json")
		}
	}

	//前端需要：所有关联的活动列表（名称	开始日期	结束日期	所有者）
	//前端要做：铺上活动数据
	//后端需要：当前线索的id
	function showRelatedActivityList() {
		$.get("clueServlet", {
			"action":"getAllRelatedActsByClueId",

			"clueId":"${clue.id}"
		}, function (data) {
			var html = "";
			//data:act列表
			$.each(data, function (i, n) {
				html += '<tr>'
				html += '<td>'+n.name+'</td>'
				html += '<td>'+n.startDate+'</td>'
				html += '<td>'+n.endDate+'</td>'
				html += '<td>'+n.owner+'</td>'
				html += '<td><a href="javascript:void(0);" onclick="unbund(\'' +n.id+ '\')" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>'
				html += '</tr>'
			})
			$("#activityBody").html(html)
		}, "json")
	}

	/**
	 * 显示线索备注列表，每次页面加载后执行
	 * 前端需要什么：线索的备注列表（数组），线索对象
	 * 后端需要什么：当前线索的Id号，作为外键查找备注
	 */
	function showClueRemarkList() {
		$.get("clueServlet",{
			"action":"showClueRemarkList",

			"clueId":"${clue.id}"
		}, function (data) {
			var company = data.company;
			var clue = data.clue;
			//铺备注列表
			html = "";
			$.each(data.clueRemarkList, function (i, n) {
				html += ' <div name="firstRemarks" id="' + n.id + '" class="remarkDiv" style="height: 60px;">';
				html += '<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">'
				html += '<div style="position: relative; top: -40px; left: 40px;" >'
				//备注内容
				html += ' <h5>'+n.noteContent+'</h5>'
				//线索名字，称呼，公司，（创建时间，创建人\修改时间，修改人）
				html += ' <font color="gray">线索</font> <font color="gray">-</font> <b>'+clue.fullname+clue.appellation+'-'+clue.company+'</b> <small style="color: gray;"> '+ (n.editFlag==0 ? n.createTime + " 由" + n.createBy : n.editTime + " 由" + n.editBy) +'</small>'
				html += ' <div id="editAndDelRemarkBtn" style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">'
				html += ' <a class="myHref" href="javascript:void(0);"><span onclick="editClueRemark(\''+n.id+'\')"  id="edit-updateRemark" class="glyphicon glyphicon-edit" style="font-size: 20px; color: #ff0000;"></span></a>'
				html += ' &nbsp;&nbsp;&nbsp;&nbsp;'
				html += ' <a class="myHref" href="javascript:void(0);"><span onclick="removeClueRemark(\''+n.id+'\')" id="remove-delRemark" class="glyphicon glyphicon-remove" style="font-size: 20px; color: #ff0000;"></span></a>'
				html += ' </div>'
				html += ' </div>'
				html += ' </div>'
			})
			//将原来的备注表删除，再将后端返回的备注表添加即可，这样是防止线索表的异常叠加
			$("div[name=firstRemarks]").remove();
			$("#remarkDiv").before(html);
		}, "json")
	}

	/**
	 * 按备注id号删除备注
	 * 前端需要什么：删除成功与否标志
	 * 前端需要做什么：向用户确认是否要删除，删除成功后刷新列表
	 * 后端需要什么：备注id号
	 * 后端需要做什么：按删除备注
	 *
	 */
	function removeClueRemark(id) {
		var noteContent = $("#" + id +" h5").html();
		if(confirm("你确定要删除备注【"+noteContent+"】吗?")) {
			$.get("clueServlet",{
				"action":"removeClueRemarkByRemarkId",

				"id":id
			}, function (data) {
				if(data.success) {
					showClueRemarkList();
				} else {
					alert("修改失败");
				}
			}, "json")
		}
	}


	/**
	 * 打开修改备注的模态窗口
	 */
	function editClueRemark(id) {
		//将备注内容填入模态窗口
		var noteContent = $("#" + id +" h5").html();
		$("#noteContent").val(noteContent);
		//将线索id埋入隐藏域，方便修改
		$("#clueRemarkId").val(id);
		$("#editRemarkModal").modal("show");
	}

</script>

</head>
<body>

	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<%-- 备注的id --%>
		<input type="hidden" id="clueRemarkId">
		<div class="modal-dialog" role="document" style="width: 40%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改备注</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">内容</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="noteContent"></textarea>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 关联市场活动的模态窗口 -->
	<div class="modal fade" id="bundModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">关联市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="aname" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询123">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td><input type="checkbox"/></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="activitySearchBody">
							<%--<tr>
								<td><input type="checkbox"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="checkbox"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="bundBtn">关联</button>
				</div>
			</div>
		</div>
	</div>

    <!-- 修改线索的模态窗口 -->
    <div class="modal fade" id="editClueModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 90%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改线索</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">

                        <div class="form-group">
                            <label for="edit-clueOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <select class="form-control" id="edit-clueOwner">
                                    <option>zhangsan</option>
                                    <option>lisi</option>
                                    <option>wangwu</option>
                                </select>
                            </div>
                            <label for="edit-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-company" value="动力节点">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-call" class="col-sm-2 control-label">称呼</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <select class="form-control" id="edit-call">
                                    <option></option>
                                    <option selected>先生</option>
                                    <option>夫人</option>
                                    <option>女士</option>
                                    <option>博士</option>
                                    <option>教授</option>
                                </select>
                            </div>
                            <label for="edit-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-fullname" value="李四">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-job" class="col-sm-2 control-label">职位</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-job" value="CTO">
                            </div>
                            <label for="edit-email" class="col-sm-2 control-label">邮箱</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-email" value="lisi@bjpowernode.com">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-phone" value="010-84846003">
                            </div>
                            <label for="edit-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-website" value="http://www.bjpowernode.com">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-mphone" class="col-sm-2 control-label">手机</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-mphone" value="12345678901">
                            </div>
                            <label for="edit-status" class="col-sm-2 control-label">线索状态</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <select class="form-control" id="edit-status">
                                    <option></option>
                                    <option>试图联系</option>
                                    <option>将来联系</option>
                                    <option selected>已联系</option>
                                    <option>虚假线索</option>
                                    <option>丢失线索</option>
                                    <option>未联系</option>
                                    <option>需要条件</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-source" class="col-sm-2 control-label">线索来源</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <select class="form-control" id="edit-source">
                                    <option></option>
                                    <option selected>广告</option>
                                    <option>推销电话</option>
                                    <option>员工介绍</option>
                                    <option>外部介绍</option>
                                    <option>在线商场</option>
                                    <option>合作伙伴</option>
                                    <option>公开媒介</option>
                                    <option>销售邮件</option>
                                    <option>合作伙伴研讨会</option>
                                    <option>内部研讨会</option>
                                    <option>交易会</option>
                                    <option>web下载</option>
                                    <option>web调研</option>
                                    <option>聊天</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="edit-describe">这是一条线索的描述信息</textarea>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                        <div style="position: relative;top: 15px;">
                            <div class="form-group">
                                <label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="edit-contactSummary">这个线索即将被转换</textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control" id="edit-nextContactTime" value="2017-05-01">
                                </div>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address">北京大兴区大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
                    </form>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
                </div>
            </div>
        </div>
    </div>

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>${c.fullname}${c.appellation} <small>${c.company}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 500px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" onclick="window.location.href='workbench/clue/convert.jsp?id=${c.id}&fullname=${c.fullname}&appellation=${c.appellation}&company=${c.company}&owner=${c.owner}';"><span class="glyphicon glyphicon-retweet"></span> 转换</button>
			<button type="button" class="btn btn-default" data-toggle="modal" data-target="#editClueModal"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.fullname}${c.appellation}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${c.owner}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">公司</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.company}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">职位</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${c.job}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">邮箱</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.email}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">公司座机</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${c.phone}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">公司网站</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.website}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">手机</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${c.mphone}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">线索状态</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.state}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">线索来源</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${c.source}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${c.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${c.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${c.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${c.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 70px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${c.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 80px;">
			<div style="width: 300px; color: gray;">联系纪要</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${c.contactSummary}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 90px;">
			<div style="width: 300px; color: gray;">下次联系时间</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.nextContactTime}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px; "></div>
		</div>
        <div style="position: relative; left: 40px; height: 30px; top: 100px;">
            <div style="width: 300px; color: gray;">详细地址</div>
            <div style="width: 630px;position: relative; left: 200px; top: -20px;">
                <b>
					${c.address}
                </b>
            </div>
            <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
        </div>
	</div>

	<!-- 备注 -->
	<div id="remarkBody" style="position: relative; top: 40px; left: 40px;" >
		<div class="page-header">
			<h4>备注</h4>
		</div>

<%--		由showClueList铺数据--%>




		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button id="remarkSaveBtn" type="button" class="btn btn-primary">保存</button>
				</p>
			</form>
		</div>
	</div>
	
	<!-- 市场活动 -->
	<div>
		<div style="position: relative; top: 60px; left: 40px;">
			<div class="page-header">
				<h4>市场活动</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>名称</td>
							<td>开始日期</td>
							<td>结束日期</td>
							<td>所有者</td>
							<td></td>
						</tr>
					</thead>
					<tbody id="activityBody">









					</tbody>
				</table>
			</div>
			
			<div>
				<a href="javascript:void(0);" id="openBundModal" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>关联市场活动</a>
			</div>
		</div>
	</div>
	
	
	<div style="height: 200px;"></div>
</body>
</html>