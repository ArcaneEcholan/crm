<%@ page import="java.util.Map" %>
<%@ page import="com.wc.settings.domain.DicValue" %>
<%@ page import="java.util.List" %>
<%@ page import="com.wc.workbench.domain.Tran" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String base = request.getScheme()
+ "://"
+ request.getServerName()
+ ":"
+ request.getServerPort()
+ request.getContextPath()
+ "/";

    //阶段和可能性的对应关系
    Map<String, String> possibilityMap = (Map<String, String>)application.getAttribute("possibilityMap");

    //取出阶段列表
    List<DicValue> stages = (List<DicValue>)application.getAttribute("stage");

    //找出正常阶段和丢失阶段的分界点（第一个丢失阶段的分界点）
    int criticalPoint = 0;
    for(int i = 0; i < stages.size(); i++) {    //遍历阶段列表
        String value = stages.get(i).getValue();
        String possibility = possibilityMap.get(value);
        if("0".equals(possibility)) {       //如果阶段的可能性是0，则找到了第一个丢失的阶段，找到分界点
            criticalPoint = i;
            break;
        }
    }

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<base href="<%=base%>">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

<style type="text/css">
.mystage{
	font-size: 20px;
	vertical-align: middle;
	cursor: pointer;
}
.closingDate{
	font-size : 15px;
	cursor: pointer;
	vertical-align: middle;
}
</style>
	
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">


	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){




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
		
		
		//阶段提示框
		$(".mystage").popover({
            trigger:'manual',
            placement : 'bottom',
            html: 'true',
            animation: false
        }).on("mouseenter", function () {
                    var _this = this;
                    $(this).popover("show");
                    $(this).siblings(".popover").on("mouseleave", function () {
                        $(_this).popover('hide');
                    });
                }).on("mouseleave", function () {
                    var _this = this;
                    setTimeout(function () {
                        if (!$(".popover:hover").length) {
                            $(_this).popover("hide")
                        }
                    }, 100);
                });


        showHistoryList();


	});


    function changeIcon(stage, i){
        //要改变的阶段
        var currentStage = stage;
        //要改变的可能
        var currentPossibility = $("#possibility").html();
        //要改变阶段的下标
        var currentIndex = i;

        //阶段临界点
        var criticalPoint = "<%=criticalPoint%>";

        console.log(currentStage);
        console.log(currentPossibility);
        console.log(currentIndex);
        console.log(criticalPoint);

        //如果当前阶段是丢失阶段
        if(currentIndex >= criticalPoint) {
            //前面全部黑圈
            for(var i = 0; i < criticalPoint; i++) {
                $("#" + i).removeClass();
                $("#" + i).addClass("glyphicon glyphicon-record mystage");
                $("#" + i).css("color", "#000000");
            }
            //当前阶段红叉，否则黑叉
            for(var i = criticalPoint; i < "<%=stages.size()%>"; i++) {
                //红叉
                if(i == currentIndex) {
                    $("#" + i).removeClass();
                    $("#" + i).addClass("glyphicon glyphicon-remove mystage");
                    $("#" + i).css("color", "#ff0000");

                //黑叉
                }else {
                    $("#" + i).removeClass();
                    $("#" + i).addClass("glyphicon glyphicon-remove mystage");
                    $("#" + i).css("color", "#000000");
                }
            }
        }else {
            for(var i = 0; i < criticalPoint; i++) {
                //绿圈
                if(i < currentIndex) {
                    $("#" + i).removeClass();
                    $("#" + i).addClass("glyphicon glyphicon-ok-circle mystage");
                    $("#" + i).css("color", "#90F790");

                //标记
                }else if(i == currentIndex) {
                    $("#" + i).removeClass();
                    $("#" + i).addClass("glyphicon glyphicon-map-marker mystage");
                    $("#" + i).css("color", "#90F790");

                //黑圈
                }else {
                    $("#" + i).removeClass();
                    $("#" + i).addClass("glyphicon glyphicon-record mystage");
                    $("#" + i).css("color", "#000000");

                }
            }
            for(var i = criticalPoint; i < "<%=stages.size()%>"; i++) {
                //黑圈
                $("#" + i).removeClass();
                $("#" + i).addClass("glyphicon glyphicon-remove mystage");
                $("#" + i).css("color", "#000000");
            }
        }
    }

    /**
     * stage是要跳转到的阶段，i是要跳转到的阶段的索引
     * @param stage
     * @param i
     */
    function changeStage(stage, i) {
        //需要做的：
        //改变当前交易的数据库数据:当前阶段
        //生成一个交易历史记录：需要写入money和expectedDate
        $.get("tranServlet", {
            "action":"changeStage",

            "tranId":"${tran.id}",//用于改变交易记录
            "targetStage":stage,          //用于改变交易记录
            "money":$("#money").html(),                //用于生成交易历史
            "expectedDate":$("#expectedDate").html()   //用于生成交易历史
        },function (data) {
            //返回是否修改成功，并返回一个tran，刷新页面内容（当前阶段，当前可能性，修改人，修改时间）
            //data{"success":true/false, "tran":{}}

            if(data.success) {
                $("#stage").html(data.tran.stage);
                $("#possibility").html(data.tran.possibility);
                $("#editBy").html(data.tran.editBy);
                $("#editTime").html(data.tran.editTime);

                console.log(stage)
                console.log(i)
                changeIcon(stage, i);
            }else {
                alert("阶段修改失败");
            }

        }, "json")


    }

	function showHistoryList() {
	    console.log("showHistoryList()")
        $.get("tranServlet", {
            "action":"showTranHistoryList",
            "tranId":"${tran.id}"
        },function (data) {
            //data[{his1}{his1}{his1}]

            var html = "";
            $.each(data, function (i,n) {
                // 阶段	金额	可能性	预计成交日期	创建时间	创建人
                html += '<tr>';
                html += '<td>'+n.stage+'</td>';
                html += '<td>'+n.money+'</td>';
                html += '<td>'+n.possibility+'</td>';
                html += '<td>'+n.expectedDate+'</td>';
                html += '<td>'+n.createTime+'</td>';
                html += '<td>'+n.createBy+'</td>';
                html += '</tr>';
            })

            $("#historyBody").html(html);
        }, "json")
    }

</script>

</head>
<body>

<input type="hidden" id="tranId" value="${tran.id}">

<!-- 返回按钮 -->
<div style="position: relative; top: 35px; left: 10px;">
<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
</div>

<!-- 大标题 -->
<div style="position: relative; left: 40px; top: -30px;">
<div class="page-header">
<h3>${tran.customerId}-${tran.name} <small>￥${tran.money}</small></h3>
</div>
<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
<button type="button" class="btn btn-default" onclick="window.location.href='edit.jsp';"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
</div>
</div>

<!-- 阶段状态 -->
<div style="position: relative; left: 40px; top: -50px;">
阶段&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <%

        //准备当前阶段：
        String nowStage = ((Tran)request.getAttribute("tran")).getStage();      //当前阶段
        String nowPossibility = possibilityMap.get(nowStage);   //当前阶段可能性


        //获取当前阶段在所有阶段中的索引，用来和现在遍历阶段做比较
        int nowIndex = 0;
        for(int i = 0; i < stages.size(); i++) {
            String stage = stages.get(i).getValue();
            if(nowStage.equals(stage)) {
                nowIndex = i;
                break;
            }
        }
        System.out.println(nowIndex);
        System.out.println(criticalPoint);


        //如果当前阶段是丢失阶段，那么前7个一定是正常阶段，都是黑圈，后两个一个是红叉一个是黑叉
        if(nowIndex >= criticalPoint) {

            //遍历数据字典中所有的阶段，再根据当前阶段，展示所有的阶段
            for(int i = 0; i < stages.size(); i++) {
                //获取遍历阶段的名称
                String currentStage = stages.get(i).getValue();
                //获取遍历阶段的可能性
                String currentPossibility = possibilityMap.get(currentStage);


                //如果当前遍历到的阶段是丢失阶段，那么判断是否和当前阶段相同
                if(i >= criticalPoint) {
                    //如果相同，就输出红叉
                    if(i == nowIndex) {
    %>

        <span id="<%=i%>" class="glyphicon glyphicon-remove mystage" onclick="changeStage('<%=currentStage%>','<%=i%>')" data-toggle="popover" data-placement="bottom" data-content="<%=stages.get(i).getText()%>" style="color: #ff0000;"></span>
        -----------

    <%


                    //如果不同，输出黑叉
                    }else {
    %>

    <span id="<%=i%>" class="glyphicon glyphicon-remove mystage" onclick="changeStage('<%=currentStage%>','<%=i%>')" data-toggle="popover" data-placement="bottom" data-content="<%=stages.get(i).getText()%>" style="color: #000000;"></span>
    -----------

    <%

                    }

                //如果当前遍历到的阶段是正常，直接输出黑圈
                } else{
    %>

    <span id="<%=i%>" class="glyphicon glyphicon-record mystage" onclick="changeStage('<%=currentStage%>','<%=i%>')" data-toggle="popover" data-placement="bottom" data-content="<%=stages.get(i).getText()%>" style="color: #000000;"></span>
    -----------

    <%
                }
            }


        //如果当前阶段是正常阶段，那么当前阶段之前的是已完成的，当前阶段是正在进行的，后两个一个都是黑叉
        }else {
                    System.out.println("进入到else");

            //遍历数据字典中所有的阶段，再根据当前阶段，展示所有的阶段
            for(int i = 0; i < stages.size(); i++) {
                //获取遍历阶段的名称
                String currentStage = stages.get(i).getValue();
                //获取遍历阶段的可能性
                String currentPossibility = possibilityMap.get(currentStage);


                //如果遍历到的阶段是当前阶段，应该输出正在进行标志
                if(nowIndex == i) {
                    System.out.println("标记");
    %>

    <span id="<%=i%>" class="glyphicon glyphicon-map-marker mystage" onclick="changeStage('<%=currentStage%>','<%=i%>')" data-toggle="popover" data-placement="bottom" data-content="<%=stages.get(i).getText()%>" style="color: #90F790;"></span>
    -----------

    <%

                //如果遍历到的阶段在当前阶段之前，应该输出绿圈，表示已经完成
                }else if(i < nowIndex) {
                    System.out.println("绿圈");
    %>

    <span id="<%=i%>" class="glyphicon glyphicon-ok-circle mystage" onclick="changeStage('<%=currentStage%>','<%=i%>')" data-toggle="popover" data-placement="bottom" data-content="<%=stages.get(i).getText()%>" style="color: #90F790;"></span>
    -----------

    <%

                //如果遍历到的阶段在当前阶段之后,应该判断是否遍历到了丢失阶段
                }else {
                    //如果才遍历到正常阶段，显示黑圈即可
                    if(i < criticalPoint) {
                        System.out.println("黑圈");
    %>

    <span id="<%=i%>" class="glyphicon glyphicon-record mystage" onclick="changeStage('<%=currentStage%>','<%=i%>')" data-toggle="popover" data-placement="bottom" data-content="<%=stages.get(i).getText()%>" style="color: #000000;"></span>
    -----------

    <%

                    //如果遍历到丢失阶段，显示黑叉即可
                    } else {
                        System.out.println("黑叉");
    %>

    <span id="<%=i%>" class="glyphicon glyphicon-remove mystage" onclick="changeStage('<%=currentStage%>','<%=i%>')" data-toggle="popover" data-placement="bottom" data-content="<%=stages.get(i).getText()%>" style="color: #000000;"></span>
    -----------

    <%
                    }
                }
            }
        }




    %>

<%--<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="需求分析" style="color: #90F790;"></span>--%>
<%---------------%>
<%--<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="价值建议" style="color: #90F790;"></span>--%>
<%---------------%>
<%--<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="确定决策者" style="color: #90F790;"></span>--%>
<%---------------%>
<%--<span class="glyphicon glyphicon-map-marker mystage" data-toggle="popover" data-placement="bottom" data-content="提案/报价" style="color: #90F790;"></span>--%>
<%---------------%>
<%--<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="谈判/复审"></span>--%>
<%---------------%>
<%--<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="成交"></span>--%>
<%---------------%>
<%--<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="丢失的线索"></span>--%>
<%---------------%>
<%--<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="因竞争丢失关闭"></span>--%>
<%---------------%>
<span class="closingDate">2010-10-10</span>
</div>

<!-- 详细信息 -->
<div style="position: relative; top: 0px;">
<div style="position: relative; left: 40px; height: 30px;">
<div style="width: 300px; color: gray;">所有者</div>
<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.owner}</b></div>
<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">金额</div>
<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="money">${tran.money}</b></div>
<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
</div>
<div style="position: relative; left: 40px; height: 30px; top: 10px;">
<div style="width: 300px; color: gray;">名称</div>
<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.customerId}-${tran.name}</b></div>
<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">预计成交日期</div>
<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="expectedDate">${tran.expectedDate}</b></div>
<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
</div>
<div style="position: relative; left: 40px; height: 30px; top: 20px;">
<div style="width: 300px; color: gray;">客户名称</div>
<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.customerId}</b></div>
<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">阶段</div>
<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="stage">${tran.stage}</b></div>
<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
</div>
<div style="position: relative; left: 40px; height: 30px; top: 30px;">
<div style="width: 300px; color: gray;">类型</div>
<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.type}</b></div>
<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">可能性</div>
<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b  id="possibility">${possibility}</b></div>
<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
</div>
<div style="position: relative; left: 40px; height: 30px; top: 40px;">
<div style="width: 300px; color: gray;">来源</div>
<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.source}</b></div>
<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">市场活动源</div>
<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${tran.activityId}</b></div>
<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
</div>
<div style="position: relative; left: 40px; height: 30px; top: 50px;">
<div style="width: 300px; color: gray;">联系人名称</div>
<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${tran.contactsId}</b></div>
<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
</div>
<div style="position: relative; left: 40px; height: 30px; top: 60px;">
<div style="width: 300px; color: gray;">创建者</div>
<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${tran.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${tran.createTime}</small></div>
<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
</div>
<div style="position: relative; left: 40px; height: 30px; top: 70px;">
<div style="width: 300px; color: gray;">修改者</div>
<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b id="editBy">${tran.editBy}&nbsp;&nbsp;</b><small id="editTime" style="font-size: 10px; color: gray;">${tran.editTime}</small></div>
<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
</div>
<div style="position: relative; left: 40px; height: 30px; top: 80px;">
<div style="width: 300px; color: gray;">描述</div>
<div style="width: 630px;position: relative; left: 200px; top: -20px;">
    <b>
        ${tran.description}
    </b>
</div>
<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
</div>
<div style="position: relative; left: 40px; height: 30px; top: 90px;">
<div style="width: 300px; color: gray;">联系纪要</div>
<div style="width: 630px;position: relative; left: 200px; top: -20px;">
    <b>
        &nbsp;${tran.contactSummary}
    </b>
</div>
<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
</div>
<div style="position: relative; left: 40px; height: 30px; top: 100px;">
<div style="width: 300px; color: gray;">下次联系时间</div>
<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>&nbsp;${tran.nextContactTime}</b></div>
<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
</div>
</div>

<!-- 备注 -->
<div style="position: relative; top: 100px; left: 40px;">
<div class="page-header">
<h4>备注</h4>
</div>

<!-- 备注1 -->
<div class="remarkDiv" style="height: 60px;">
<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
<div style="position: relative; top: -40px; left: 40px;" >
    <h5>哎呦！</h5>
    <font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
    <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
        <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
    </div>
</div>
</div>

<!-- 备注2 -->
<div class="remarkDiv" style="height: 60px;">
<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
<div style="position: relative; top: -40px; left: 40px;" >
    <h5>呵呵！</h5>
    <font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
    <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
        <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
    </div>
</div>
</div>

<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
<form role="form" style="position: relative;top: 10px; left: 10px;">
    <textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
    <p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
        <button id="cancelBtn" type="button" class="btn btn-default">取消</button>
        <button type="button" class="btn btn-primary">保存</button>
    </p>
</form>
</div>
</div>

<!-- 阶段历史 -->
<div>
<div style="position: relative; top: 100px; left: 40px;">
<div class="page-header">
    <h4>阶段历史</h4>
</div>
<div style="position: relative;top: 0px;">
    <table id="activityTable" class="table table-hover" style="width: 900px;">
        <thead>
            <tr style="color: #B3B3B3;">
                <td>阶段</td>
                <td>金额</td>
                <td>可能性</td>
                <td>预计成交日期</td>
                <td>创建时间</td>
                <td>创建人</td>
            </tr>
        </thead>
        <tbody id="historyBody">




        </tbody>
    </table>
</div>

</div>
</div>

<div style="height: 200px;"></div>

</body>
</html>