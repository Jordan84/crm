/**
 *@Author Terwer
 * @Date 2018-05-07
 */

// 手机号码验证
$.validator.addMethod("mobile", function (value, element) {
    var length = value.length;
    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
    return this.optional(element) || (length == 11 && mobile.test(value));
}, "请正确填写您的手机号码");

/**
 * 修复Jquery validate验证表单时多个name相同的元素只验证第一个的问题
 */
if ($.validator) {
    $.validator.prototype.elements = function () {
        var validator = this,
            rulesCache = {};
        // select all valid inputs inside the form (no submit or reset buttons)
        return $([]).add($(this.currentForm)
            .find("input, select, textarea")
            .not(":submit, :reset, :image, [disabled]")
            .not(this.settings.ignore)
            .filter(function () {
                if (!this.name && validator.settings.debug && window.console) {
                    console.error("%o has no name assigned", this);
                }
                //注释这行代码
                // select only the first element for each name, and only those with rules specified
                //if ( this.name in rulesCache || !validator.objectLength($(this).rules()) ) {
                //    return false;
                //}
                rulesCache[this.name] = true;
                return true;
            }));
    }
}