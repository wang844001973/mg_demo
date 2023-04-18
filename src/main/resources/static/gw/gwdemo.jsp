<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
// 是否开启二维码认证
String QRCodeAuth = request.getAttribute("QRCodeAuth")==null?
		null:request.getAttribute("QRCodeAuth").toString();
%>
<script type="text/javascript" src="gw/js/pnxclient.js"></script>
<head>
	<title>模拟应用登录页面</title>
	<script type="text/javascript">
		// 如果未安装插件，则提示安装。
		try{
			JIT_GW_ExtInterface.GetVersion();
		}catch(e){
	        window.location.href = "gw/PNXClient.exe";
	    }
	
		// 是否开启二维码认证
		var qrCodeAuth = 'false';
        var original = '';
		function getori(){
            $.ajax({
				url:"jitGWRandom",
				type:"post",
                cache:false,
                dataType: "json",
				async: "false",
                success: function(result) {
                    if(result.QRCodeAuth=="false"){
                        document.getElementById("original").value = result.original;
                        document.getElementById("pinCode").value = result.pinCode;
                        original = result.original;
                        doDataProcess(initParam);
                    }else{
                        qrCodeAuth = result.QRCodeAuth;
                        generateQRCode();
                        document.getElementById("qr_div").style.display="";
                    }
                },
                error: function(e) {
				    alert(e.responseText);
                }
            });

        }
	</script>
</head>
<body>
<br>

<table width="100%" align="center">
	<tr align="center" valign="middle">
		<td>
			用户名：<input type="text" />
		</td>
	</tr>
	<tr align="center" valign="middle">
		<td>
			密&nbsp;&nbsp;&nbsp;&nbsp;码：<input type="text" />
		</td>
	</tr>
	<tr align="center" valign="middle">
		<td>
			<input type="submit" value="登录" />&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"   onclick="getori()"  value="认证" />
		</td>
	</tr>
</table>
<div id="qr_div" style="display:none;">
	<jsp:include page="qrCodeLogin.jsp"></jsp:include>
</div>
<jsp:include page="login.jsp"></jsp:include>
</body>