// 根据原文和证书产生认证数据包
// 参数说明：initParam：vctk控件初始化参数
// 参数说明：authContent：认证原文
// 参数说明：signSubject：证书版发者主题
function detachSignStr(initParam,authContent,signSubject){
	// 验证认证原文不能为空
	if(authContent == ""){
		alert("认证原文不能为空!");
		return false;
	}else{
		try{
			JIT_GW_ExtInterface.GetVersion();
		}catch(e){
            alert("未安装控件，请进行安装控件");
			return false;
        }
		try{
			JIT_GW_ExtInterface.ClearFilter();
			
			// 初始化vctk控件
		    JIT_GW_ExtInterface.Initialize("",initParam);
			// 控制证书为一个时，不弹出证书选择框
			JIT_GW_ExtInterface.SetChooseSingleCert(1);
			// 同DN情况下优先使用sm2证书
			JIT_GW_ExtInterface.AddFilter(11,"2");
			// 设置是否强制弹出PIN码
			var pinCode = document.getElementById("pinCode").value;
			if("true"== pinCode){
				//加捕获异常的方法为兼容之前老版本插件，如果是老版本插件跳过此方法
				try{
					JIT_GW_ExtInterface.SetForcePinDialog(1);
				}catch(e){
					
				}
			}
		}catch(e){
            alert("调用方法失败："+JIT_GW_ExtInterface.GetLastErrorMessage());
			return false;
        }
		 // 生成签名信息 
		var sign_Result = "";
		try{
			sign_Result = JIT_GW_ExtInterface.P7SignString(authContent,true,true);
		}catch(e){
            alert("生成签名信息失败：" + JIT_GW_ExtInterface.GetLastErrorMessage());
			return false;
        }
		if(JIT_GW_ExtInterface.GetLastError() !=0){
			if (JIT_GW_ExtInterface.GetLastError() == 3758096386
					|| JIT_GW_ExtInterface.GetLastError() == 2148532334 || JIT_GW_ExtInterface.GetLastError() == 3758096385){
				alert("用户取消操作");
				return;
			}else if (JIT_GW_ExtInterface.GetLastError() == -536870815
					|| JIT_GW_ExtInterface.GetLastError() == 3758096481) {
				alert("没有找到有效的证书，如果使用的是KEY，请确认已经插入key");
				return;
			}else{
				alert(JIT_GW_ExtInterface.GetLastErrorMessage());
				return;
			}
		}
		 // 返回签名结果
		return sign_Result;
		
	}
}