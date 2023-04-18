var JIT_GW_ExtInterface = function(){

	var currentSessionWSURL = "";
	var useActioveX = false;
	var useNPPlugin = false;

	/**
     * simple polyfill Object.assign for IE <= 11
     * @see https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/assign
     * @param {Object} target - target options
     * @param {Object} options - new options
     */
    var extendOptions = function (target, options) {
        if (typeof Object.assign === 'function') {
            Object.assign(target, options)
        } else {
            // for IE < 11
            for (var key in options) {
                target[key] = options[key]
            }
        }
    }

    var options = {};

	var checkSessionWSURL = function ()
	{
		if( currentSessionWSURL=="" )
		{
			if ("https:" == document.location.protocol)
			{
				currentSessionWSURL = SendAndWaitMessage("https://127.0.0.1:10087/", "QueryService");	
			}
			else
			{
				currentSessionWSURL = SendAndWaitMessage("http://127.0.0.1:10086/", "QueryService");	
			}
		}
	}

	// 是否是IE
	var isUseActioveX = function ()
	{
		if (!!window.ActiveXObject || "ActiveXObject" in window)
		{
			//try
			//{
			//	var ax = new ActiveXObject("PNXClient.PNXDataTrans");
			//	useActioveX = true;
			//}
			//catch(e)
			//{
			//	useActioveX = false;   
			//}
			useActioveX = true;
		}
		else
		{
			useActioveX = false;
		}
	}
	
	// 是否使用NPPlugin
	var isUseNPPlugin = function()
	{
		checkSessionWSURL();
		if ("{\"value\":\"\"}" == currentSessionWSURL)
		{
			useNPPlugin = true;
		}
		else
		{
			useNPPlugin = false;
		}
	}

	var addActioveX = function(){
		document.write("<object classid='clsid:9DD991F7-6FB0-4004-95A4-0A55006A8C42' width='0' height='0' id='PNXGWClient'></object>");
	}
	
	var addNPPlugin = function(){
		document.write("<embed type='application/x-jit-auth-plugin' id='PNXGWClient' width='0' height='0'></embed>");
	}

	var SendAndWaitMessageEx = function (operatorCmd, sendMsg)
	{
		checkSessionWSURL();
		var strSendMsg = operatorCmd + ":" + sendMsg;
		
		return SendAndWaitMessage(currentSessionWSURL, strSendMsg);
	}

	var SendAndWaitMessage = function (wsurl, sendMsg)
	{
		var ResultMsg = "{\"value\":\"\"}";
		
		if( ResultMsg == wsurl )
		{
			return ;
		}
		try{
			var globalXmlHttpRequest = new XMLHttpRequest();
			globalXmlHttpRequest.open("POST", wsurl, false);
			globalXmlHttpRequest.setRequestHeader("Content-Type", "text/plain;charset=UTF-8");
			globalXmlHttpRequest.send(sendMsg);
			ResultMsg = globalXmlHttpRequest.responseText;
		}catch(e){
			currentSessionWSURL = "";
			if (options.onError) {
                options.onError.call(undefined, e);
            }
		}
		return ResultMsg;
	}

	return {
		Config: function(extendOption) {
            if (extendOption && typeof extendOption === 'object') {
                extendOptions(options, extendOption)
            }
        },
		Init: function()
		{
			isUseActioveX();
			if (useActioveX) {
				addActioveX();
			}
			else
			{
				isUseNPPlugin();
				if (useNPPlugin){
					addNPPlugin();
				}	
			}
		},
		// 功能：返回客户端的版本 HRESULT GetClientVersion([out,retval] BSTR* bstrClientVersion);
		GetClientVersion: function()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GetClientVersion();
			}
			else
			{
				var result = JSON.parse(SendAndWaitMessageEx("GetClientVersion", ""));
				return result.value;
			}
		},

		// 功能：下载客户端程序并安装 HRESULT SetupClient([in] BSTR strURL, [in] BSTR strHashValue, [in] BOOL bSync, [in] BOOL bOnlySSO, [out,retval] LONG* lRetVal);
		SetupClient: function (bstrURL, bstrHashValue, bSync, bOnlySSO)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.SetupClient(bstrURL, bstrHashValue, bSync, bOnlySSO);
			}
			else
			{	
				var jsonstr = {"strURL":bstrURL,"strHashValue":bstrHashValue,"bSync":bSync,"bOnlySSO":bOnlySSO};
				var result = JSON.parse(SendAndWaitMessageEx("SetupClient", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：获取硬件指纹信息 HRESULT GetFinger([in] ULONG dwSign, [in] BSTR strGateWayIP, [out,retval] BSTR* bstrFinger);
		GetFinger: function (dwSign, strGateWayIP)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GetFinger(dwSign, strGateWayIP);
			}
			else
			{
				var jsonstr = {"dwSign":dwSign, "strGateWayIP":strGateWayIP};
				var result = JSON.parse(SendAndWaitMessageEx("GetFinger", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：设置转发策略和代填策略 HRESULT SetPolicy([in] BSTR strProxyPolicy, [in] BSTR strSSOPolicy, [in] BSTR strGatewayAddress, [out,retval] LONG* lRetVal);
		SetPolicy:function (strProxyPolicy, strSSOPolicy, strGatewayAddress)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.SetPolicy(strProxyPolicy, strSSOPolicy, strGatewayAddress);
			}
			else
			{
				var jsonstr = {"strProxyPolicy":strProxyPolicy, "strSSOPolicy":strSSOPolicy, "strGatewayAddress":strGatewayAddress};
				var result = JSON.parse(SendAndWaitMessageEx("SetPolicy", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：启动认证信息模块 HRESULT GetAuthToken([in] BSTR strFileName,[in] BSTR strXmlData,[out,retval] BSTR* bstrInfo);
		GetAuthToken: function (strFileName, strXmlData)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GetAuthToken(strFileName, strXmlData);
			}
			else
			{
				var jsonstr = {"strFileName":strFileName, "strXmlData":strXmlData};
				var result = JSON.parse(SendAndWaitMessageEx("GetAuthToken", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：打开BS应用 HRESULT NavigateURL([in] BSTR strUrl, [out,retval] LONG* lRetVal);
		NavigateURL: function (strUrl)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.NavigateURL(strUrl);
			}
			else
			{
				var jsonstr = {"strUrl":strUrl};
				var result = JSON.parse(SendAndWaitMessageEx("NavigateURL", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：打开CS应用 HRESULT ExecuteCS([in] BSTR strGatewayAddress, [in] BSTR strAppFlag, [in] BSTR strAppPath, [out,retval] LONG* lRetVal);
		ExecuteCS: function (strGatewayAddress, strAppFlag, strAppPath)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.ExecuteCS(strGatewayAddress, strAppFlag, strAppPath);
			}
			else
			{
				var jsonstr = {"strGatewayAddress":strGatewayAddress, "strAppFlag":strAppFlag, "strAppPath":strAppPath};
				var result = JSON.parse(SendAndWaitMessageEx("ExecuteCS", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：设置自动更新策略 HRESULT SetUpdatePolicy([in] BSTR strGatewayAddress, [in] SHORT nGatewayPort, [in] BSTR strUserToken, [in] ULONG ulUpdateDelay, [out, retval] LONG* lRetVal);
		SetUpdatePolicy: function (strGatewayAddress, nGatewayPort, strUserToken, updatedelay)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.SetUpdatePolicy(strGatewayAddress, nGatewayPort, strUserToken, updatedelay);
			}
			else
			{
				var jsonstr = {"strGatewayAddress":strGatewayAddress, "nGatewayPort":nGatewayPort, "strUserToken":strUserToken, "ulUpdateDelay":updatedelay};
				var result = JSON.parse(SendAndWaitMessageEx("SetUpdatePolicy", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：获取客户端IP HRESULT GetClientIP([in] BSTR strGatewayIP, [out, retval] BSTR* RetVal);
		GetClientIP: function (strGatewayIP)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GetClientIP(strGatewayIP);
			}
			else
			{
				var jsonstr = {"strGatewayIP":strGatewayIP};
				var result = JSON.parse(SendAndWaitMessageEx("GetClientIP", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：登出网关 HRESULT GWLogout([in] BSTR strServerIP, [out, retval] LONG* lRetVal);
		GWLogout: function (strServerIP)
		{	
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GWLogout(strServerIP);
			}
			else
			{
				var jsonstr = {"strServerIP":strServerIP};
				var result = JSON.parse(SendAndWaitMessageEx("GWLogout", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：运行安装包: 0 非静默安装，1 静默安装，2 同步安装，4 异步安装 HRESULT RunSetup([in] LONG lRunType, [in] BOOL bIsOnlySSO, [out, retval] LONG* lRetVal);
		RunSetup: function (lRunType, bIsOnlySSO)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.RunSetup(lRunType, bIsOnlySSO);
			}
			else
			{
				var jsonstr = {"lRunType":lRunType, "bIsOnlySSO":bIsOnlySSO};
				var result = JSON.parse(SendAndWaitMessageEx("RunSetup", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：获取下载进度 HRESULT GetDownloadProgress([out, retval] LONG* lRetVal);
		GetDownloadProgress: function ()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GetDownloadProgress();
			}
			else
			{
				var jsonstr = "";	
				var result = JSON.parse(SendAndWaitMessageEx("GetDownloadProgress", jsonstr));
				return result.value;
			}
		},

		// 功能：是否安装完成 HRESULT IsInstallComplete([out, retval] BOOL* lRetVal);
		IsInstallComplete: function ()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.IsInstallComplete();
			}
			else
			{
				var jsonstr = "";	
				var result = JSON.parse(SendAndWaitMessageEx("IsInstallComplete", jsonstr));
				return result.value;
			}
		},

		// 功能：初始化签包对象 HRESULT Initialize([in] BSTR strAlgType, [in] BSTR strAuxParam, [out,retval] LONG* Result);
		Initialize: function (strAlgType, strAuxParam)
		{
			if(useActioveX|| useNPPlugin )
			{
				return PNXGWClient.Initialize(strAlgType, strAuxParam);
			}
			else
			{
				var jsonstr = { "strAlgType":strAlgType, "strAuxParam":strAuxParam };
				var result = JSON.parse(SendAndWaitMessageEx("Initialize", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：设置摘要算法 HRESULT SetDigestAlg([in] BSTR strDigestAlg, [out,retval] LONG* Result);
		SetDigestAlg: function (strDigestAlg)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.SetDigestAlg(strDigestAlg);
			}
			else
			{
				var jsonstr = {"strDigestAlg":strDigestAlg};
				var result = JSON.parse(SendAndWaitMessageEx("SetDigestAlg", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：设置单证书是否弹出对话框 HRESULT SetChooseSingleCert([in] ULONG isChoose, [out,retval] LONG* Result);
		SetChooseSingleCert: function (isChoose)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.SetChooseSingleCert(isChoose);
			}
			else
			{
				var jsonstr = {"isChoose":isChoose};
				var result = JSON.parse(SendAndWaitMessageEx("SetChooseSingleCert", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：添加证书过滤条件 HRESULT AddFilter([in] ULONG ulType, [in] BSTR strValue, [out,retval] LONG* Result);
		AddFilter: function (ulType, strValue)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.AddFilter(ulType, strValue);
			}
			else
			{
				var jsonstr = {"ulType":ulType, "strValue":strValue};
				var result = JSON.parse(SendAndWaitMessageEx("AddFilter", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：清除所有过滤条件 HRESULT ClearFilter([out,retval] LONG* Result);
		ClearFilter: function ()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.ClearFilter();
			}
			else
			{
				var jsonstr = "";	
				var result = JSON.parse(SendAndWaitMessageEx("ClearFilter", jsonstr));
				return result.value;
			}
		},

		// 功能：P1签名 HRESULT P1Sign([in] BSTR strValueBase64, [out,retval] BSTR* Result);
		P1Sign: function (strValueBase64)
		{
			if(useActioveX|| useNPPlugin )
			{
				return PNXGWClient.P1Sign(strValueBase64);
			}
			else
			{
				var jsonstr = {"strValueBase64":strValueBase64};
				var result = JSON.parse(SendAndWaitMessageEx("P1Sign", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：P1签名字符串 HRESULT P1SignString([in] BSTR strValue, [out,retval] BSTR* Result);
		P1SignString: function (strValue)
		{	
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.P1SignString(strValue);
			}
			else
			{
				var jsonstr = {"strValue":strValue};
				var result = JSON.parse(SendAndWaitMessageEx("P1SignString", JSON.stringify(jsonstr)));
				return result.value;
			}
		},
		 
		// 功能：P7签名 HRESULT P7Sign([in] BSTR strValueBase64,[in] VARIANT_BOOL isDetach,[in] VARIANT_BOOL isIncludeCert, [out,retval] BSTR* Result);
		P7Sign: function (strValueBase64, isDetach, isIncludeCert)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.P7Sign(strValueBase64, isDetach, isIncludeCert);
			}
			else
			{
				var jsonstr = {"strValueBase64":strValueBase64, "isDetach":isDetach, "isIncludeCert":isIncludeCert};
				var result = JSON.parse(SendAndWaitMessageEx("P7Sign", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：P7签名字符串 HRESULT P7SignString([in] BSTR strValue,[in] VARIANT_BOOL isDetach,[in] VARIANT_BOOL isIncludeCert, [out,retval] BSTR* Result);
		P7SignString: function (strValue, isDetach, isIncludeCert)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.P7SignString(strValue, isDetach, isIncludeCert);
			}
			else
			{
				var jsonstr = {"strValue":strValue, "isDetach":isDetach, "isIncludeCert":isIncludeCert};
				var result = JSON.parse(SendAndWaitMessageEx("P7SignString", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：释放签名对象 HRESULT Finalize([out,retval] LONG* Result);
		Finalize: function ()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.Finalize();
			}
			else
			{
				var jsonstr = "";	
				var result = JSON.parse(SendAndWaitMessageEx("Finalize", jsonstr));
				return result.value;
			}
		},

		// 功能：获取签名版本 HRESULT GetSignVersion([out,retval] BSTR* Result);
		GetSignVersion: function ()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GetSignVersion();
			}
			else
			{
				var jsonstr = "";	
				var result = JSON.parse(SendAndWaitMessageEx("GetSignVersion", jsonstr));
				return result.value;
			}
		},

		// 功能：获取签名证书 HRESULT GetSignCert([out,retval] BSTR* RetCert);
		GetSignCert: function ()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GetSignCert();
			}
			else
			{
				var jsonstr = "";	
				var result = JSON.parse(SendAndWaitMessageEx("GetSignCert", jsonstr));
				return result.value;
			}
		},

		// 功能：获取错误码 HRESULT GetLastError([out,retval] ULONG* RetLong);
		GetLastError: function ()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GetLastError();
			}
			else
			{
				var jsonstr = "";	
				var result = JSON.parse(SendAndWaitMessageEx("GetLastError", jsonstr));
				return result.value;
			}
		},

		// 功能：获取错误信息 HRESULT GetLastErrorMessage([out,retval] BSTR* RetStr);
		GetLastErrorMessage: function ()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GetLastErrorMessage();
			}
			else
			{
				var jsonstr = "";	
				var result = JSON.parse(SendAndWaitMessageEx("GetLastErrorMessage", jsonstr));
				return result.value;
			}
		},

		// 功能：销毁认证对象 HRESULT DestoryAuth([out,retval] LONG* lRetVal);
		DestoryAuth: function ()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.DestoryAuth();
			}
			else
			{
				var jsonstr = "";	
				var result = JSON.parse(SendAndWaitMessageEx("DestoryAuth", jsonstr));
				return result.value;
			}
		},

		// 功能：获取证书类型 HRESULT GetSignCertType([out,retval] BSTR* RetCertType);
		GetSignCertType: function ()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GetSignCertType();
			}
			else
			{
				var jsonstr = "";	
				var result = JSON.parse(SendAndWaitMessageEx("GetSignCertType", jsonstr));
				return result.value;
			}
		},

		// 功能：获取签名摘要算法 HRESULT GetSignHash([out,retval] BSTR* RetSignHash);
		GetSignHash: function ()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GetSignHash();
			}
			else
			{
				var jsonstr = "";	
				var result = JSON.parse(SendAndWaitMessageEx("GetSignHash", jsonstr));
				return result.value;
			}
		},

		// 功能：使能调用序列 HRESULT CallQueueEnable([in] BOOL bCallQueueEnable, [in] BOOL bHeadInfoEnable, [out,retval] LONG* lRetVal);
		CallQueueEnable: function (bCallQueueEnable, bHeadInfoEnable)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.CallQueueEnable(bCallQueueEnable, bHeadInfoEnable);
			}
			else
			{
				var jsonstr = {"bCallQueueEnable":bCallQueueEnable, "bHeadInfoEnable":bHeadInfoEnable};
				var result = JSON.parse(SendAndWaitMessageEx("CallQueueEnable", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：清空调用序列结果 HRESULT CallQueueClear([out,retval] LONG* lRetVal);
		CallQueueClear: function ()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.CallQueueClear();
			}
			else
			{
				var jsonstr = "";	
				var result = JSON.parse(SendAndWaitMessageEx("CallQueueClear", jsonstr));
				return result.value;
			}
		},

		// 功能：获取调用序列结果 HRESULT CallQueueGet([out,retval] BSTR* bstrRetVal);
		CallQueueGet: function ()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.CallQueueGet();
			}
			else
			{
				var jsonstr = "";	
				var result = JSON.parse(SendAndWaitMessageEx("CallQueueGet", jsonstr));
				return result.value;
			}
		},

		// 功能：客户端安全策略检查 HRESULT DoClientSecurityCheck([in] BSTR strGatewayAddress, [in] SHORT usGatewayPort, [in] BSTR strSecurityPolicys, [out,retval] BOOL* RetVal);
		DoClientSecurityCheck: function (strGatewayAddress, usGatewayPort, strSecurityPolicys)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.DoClientSecurityCheck(strGatewayAddress, usGatewayPort, strSecurityPolicys);
			}
			else
			{
				var jsonstr = {"strGatewayAddress":strGatewayAddress, "usGatewayPort":usGatewayPort, "strSecurityPolicys":strSecurityPolicys};
				var result = JSON.parse(SendAndWaitMessageEx("DoClientSecurityCheck", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：获取通讯协议版本 HRESULT GetProtocolVersion([out,retval] BSTR* strProtocolVersion);
		GetProtocolVersion: function ()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GetProtocolVersion();
			}
			else
			{
				var jsonstr = "";	
				var result = JSON.parse(SendAndWaitMessageEx("GetProtocolVersion", jsonstr));
				return result.value;
			}
		},

		// 功能：修改Key的Pin码 HRESULT ChangePinCode([in] BSTR strCertSn, [in] LONG lPinCodeType, [in] BSTR strCurPinCode, [in] BSTR strNewPinCode, [out,retval] LONG *pRet);
		ChangePinCode: function (strCertSn, lPinCodeType, strCurPinCode, strNewPinCode)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.ChangePinCode(strCertSn, lPinCodeType, strCurPinCode, strNewPinCode);
			}
			else
			{
				var jsonstr = {"strCertSn":strCertSn, "lPinCodeType":lPinCodeType, "strCurPinCode":strCurPinCode, "strNewPinCode":strNewPinCode};
				var result = JSON.parse(SendAndWaitMessageEx("ChangePinCode", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：等待策略设置完毕 HRESULT WaitSetPolicyFinish([in] BSTR strGatewayAddress, [out,retval] LONG* lRetVal);
		WaitSetPolicyFinish: function (strGatewayAddress)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.WaitSetPolicyFinish(strGatewayAddress);
			}
			else
			{
				var jsonstr = {"strGatewayAddress":strGatewayAddress};
				var result = JSON.parse(SendAndWaitMessageEx("WaitSetPolicyFinish", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：获取会话Token HRESULT GetSessionToken([in] BSTR strGatewayAddress, [out,retval] BSTR* bstrToken);
		GetSessionToken: function (strGatewayAddress)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GetSessionToken(strGatewayAddress);
			}
			else
			{
				var jsonstr = {"strGatewayAddress":strGatewayAddress};
				var result = JSON.parse(SendAndWaitMessageEx("GetSessionToken", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：获取控件版本 HRESULT GetVersion([out,retval] BSTR* bstrRetVal);
		GetVersion: function ()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GetVersion();
			}
			else
			{
				var jsonstr = "";	
				var result = JSON.parse(SendAndWaitMessageEx("GetVersion", jsonstr));
				return result.value;
			}
		},

		// 功能：设置语言资源 HRESULT SetLanguage([in] BSTR strLanguage, [out,retval] LONG* Result);
		SetLanguage: function (strLanguage)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.SetLanguage(strLanguage);
			}
			else
			{
				var jsonstr = {"strLanguage":strLanguage};
				var result = JSON.parse(SendAndWaitMessageEx("SetLanguage", JSON.stringify(jsonstr)));
				return result.value;
			}
		},

		// 功能：获取用户属性 HRESULT GetAttribute([in] BSTR strGatewayAddress, [in] BSTR strAppFlag, [in] BSTR strAttributeName, [out, retval] BSTR* attributevalue);
		GetAttribute: function (strGatewayAddress, strAppFlag, strAttributeName)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GetAttribute(strGatewayAddress, strAppFlag, strAttributeName);
			}
			else
			{
				var jsonstr = {"strGatewayAddress":strGatewayAddress, "strAppFlag":strAppFlag, "strAttributeName":strAttributeName};
				var result = JSON.parse(SendAndWaitMessageEx("GetAttribute", JSON.stringify(jsonstr)));
				return result.value;
			}
		},
		
		//功能：获取签名加密PIN码 HRESULT GetPinCode([out,retval] BSTR* Result);
		GetPinCode: function ()
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.GetPinCode();
			}
			else
			{
				var jsonstr = "";	
				var result = JSON.parse(SendAndWaitMessageEx("GetPinCode", JSON.stringify(jsonstr)));
				return result.value;				
			}
		},
		
		//功能：设置强制弹出PIN码框 HRESULT SetForcePinDialog([in] ULONG isForcePinDialog, [out,retval] LONG* Result);
		SetForcePinDialog: function (isForcePinDialog)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.SetForcePinDialog(isForcePinDialog);
			}
			else
			{
				var jsonstr = {"isForcePinDialog":isForcePinDialog};
				var result = JSON.parse(SendAndWaitMessageEx("SetForcePinDialog", JSON.stringify(jsonstr)));
				return result.value;				
			}			
		},
		
		//功能：返回报文认证属性 HRESULT MessageAuth([in] BSTR bstrGatewayAddress, [in] BSTR bstrAttributeName, [in] BSTR bstrAppID,[out,retval] BSTR* bstrAttributeValue);
		MessageAuth: function (bstrGatewayAddress, bstrAttributeName, bstrAppID)
		{
			if( useActioveX || useNPPlugin )
			{
				return PNXGWClient.MessageAuth(bstrGatewayAddress, bstrAttributeName, bstrAppID);
			}
			else
			{
				var jsonstr = {"bstrGatewayAddress":bstrGatewayAddress, "bstrAttributeName":bstrAttributeName, "bstrAppID":bstrAppID};
				var result = JSON.parse(SendAndWaitMessageEx("MessageAuth", JSON.stringify(jsonstr)));
				return result.value;				
			}
		}
	}
}();

(function(){
	JIT_GW_ExtInterface.Init();
})();