var DCROLE;
Ext.onReady(function() {
      var ss='';
      if (CFG_DEPLOYMENT_TYPE == 'DEV'){
        ss=" &nbsp;&nbsp;&nbsp;<font size=-2>&lt;UIROLE&gt;</div>"
        //ss="<span class='uiName'>UI0081</span>";
      }

      if (!Ext.isEmpty(Ext.get("app-loading"))) {
        Ext.get("app-loading").remove();
      }
      Ext.BLANK_IMAGE_URL = CFG_PATH_ICONS + "/s.gif";
      Ext.QuickTips.init();
      var bodyStyle = "background:#efeff3;";
      DCROLE = new Sys.BaseData.DCROLE({
            id : "DCROLE",
            autoScroll : true
          });
      var gui = new Ext.Viewport({
            layout : "border",
            style : bodyStyle,
            items : [new Ext.Panel({
                      id : "mainPanel",
                      region : "center",
                      layout : "fit",
                      border : false,
                      split : true,
                      bodyStyle : bodyStyle,
                      items : [DCROLE]
                    }), {
                  region : "south",
                  border : false,
                  minHeight : 1,
                  bodyStyle : bodyStyle,
                  split : true,
                  height : getWindowInnerHeight() - 600
                }, {
                  region : "east",
                  border : false,
                  minWidth : 1,
                  bodyStyle : bodyStyle,
                  split : true,
                  width : getWindowInnerWidth() - 500
                }, {
                  region : "north",
                  border : false,
                  html : "<div class='gui_title'>" + ("角色") + ss
                }]
          });
      gui.findById("DCROLE").executeQuery();
    });
