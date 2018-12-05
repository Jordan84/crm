var eCrmCalendar = (function () {
	function _init (option) {
		_createEle(option)
		option.success()
	}
	function _createEle (option) {
		var weekArr = ['一', '二', '三', '四', '五', '六', '日']
		var year = isNaN(parseInt(option.year)) ? 0 : parseInt(option.year)
		var month = isNaN(parseInt(option.month)) ? 0 : parseInt(option.month)
		var daylength = null
		var firstDay = null
		var dom = document.getElementById(option.id)
		var eCrmCalendarStr = ''
		if (year === 0 || month === 0) {
			year = new Date().getFullYear()
			month = new Date().getMonth() + 1
		}
		firstDay = new Date(year + '-' + month + '-' + 1).getDay()
		daylength = parseInt((new Date(year + '-' + (month + 1)).getTime() - new Date(year + '-' + month).getTime()) / 3600 / 1000 / 24)
		eCrmCalendarStr = '<div class="eCrmCalendar">'
		eCrmCalendarStr += '<ul class="week">'
		for(item in weekArr) {
			eCrmCalendarStr += '<li>' + weekArr[item] + '</li>'
		}
		eCrmCalendarStr += '</ul>'
		eCrmCalendarStr += '<ul class="dateitem">'
		for(var item = 1; item < firstDay; item++) {
			eCrmCalendarStr += '<li></li>'
		}
		for(var item = 1; item <= daylength; item++) {
			eCrmCalendarStr += '<li>' + item + '</li>'
		}
		eCrmCalendarStr += '</ul></div>'
		dom.innerHTML = eCrmCalendarStr
	}
	return {
		init: _init
	}
})();
