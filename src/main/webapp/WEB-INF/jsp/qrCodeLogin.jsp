<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
	<script type="text/javascript" src="gw/js/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src="gw/js/GWqrCodeLogin.js"></script>
</head>
<body>
	<div>
		<div>
			<form id="QRAuth" name="QRAuth" method="post" action="jitGWAuth">
				<!-- 隐藏字段 -->
				<!-- 认证方式 -->
				<input id="authMode" name="authMode" type="hidden" value="QRCode">
				<!-- token -->
				<input id="token" name="token" type="hidden">
				 
				<div>
					<div id="qrcode-box">
						<div>
							<img id="QRCode" alt="QRCode" src="qrcode"/>
						</div>
					</div>
					<div id="qrcode-succ" style="display:none">
						<h4>扫描成功！</h4>
							请勿刷新本页面，按手机提示操作！
					</div>
					<div id="qrcode-msg" style="display:none">
						<h4>二维码过期</h4>
						请<a href="javascript:generateQRCode();">刷新</a>二维码后重新扫描
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
