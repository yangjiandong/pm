
/*
Object.prototype.hasOwnProperty = function(prop) {
  for (var k in this) {
    alert(k);

  }

}
  */
  
//Javascript中各种trim的实现
//http://justjavac.iteye.com/blog/933093
//据说是最快的实现
function trim(str){
    str = str.replace(/^(\s|\u00A0)+/,'');
    for(var i=str.length-1; i>=0; i--){
        if(/\S/.test(str.charAt(i))){
            str = str.substring(0, i+1);
            break;
        }
    }
    return str;
}

//String.prototype.trim = function() {
//     //return this.replace(/[(^\s+)(\s+$)]/g,"");//會把字符串中間的空白符也去掉
//     //return this.replace(/^\s+|\s+$/g,""); //
//     return this.replace(/^\s+/g,"").replace(/\s+$/g,"");
//}
////motools
//function trim(str){
//    return str.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '');
//}
////jquery
//function trim(str){
//    return str.replace(/^(\s|\u00A0)+/,'').replace(/(\s|\u00A0)+$/,'');
//}

function getCurUser() {
  var name;
//        AppSysAction.doEcho('', function(result, e){
//          var t = e.getTransaction();
//          if(e.status){
//              name=result;
//              //alert('test:' + name);
//          }else{
//          }
//
//      });
  return name;
}

function getWindowInnerHeight() {
  var myWidth = 0, myHeight = 0;
  if( typeof( window.innerWidth ) == 'number' ) {
    //Non-IE
    myWidth = window.innerWidth;
    myHeight = window.innerHeight;
  } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
    //IE 6+ in 'standards compliant mode'
    myWidth = document.documentElement.clientWidth;
    myHeight = document.documentElement.clientHeight;
  } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
    //IE 4 compatible
    myWidth = document.body.clientWidth;
    myHeight = document.body.clientHeight;
  }
  return myHeight;
}

function getWindowInnerWidth() {
  var myWidth = 0, myHeight = 0;
  if( typeof( window.innerWidth ) == 'number' ) {
    //Non-IE
    myWidth = window.innerWidth;
    myHeight = window.innerHeight;
  } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
    //IE 6+ in 'standards compliant mode'
    myWidth = document.documentElement.clientWidth;
    myHeight = document.documentElement.clientHeight;
  } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
    //IE 4 compatible
    myWidth = document.body.clientWidth;
    myHeight = document.body.clientHeight;
  }
  return myWidth;
}

function find_window(p_name) {
  return Ext.getCmp(p_name);
}
function show_window(p_name) {
  return Ext.getCmp(p_name).show();
}
function hide_window(p_name) {
  return Ext.getCmp(p_name).hide();
}
function close_window(p_name) {
  return Ext.getCmp(p_name).close();
}

function show_message(p_message, p_type) {
  Ext.Msg.alert(p_type||'Error',p_message );
}

function createCookie(name,value,days) {
  if (days) {
    var date = new Date();
    date.setTime(date.getTime()+(days*24*60*60*1000));
    var expires = "; expires="+date.toGMTString();
  }
  else var expires = "";
  document.cookie = name+"="+value+expires+"; path=/";
}

function readCookie(name) {
  var nameEQ = name + "=";
  var ca = document.cookie.split(';');
  for(var i=0;i < ca.length;i++) {
    var c = ca[i];
    while (c.charAt(0)==' ') c = c.substring(1,c.length);
    if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
  }
  return null;
}

function eraseCookie(name) {
  createCookie(name,"",-1);
}



/**
 *  Decode a string which has been urlencoded
 */
function urldecode ( str ) {
  var ret = str;
  ret = ret.replace(/\+/g, "%20");
  ret = decodeURIComponent(ret);
  ret = ret.toString();
  return ret;
}



Ext.form.TextField.prototype.caseRestriction = 'Mixed'; //Upper, Lower

Ext.form.TextField.prototype.getValue = function() {
   var v = Ext.form.TextField.superclass.getValue.call(this);
   if (this.caseRestriction == "Upper" ) {
     return v.toUpperCase();
   }else if (this.caseRestriction == "Lower" ) {
     return v.toLowerCase();
   }else
    return v;
}