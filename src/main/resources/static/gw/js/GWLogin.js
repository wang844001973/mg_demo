// 签名结果
var signResult;

// 根据原文和证书产生认证数据包
function doDataProcess(initParam){
	
	// 证书版本者主题
	var signSubject = ""; //document.getElementById("rootCADN").value;
	
	// 验证认证原文是否为空
	if(original == ""){
		alert("认证原文不能为空!");
		return false;
	}else{
		// VCTK初始化参数，数据可从网关系统：认证管理->Key类型管理中导出
		//var initParam = "<\?xml version=\"1.0\" encoding=\"gb2312\"\?><authinfo><liblist><lib type=\"CSP\" version=\"1.0\" dllname=\"\" ><algid val=\"SHA1\" sm2_hashalg=\"sm3\"/></lib><lib type=\"SKF\" version=\"1.1\" dllname=\"SERfR01DQUlTLmRsbA==\" ><algid val=\"SHA1\" sm2_hashalg=\"sm3\"/></lib><lib type=\"SKF\" version=\"1.1\" dllname=\"U2h1dHRsZUNzcDExXzMwMDBHTS5kbGw=\" ><algid val=\"SHA1\" sm2_hashalg=\"sm3\"/></lib><lib type=\"SKF\" version=\"1.1\" dllname=\"U0tGQVBJLmRsbA==\" ><algid val=\"SHA1\" sm2_hashalg=\"sm3\"/></lib></liblist></authinfo>";
		// 调用网关工具脚本中的detachSignStr方法进行签名，返回签名结果
		// 参数说明：initParam：vctk控件初始化参数，authContent：认证原文，signSubject：证书版本者主题
		
		signResult = detachSignStr(initParam,original,signSubject);
		if(signResult){
			// 设置原文及签名结果
			document.getElementById("original").value = original;
			document.getElementById("signed_data").value = signResult;
			
			//判断是否是keyPing认证，是的话获取king的账户及ping值
			var pinCode = document.getElementById("pinCode").value;
			if('true'==pinCode){
				var pinId="";
				try{
					pinId = JIT_GW_ExtInterface.GetPinCode();
				}catch(e){}
				//alert(pinId);
				//设置pin码
				document.getElementById("key_pin").value =pinId;
			}
			// 页面提交，请求后台认证
			document.forms['certAuth'].submit();
		}else{
			return false;
		}
	}
}