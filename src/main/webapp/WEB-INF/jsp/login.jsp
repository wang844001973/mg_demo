<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
	<script type="text/javascript" src="gw/js/jquery-1.10.2.min.js"></script>
	<script src="gw/js/GWUtil.js"></script>
	<script type="text/javascript" src="gw/js/GWLogin.js"></script>
</head>
<body>
	<div style="display:none;">
		<div>
			<form id="certAuth" name="certAuth" method="post" action="jitGWAuth">
				<!-- 隐藏字段 -->
				<!-- 认证方式 -->
				<input id="authMode" name="authMode" type="hidden" value="cert"/>
				<!-- 认证原文 -->
				<input type="hidden" id="original" name="original" />
				<!-- 签名结果 -->
				<input type="hidden" id="signed_data" name="signed_data" />
				<!-- pin码 -->
				<input type="hidden" id="key_pin" name="key_pin" />
				<!-- pin认证是否取用,默认false-->
				<input type="hidden" id="pinCode" name="pinCode"  value="false"/>
			</form>
		</div>
	</div>
	<script type="text/javascript">
		// 认证原文不为空时，自动弹出证书选择框
		// VCTK初始化参数，应用改造需修改,初始化参数可从网关系统：认证管理->Key类型管理中导出
		var initParam = "<\?xml version=\"1.0\" encoding=\"utf-8\"\?><authinfo><liblist><lib type=\"PM\" version=\"1.0\" dllname=\"Q3J5cHRPY3guZGxs\"><algid val=\"SHA1\" sm2_hashalg=\"SM3\" /></lib><lib type=\"CSP\" version=\"1.0\" dllname=\"TWljcm9zb2Z0IEVuaGFuY2VkIENyeXB0b2dyYXBoaWMgUHJvdmlkZXIgdjEuMA==\"><algid val=\"SHA1\" sm2_hashalg=\"SHA1\" /></lib><lib type=\"CSP\" version=\"1.0\" dllname=\"TWljcm9zb2Z0IFN0cm9uZyBDcnlwdG9ncmFwaGljIFByb3ZpZGVy\"><algid val=\"SHA1\" sm2_hashalg=\"SHA1\" /></lib><lib type=\"CSP\" version=\"1.0\" dllname=\"RW50ZXJTYWZlIGVQYXNzMzAwMyBDU1AgdjEuMA==\"><algid val=\"SHA1\" sm2_hashalg=\"SHA1\" /></lib><lib type=\"CSP\" version=\"1.0\" dllname=\"SklUIFVTQiBLZXkgQ1NQIHYxLjA=\"><algid val=\"SHA1\" sm2_hashalg=\"SHA1\" /></lib><lib type=\"CSP\" version=\"1.0\" dllname=\"RW50ZXJTYWZlIGVQYXNzMjAwMSBDU1AgdjEuMA==\"><algid val=\"SHA1\" sm2_hashalg=\"SHA1\" /></lib><lib type=\"CSP\" version=\"1.0\" dllname=\"SklUIFVTQiBLZXkzMDAzIENTUCB2MS4w\"><algid val=\"SHA1\" sm2_hashalg=\"SHA1\" /></lib><lib type=\"CSP\" version=\"1.0\" dllname=\"TWljcm9zb2Z0IEJhc2UgQ3J5cHRvZ3JhcGhpYyBQcm92aWRlciB2MS4w\"><algid val=\"SHA1\" sm2_hashalg=\"SHA1\" /></lib><lib type=\"CSP\" version=\"1.0\" dllname=\"RkVJVElBTiBlUGFzc05HIFJTQSBDcnlwdG9ncmFwaGljIFNlcnZpY2UgUHJvdmlkZXI=\"><algid val=\"SHA1\" sm2_hashalg=\"SHA1\" /></lib><lib type=\"CSP\" version=\"1.0\" dllname=\"RkVJVElBTiBlUGFzc05HIENTUCBGb3IgSklUM0sgVjEuMA==\"><algid val=\"SHA1\" sm2_hashalg=\"SHA1\" /></lib><lib type=\"SKF\" version=\"1.1\" dllname=\"U2h1dHRsZUNzcDExXzMwMDBHTS5kbGw=\"><algid val=\"SHA1\" sm2_hashalg=\"SM3\" /></lib></liblist></authinfo>";
	</script>
</body>
</html>
