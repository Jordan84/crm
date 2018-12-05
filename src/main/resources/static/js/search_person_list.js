/* 
 * nameInput：显示名字的input
 * hiddenInput：隐藏id的input  
 * dataUrl：接口地址       
 * 例如：creatSearchDiv("testInput","hiddenInput","test.json");
 * ajax接收的对象object{name:"",id:""}
 * 
 * */
function creatSearchDiv(nameInput, hiddenInput, dataUrl,top,left,succallback) {
	//获取input
	var newInputDiv = "#" + nameInput;
	var newHiddentInput = "#" + hiddenInput;
	
	var inputDivWidth = $(newInputDiv)[0].offsetWidth;
	//var inputDivHeight = $(newInputDiv).outerHeight();
	var inputDivLeft = $(newInputDiv).offset().left;
	var inputDivTop = $(newInputDiv).offset().top;
	console.log($(newInputDiv),inputDivWidth,$(newInputDiv)[0].offsetWidth)
	//获取位置
	var inputtop = top?top:0;
	var inputleft = left?left:0;
	//创建div和样式设置
	$(newInputDiv).parent().append('<div class="searchDiv"><ul></ul></div>');
	$(".searchDiv").css({ "width": inputDivWidth, "height": "auto", "margin-right":"15","background": "#ffffff", "z-index":"100","position": "absolute", "overflow-y": "hidden", "top": inputtop, "left": inputleft ,"display":"none"});
	$(".searchDiv ul").css("border","1px solid #cccccc");
	$(".searchDiv").attr("isShow", "true");
	$(".searchDiv ul").css({ "margin": 0, "padding": 0 })

	//input数值改变事件
	$(newInputDiv).on("input", function(event) {
		//正则
		if($.trim($(newInputDiv).val())==""){$(newHiddentInput).val("");$(newInputDiv).val("");succallback();return;}
		var pattern = /[^\u4e00-\u9fa5\w]+/i;
		if($(newInputDiv).val().match(pattern) != null) {
			$(newInputDiv).val($(newInputDiv).val().replace(pattern, ""));
		} else {
			var keyWords = $(newInputDiv).val();
			getData(keyWords);
		}
	});
	//input键盘enter事件
	$(newInputDiv).on("keydown", function(event) {
		if(event.keyCode == 13) {
			$(newInputDiv).val(subText($(".searchDiv ul li").eq(0).text()));
			$(newHiddentInput).val($(".searchDiv ul li").eq(0).attr("iname"));
			$(".searchDiv").css("display", "none");
		}
	});
	//input点击事件
	/*$(newInputDiv).click(function() {
		if($.trim($(newInputDiv).val()) != "") {
			var keyWords = $(newInputDiv).val();
			getData(keyWords);
		}else{
			$(newInputDiv).val("");
		}
	});*/
	function subText(str){
		return str;
		//return str.substring(0,(str.indexOf(" | ")==-1?str:(str.indexOf(" | "))));
	}
	//获取数据 
	function getData(key) {
		$(newHiddentInput).val("");		
		$.ajax({
			type: "post",
			url: dataUrl,
			async: true,
			data: {"search":key},
			dataType:'json',
			success: function(data) {
				$("#divLoginName").removeClass("has-error");
				$("#spanLoginName").removeClass("glyphicon-remove");
				$("#errorinfo").addClass("hidden");
				
				if(data.personList.length == 0 || data.personList.length == null) {
					//$(".searchDiv").css("display", "none");
					$("#divLoginName").addClass("has-error");
					$("#spanLoginName").addClass("glyphicon-remove");
					$("#errorinfo").removeClass("hidden");
					var htmlstr = "<li value = '0'>暂无数据</li>";
					$(".searchDiv ul").html(htmlstr)
				} else if(data.personList.length == 1) {
					//失去焦点的时候赋值
					$(".searchDiv").css("display", "block");
				} else {
					$(newInputDiv).unbind("blur");
					$(".searchDiv").css("display", "block");
					$(".searchDiv ul").empty();
				}
                 var htmlstr = "";
				$.each(data.personList, function(index, item) {
                    htmlstr += "<li data-ccName='"+item.ccName+"' data-sscName='"+item.sscName+"' data-ccId='"+item.ccId+"' data-sscId='"+item.sscId+"' data-loginName='"+item.loginName+"' value='" + item.id + "'>" + item.loginName + " | "+item.cnName+" | "+item.phone+"</li>";
				});
                $(".searchDiv ul").html(htmlstr);

				$(".searchDiv ul li").css({ "padding": "5px" });
				$(".searchDiv ul li").mouseover(function() {
					$(this).css("background", "#f0f0f0");
					$(this).css("cursor","pointer");
				});
				$(".searchDiv ul li").mouseout(function() {
					$(this).css("background", "#FFFFFF");
				});

				$(".searchDiv ul li").click(function() {
					if($(this).val() == 0 || $(this).val() == null){
						$(this).removeAttr('onclick');
						return;
					}
					var inputValue = subText($(this).attr("data-loginName"));
					$(newInputDiv).val(inputValue);
					$(".searchDiv").css("display", "none");
					$(newHiddentInput).val($(this).attr("iname"));
					$(newHiddentInput).attr("value",$(this).attr("value"));
					if($(this).attr("data-ccName") != null ||$(this).attr("data-ccName") != '' ){
						$("#cc").val($(this).attr("data-ccName")).attr("data-ccId",$(this).attr("data-ccId"));
					}
					$("#cc").attr("readonly","readonly");
					if($(this).attr("data-sscName") != null ||$(this).attr("data-sscName") != '' ){
						$("#ssc").val($(this).attr("data-sscName")).attr("data-sscId",$(this).attr("data-sscId"));
                    }
					$("#ssc").attr("readonly","readonly");
					// loadStudentInfo($(newHiddentInput).attr("value"));
					console.info($(newHiddentInput).attr("value"));
					succallback();
					//诸葛io拿到phone
					var num = $(this).index();
					// $("#zhugePhone").val((data.listPerson)[num].publicPhone);
				});
				//
			},
			error: function(err) {
				console.log(err)
			}
		});
	}
	//点击屏幕外取消
	$(window).click(function() {
		if($.trim($(newInputDiv).val())==""){
			$(".searchDiv").css("display", "none");
		}else if($.trim($(newHiddentInput).val())!=""){
			$(".searchDiv").css("display", "none");
		}
	})
};
