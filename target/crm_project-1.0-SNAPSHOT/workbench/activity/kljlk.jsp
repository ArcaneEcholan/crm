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