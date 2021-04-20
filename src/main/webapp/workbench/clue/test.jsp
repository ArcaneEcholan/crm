<!-- 备注 -->
<div style="position: relative; top: 40px; left: 40px;">
	<div class="page-header">
		<h4>备注</h4>
	</div>

	<!-- 备注1 -->
	<div class="remarkDiv" style="height: 60px;">
		<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
		<div style="position: relative; top: -40px; left: 40px;" >
			<h5>哎呦！</h5>
			<font color="gray">线索</font> <font color="gray">-</font> <b>李四先生-动力节点</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
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
				<button id="remarkSaveBtn" type="button" class="btn btn-primary">保存</button>
			</p>
		</form>
	</div>
</div>