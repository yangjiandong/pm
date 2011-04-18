Labels = {
  "/Application/Name":"spring + extjs 应用程序演示 ",
  "/Application/Version"                : "1.0",
  "/ActionSettings/ConfirmRemoveAction" : "Do you want to remove '{0}'?",
  "/Login/UserName"                     : "用户名",
  "/Login/Password"                     : "密码",
  "/Login/OK"                           : "确定",
  "/SessionMenu/LogOut"                 : "注销",
  "/SessionMenu/LockScrenn"             : "锁屏"
};
L = (function(lbl) {
  if (typeof Labels !== "undefined" && Labels[lbl]) {
    var args = Array.prototype.slice.call(arguments, 1);
    return Labels[lbl].replace(/\{(\d+)\}/g, function(s, iArg) {
          if (args[iArg] != undefined) {
            return args[iArg];
          } else {
            return s;
          }
        });
  } else {
    return lbl;
  }
});
Localization = {
  "language"     : "default",
  "langCode"     : "eng",
  "time"         : "pH:MM:ss",
  "shortDate"    : "yy-mm-dd",
  "extShortDate" : "y-m-d",
  "longDate"     : "yyyy't'm''d'�'",
  "firstWeekDay" : 0,
  "widthFactor"  : 1,
  "country"      : ""
};
L10N = ({
  langList   : {
    de        : {
      langCode     : "ger",
      time         : "HH:MM:ss",
      shortDate    : "dd.mm.yy",
      extShortDate : "d.m.y",
      longDate     : "d. mmmm yyyy",
      firstWeekDay : 1
    },
    en        : {
      langCode     : "eng",
      time         : "h:MM:ss tt",
      shortDate    : "mm/dd/yy",
      extShortDate : "m/d/y",
      longDate     : "mmmm d, yyyy",
      firstWeekDay : 0,
      widthFactor  : 1
    },
    'en-gb'   : {
      firstWeekDay : 1
    },
    'en-ca'   : {
      shortDate    : "dd/mm/yy",
      extShortDate : "d/m/y"
    },
    es        : {
      langCode     : "spa",
      time         : "HH:MM:ss",
      shortDate    : "dd/mm/yy",
      extShortDate : "d/m/y",
      longDate     : "d 'de' mmmm 'de' yyyy",
      firstWeekDay : 0,
      widthFactor  : 1.34
    },
    'es-es'   : {
      firstWeekDay : 1
    },
    fr        : {
      langCode     : "fre",
      time         : "HH:MM:ss",
      shortDate    : "dd/mm/yy",
      extShortDate : "d/m/y",
      longDate     : "d mmmm yyyy",
      firstWeekDay : 1,
      widthFactor  : 1.34
    },
    'fr-ca'   : {
      shortDate    : "yy-mm-dd",
      extShortDate : "y-m-d",
      firstWeekDay : 0
    },
    it        : {
      langCode     : "ita",
      time         : "HH:MM:ss",
      shortDate    : "dd/mm/yy",
      extShortDate : "d/m/y",
      longDate     : "dd mmmm yyyy",
      firstWeekDay : 1
    },
    ja        : {
      langCode     : "jap",
      time         : "HH:MM:ss",
      shortDate    : "yy/mm/dd",
      extShortDate : "y/m/d",
      longDate     : "yyyy'\u5E74'm'\u6708'd'\u65E5'",
      firstWeekDay : 0
    },
    nl        : {
      langCode     : "dut",
      time         : "HH:MM:ss",
      shortDate    : "dd/mm/yy",
      extShortDate : "d/m/y",
      longDate     : "d mmmm yyyy",
      firstWeekDay : 1
    },
    pl        : {
      langCode     : "pol",
      time         : "HH:MM:ss",
      shortDate    : "dd/mm/yy",
      extShortDate : "d/m/y",
      longDate     : "d mmmm yyyy",
      firstWeekDay : 1,
      widthFactor  : 1.34
    },
    pt        : {
      langCode     : "por",
      time         : "HH:MM:ss",
      shortDate    : "yy/mm/dd",
      extShortDate : "y/m/d",
      longDate     : "d 'de' mmmm 'de' yyyy",
      firstWeekDay : 1
    },
    'pt-br'   : {
      shortDate    : "dd/mm/yy",
      extShortDate : "d/m/y",
      firstWeekDay : 0
    },
    ru        : {
      langCode     : "rus",
      time         : "HH:MM:ss",
      shortDate    : "dd.mm.yy",
      extShortDate : "d.m.y",
      longDate     : "d mmmm yyyy",
      firstWeekDay : 1,
      widthFactor  : 1.34
    },
    zh        : {
      langCode     : "chi",
      time         : "pH:MM:ss",
      shortDate    : "yy-mm-dd",
      extShortDate : "y-m-d",
      longDate     : "yyyy'\u5E74'm'\u6708'd'\u65E5'",
      firstWeekDay : 0
    },
    'default' : {
      language     : "english",
      langCode     : "eng",
      time         : "H:MM:ss",
      shortDate    : "yy/mm/dd",
      extShortDate : "y/m/d",
      longDate     : "yyyy/mm/dd",
      firstWeekDay : 0,
      widthFactor  : 1
    }
  },
  langIDConv : {
    german           : "de",
    english          : "en",
    canada           : "ca",
    'united kingdom' : "gb",
    'united states'  : "us",
    french           : "fr",
    italian          : "it",
    japanese         : "ja",
    dutch            : "nl",
    portuguese       : "pt",
    brazil           : "br",
    spanish          : "es",
    spain            : "es",
    russian          : "ru",
    chinese          : "zh"
  },
  dayKeys    : ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
      "Friday", "Saturday"],
  monthKeys  : ["January", "February", "March", "April", "May", "June", "July",
      "August", "September", "October", "November", "December"],
  dayNames   : ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
      "Friday", "Saturday"],
  monthNames : ["January", "February", "March", "April", "May", "June", "July",
      "August", "September", "October", "November", "December"],
  masks      : {
    'default'   : "yyyy-mm-dd HH:MM:ss",
    shortDate   : "yy-mm-dd",
    longDate    : "yyyy'\u5E74'm'\u6708'd'\u65E5'",
    time        : "pH:MM:ss",
    isoDate     : "yyyy-mm-dd",
    isoTime     : "HH:MM:ss",
    isoDateTime : "yyyy-mm-dd HH:MM:ss"
  },
  init       : (function(localization) {
    for (var i in L10N.dayKeys) {
      L10N.dayNames[i] = L("/Misc/" + L10N.dayKeys[i]);
    }
    for (var i in L10N.monthKeys) {
      L10N.monthNames[i] = L("/Misc/" + L10N.monthKeys[i]);
    }
    for (var maskName in L10N.masks) {
      if (localization[maskName]) {
        L10N.masks[maskName] = localization[maskName];
      }
    }
  }),
  format     : (function(date, fmt) {
    var token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g;
    var pad = function(val, len) {
      val = String(val);
      while (val.length < len) {
        val = "0" + val;
      }
      return val;
    };
    var d = date.getDate();
    var D = date.getDay();
    var m = date.getMonth();
    var y = date.getFullYear();
    var H = date.getHours();
    var M = date.getMinutes();
    var s = date.getSeconds();
    var L = date.getMilliseconds();
    var flags = {
      d    : d,
      dd   : pad(d, 2),
      ddd  : L10N.dayNames[D].substring(0, 3),
      dddd : L10N.dayNames[D],
      m    : m + 1,
      mm   : pad(m + 1, 2),
      mmm  : L10N.monthNames[m].substring(0, 3),
      mmmm : L10N.monthNames[m],
      y    : y,
      yy   : String(y).slice(2),
      yyyy : y,
      h    : H % 12 || 12,
      hh   : pad(H % 12 || 12, 2),
      H    : H,
      HH   : pad(H, 2),
      M    : M,
      MM   : pad(M, 2),
      s    : s,
      ss   : pad(s, 2),
      l    : pad(L, 3),
      L    : pad(L > 99 ? Math.round(L / 10) : L, 2),
      t    : H < 12 ? "a" : "p",
      tt   : H < 12 ? "am" : "pm",
      T    : H < 12 ? "A" : "P",
      TT   : H < 12 ? "AM" : "PM"
    };
    return fmt.replace(/\{([^\}]*)\}/g, function(smask, mask) {
          if (L10N.masks[mask]) {
            mask = L10N.masks[mask];
          }
          return mask.replace(token, function(tk) {
                return tk in flags ? flags[tk] : tk.slice(1, tk.length - 1);
              });
        });
  }),
  time       : (function(value) {
    var unit = {
      hour : L("/Misc/Unit.h"),
      min  : L("/Misc/Unit.m"),
      sec  : L("/Misc/Unit.s")
    };
    var date = new Date(value * 1000);
    var h = Math.floor(value / 3600);
    var m = date.getMinutes();
    var s = date.getSeconds();
    var result = "";
    if (h > 0) {
      result += S("{0} {1} ", h, unit.hour);
    }
    if (m > 0 || h > 0) {
      result += S("{0} {1} ", String.leftPad(m, h > 0 ? 2 : 1, "0"), unit.min);
    }
    result += S("{0} {1}", String.leftPad(s, h + m > 0 ? 2 : 1, "0"), unit.sec);
    return result;
  })
});
L10N.init(Localization);
getEventLabel = (function(object, value) {
  var rootKey = S("/Event/{0}", object);
  var valKey = S("/Event/{0}/{1}", object, value);
  var varValue = {
    value : value
  };
  function formatMessage(label) {
    return label.replace(/\{(\w+)\}/g, function(s, fmt) {
          switch (fmt) {
            case "x" :
              return varValue[fmt];
            case "y" :
              return varValue[fmt];
            case "z" :
              return varValue[fmt];
            case "value" :
              return varValue.value;
            case "time" :
              return L10N.time(varValue.value);
            default :
              return s;
          }
        });
  }
  if (Labels[valKey]) {
    return formatMessage(Labels[valKey]);
  }
  if (Labels[rootKey]) {
    return formatMessage(Labels[rootKey]);
  }
  var varList = ["x", "y", "z"];
  var iVar = 0;
  rootKey = rootKey.replace(/\[(\d+)\]/g, function(s, i) {
        var varName = varList[iVar++];
        varValue[varName] = i;
        return "[" + varName + "]";
      });
  valKey = S("{0}/{1}", rootKey, value);
  if (Labels[valKey]) {
    return formatMessage(Labels[valKey]);
  }
  if (Labels[rootKey]) {
    return formatMessage(Labels[rootKey]);
  }
  return valKey;
})