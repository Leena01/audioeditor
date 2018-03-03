

// Absolute Live Support .NET
// Copyright(c) XIGLA SOFTWARE
// http://www.xigla.com 

function xlaALSprecheck_93844(){
	if ((document.body && typeof(document.body.innerHTML)) != 'undefined'){
		xlaALSrequest_93844();
	} else {
		alert('Your browser does not support several features of this system.\nPlease upgrade to a newer version');
	}
}
function xlaALSrequest_93844(){
	var w = 640, h = 480;
	if (document.all || document.layers) {
	   w = screen.availWidth;
	   h = screen.availHeight;
	}
	var leftPos = (w-520)/2, topPos = (h-380)/2;
	xlaALSwindow=window.open("https://ls.scholarships.at/UserPreChat.aspx?ref=https%3a%2f%2fceepus.info%2fdocument%2fMobReport%2fmobreport_edit.aspx&d=1&u=&bypass=","ALSRoom","toolbar=0,location=0,status=0,menubar=0,scrollbars=1,resizable=1,width=520,height=380,top=" + topPos + ",left=" + leftPos);
	xlaALSwindow.focus();
}

document.write("<a href='javascript:;' target=_self onclick='javascript:xlaALSprecheck_93844()'><img src='https:\/\/ls.scholarships.at\/files\/offline.gif' border=0 id='xlaALSstatusimg_93844'><\/a>");


function xlaALScheckstatus_93844(){
	var nt=String(Math.random()).substr(2,10);
	document.getElementById("xlaALSstatusimg_93844").src='https://ls.scholarships.at/als.aspx?u=&d=1&getstatus=1&nt=' + nt;
	setTimeout("xlaALScheckstatus_93844();", 20000);
}

xlaALScheckstatus_93844();
