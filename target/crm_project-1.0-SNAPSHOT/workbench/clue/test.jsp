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
							<input type="text" class="form-control" id="aname" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
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
					<tbody id="bundActivityBody">
					<%--					<tr>--%>
					<%--						<td><input type="checkbox"/></td>--%>
					<%--						<td>发传单</td>--%>
					<%--						<td>2020-10-10</td>--%>
					<%--						<td>2020-10-20</td>--%>
					<%--						<td>zhangsan</td>--%>
					<%--					</tr>--%>
					</tbody>
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<button type="button" class="btn btn-primary" data-dismiss="modal">关联</button>
			</div>
		</div>
	</div>
</div>


$.get("clueServlet", {
	"action":"showClueList",

	//以下参数来自查询框，可有可无
	"fullname":$("#search-fullname").val(),
	"company":$("#search-company").val(),
	"phone":$("#search-phone").val(),
	"source":$("#search-source").val(),
	"ownerName":$("#search-ownerName").val(),
	"mphone":$("#search-mphone").val(),
	"state":$("#search-state").val()
},function (data) {
	//data[{clue1}{clue1}{clue1}]

	console.log(data);

	var html = "";
	$.each(data, function (i,n) {
	//n是clue对象
	html += '<tr class="active">';
	html += '<td> <input type="checkbox" name="xz" value="'+n.id+'"/> </td>';
	html += '<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href=\'clueServlet?action=showDetail&id=' +n.id+ ' \';\">' +n.fullname+ '</a></td>';
	html += '<td>'+n.company+'</td>';
	html += '<td>'+n.phone+'</td>';
	html += '<td>'+n.mphone+'</td>';
	html += '<td>'+n.source+'</td>';
	html += '<td>'+n.owner+'</td>';
	html += '<td>'+n.state+'</td>';
	html += '</tr>';
	});

	// alert(html);
	//
	// console.log(html);

	$("#clueBody").html(html);
}, "json")
'<td><a href="javascript:void(0);" onclick="unbund(\''+ n.id +'\')" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>'
