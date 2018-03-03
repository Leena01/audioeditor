var timer = 0;
var intervalMin = 30; // in minuten

function LiveWindow(nurl, nwidth, nheight, sbar, mbar, tbar) {
    wndLiveWindow = window.open(nurl, "wndLiveWindow", "width=" + nwidth + ",height=" + nheight + ",toolbar=" + tbar + ",location=0,directories=0,status=0,scrollbars=" + sbar + ",menubar=" + mbar + ",resizable=1");
    wndLiveWindow.focus();
}

function OpenWindow(nurl, nwidth, nheight, sbar, mbar, tbar) {
    wndWindow = window.open(nurl, "wndWindow", "width=" + nwidth + ",height=" + nheight + ",toolbar=" + tbar + ",location=0,directories=0,status=0,scrollbars=" + sbar + ",menubar=" + mbar + ",resizable=1");
    wndWindow.focus();
}

function OpenNewWindow(nurl, nwidth, nheight, sbar, mbar, tbar) {
    wndNewWindow = window.open(nurl, "wndNewWindow", "width=" + nwidth + ",height=" + nheight + ",toolbar=" + tbar + ",location=0,directories=0,status=0,scrollbars=" + sbar + ",menubar=" + mbar + ",resizable=1");
    wndNewWindow.focus();
}

function OpenViewlet(nurl, nwidth, nheight, sbar, mbar, tbar) {
    wndViewlet = window.open(nurl, "wndViewlet", "width=" + nwidth + ",height=" + nheight + ",toolbar=" + tbar + ",location=0,directories=0,status=0,scrollbars=" + sbar + ",menubar=" + mbar + ",resizable=1");
    wndViewlet.focus();
}

function ShowExport(pUrl) {
    htmlWindow = window.open(pUrl);
    return false;
}

function css_browser_selector(u) { var ua = u.toLowerCase(), is = function (t) { return ua.indexOf(t) > -1; }, g = 'gecko', w = 'webkit', s = 'safari', o = 'opera', h = document.documentElement, b = [(!(/opera|webtv/i.test(ua)) && /msie\s(\d)/.test(ua)) ? ('ie ie' + RegExp.$1) : is('firefox/2') ? g + ' ff2' : is('firefox/3.5') ? g + ' ff3 ff3_5' : is('firefox/3') ? g + ' ff3' : is('gecko/') ? g : is('opera') ? o + (/version\/(\d+)/.test(ua) ? ' ' + o + RegExp.$1 : (/opera(\s|\/)(\d+)/.test(ua) ? ' ' + o + RegExp.$2 : '')) : is('konqueror') ? 'konqueror' : is('chrome') ? w + ' chrome' : is('iron') ? w + ' iron' : is('applewebkit/') ? w + ' ' + s + (/version\/(\d+)/.test(ua) ? ' ' + s + RegExp.$1 : '') : is('mozilla/') ? g : '', is('j2me') ? 'mobile' : is('iphone') ? 'iphone' : is('ipod') ? 'ipod' : is('mac') ? 'mac' : is('darwin') ? 'mac' : is('webtv') ? 'webtv' : is('win') ? 'win' : is('freebsd') ? 'freebsd' : (is('x11') || is('linux')) ? 'linux' : '', 'js']; c = b.join(' '); h.className += ' ' + c; return c; }; css_browser_selector(navigator.userAgent);

function Logout(pRootUrl) {
    var fbText = 'Do you really want to log out?';
    if (!confirm(fbText)) {
        return;
    }
    else {
        window.location = pRootUrl + '/login/logout.aspx';
    }
}

window.location.hash = "no-back-button";
window.location.hash = "Again-No-back-button";//again because google chrome don't insert first hash into history
window.onhashchange = function () { window.location.hash = "no-back-button"; }

$(document).ready(function () {
    set_interval();
    $(this)
    //.mousemove(reset_interval)
    .mousedown(reset_interval)
    .keypress(reset_interval);
    //.scroll(reset_interval);
    if (navigator.appName != "Microsoft Internet Explorer") {
        history.pushState({ page: 1 }, "title 1", "#nbb");
        window.onhashchange = function (event) {
            window.location.hash = "nbb";
        };
    }
});

function set_interval() {

    var localDisableAutologout = false;

    // Überprft ob das Autologout disabled ist
    if (typeof disableAutoLogout != 'undefined') {
        if (disableAutoLogout) {
            localDisableAutologout = true;
        }
    }

    // Das Template bzw das Hidden Field overruled immer die Variable
    if ($('input#DisableAutoLogout') != null) {
        if ($('input#DisableAutoLogout').val() == "1") {
            localDisableAutologout = true;
        }
        else {
            localDisableAutologout = false;
        }
    }

    // Das Template bzw das Hidden Field overruled immer die Variable
    if ($('input#AutoLogoutIntervalMin') != null) {
        if ($('input#AutoLogoutIntervalMin').val() != "" && !isNaN($('input#AutoLogoutIntervalMin').val())) {
            intervalMin = parseInt($('input#AutoLogoutIntervalMin').val());
        }
    }

    if (intervalMin == 0) {
        localDisableAutologout = true;
    }

    // Falls das Override gesetz ist dann diese verwenden
    if (typeof disableAutoLogoutOverrideTemplate != 'undefined') {
        if (disableAutoLogoutOverrideTemplate) {
            localDisableAutologout = true;
        }
        else {
            localDisableAutologout = false;
        }
    }

    if (!localDisableAutologout) {
        timer = setInterval("auto_logout()", 1000 * 60 * intervalMin);

        $(this).unbind("mousedown keypress");
        $(this).bind("mousedown keypress", reset_interval);
    }
}

function reset_interval() {
    clearInterval(timer);
    set_interval();
}

function auto_logout() {
    if (IsloggedIn == 1) {
        window.location = RootUrl + "/login/logout_timeout.aspx";
    }
}

function OpenValidate() {
    $.fn.prettyPhoto({ modal: true, social_tools: false, default_height: 315, default_width: 500 });
    $.prettyPhoto.open(RootUrl + '/login/ValidateEmailAddress.aspx?custom=true&amp;iframe=true&amp;width=400px&amp;height=345', '', '');
    window.parent.$('.pp_close').css('display', 'none');
}