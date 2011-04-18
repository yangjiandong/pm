// +-------------------------------------------------------------------------+
// | JavaScript                                                              |
// +-------------------------------------------------------------------------+
// | Copyright (c) 2010 -  yangjiandong CO., LTD.                            |
// +-------------------------------------------------------------------------+
// | LICENSE: This library is free software; you can redistribute it and/or  |
// |          modify it under the terms of the GNU Lesser General Public     |
// |          License as published by the Free Software Foundation; either   |
// |          version 3 of the License, or (at your option) any later        |
// |          version.                                                       |
// |          See the GNU Lesser General Public License for more details.    |
// |          Details have been described to the LICENSE file.               |
// +-------------------------------------------------------------------------+
// | Authors: yang jiandong <young.jiandong@gmail.com>                       |
// +-------------------------------------------------------------------------+
//
// $Id: main.js 1441 2009-04-14 02:17:46Z yangjiandong $
//上午09:43:29
// @version       $Revision: 1.1 $  $Date: 2006/07/18 05:56:31 $

var logInWindow, loggedIn;
var appViewport;
var mainViewportLoaded = false;
var mTlb;
var menuTree, shortcutTree, contentPane;
var subSysMenu;
var uiMsgQue;
var uiDcInc;

var AUTH_COOKIE_NAME = 'ext_spring_loggedInCookie';
var session;

Ext.onReady(function() {
  Ext.BLANK_IMAGE_URL = CFG_PATH_ICONS + "/s.gif";

  uiMsgQue = new Ext.util.MixedCollection();
  session = new Ext.util.MixedCollection();
  uiDcInc = new Ext.util.MixedCollection();
  initUiDcIncludes(uiDcInc);
  // session.add("language", CFG_DEFAULT_LANGUAGE);

  loggedIn = (readCookie(AUTH_COOKIE_NAME) == null) ? false : true;
  logInWindow = new App.dc.DcLogin({
        authServerUrl : CFG_AUTHSERVER_URL
      });
  logInWindow.addListener('logonSuccess', function() {
        onLogonSuccess();
      });

  if (!Ext.isEmpty(Ext.get('app-loading'))) {
    Ext.get('app-loading').remove();
  }

  if (!loggedIn) {
    logInWindow.show();
    setTimeout('logInWindow.focusUserName()', 200);
  } else {
    showApp();
  }

  function onLogonSuccess() {
    loggedIn = true;
    createCookie(AUTH_COOKIE_NAME, '1');
    logInWindow.hide();
    showApp();
  }

  function showApp() {
    if (!mainViewportLoaded) {
      createToolbar();
      createMenuTree();
      initAppViewport();
      getCurUserInfos();
    }
    document.getElementById("content_iframe").src = "help/showAbout";
  }

  function initAppViewport() {
    contentPane = new Ext.TabPanel({
          region : 'center',
          deferredRender : false,
          activeTab : 0,
          plain : true,
          items : [{
                contentEl : 'content',
                title : '欢迎',
                autoScroll : true,
                layout : 'fit'
              }]
        });

    appViewport = new Ext.Viewport({
          layout : 'border',
          items : [{
                region : "north",
                height : 76,
                items : [mTlb]
              }, new Ext.Panel({
                    layout : 'accordion',
                    region : 'west',
                    split : true,
                    width : 220,
                    title : '菜单',
                    minSize : 175,
                    maxSize : 300,
                    collapsible : true,
                    items : [{
                          title : '应用程序菜单',
                          layout : 'fit',
                          border : false,
                          items : [menuTree]
                        }, {
                          title : '其他',
                          border : false,
                          html : ""
                        }]
                  }), contentPane, {
                region : "south",
                border : false,
                frame : true,
                height : 25,
                html : "版权copyRight2009-"
              }, {
                region : "east",
                border : false,
                width : 0
              }],
          listeners : {
            render : function() {
              Ext.Ajax.request({
                    url : 'resource/loadSubSystem',
                    success : successFn
                  })
            }
          }
        }); // end viewport
  }

  function createMenuTree() {
    menuTree = new N21.Other.MenuTree();
    menuTree.on("openMenuLink", function(guiID, guiText, params) {
          openMenuLink(guiID, guiText, params);
        })
    // menuTree.on("openMenuLinkInNewTab", function(guiID, guiText, params) {
    // openMenuLinkInNewTab(guiID, guiText, params);
    // })

    // TODO
    // menuTree.getLoader().on("loadexception", function(loader, node, response)
    // {
    // alert(loader);
    // onBackendSessionExpired();
    // });
  }

  // viewport 渲染后，加载子系统菜单项
  function successFn(response) {
    var data = Ext.decode(response.responseText);
    if (data != null && data.success) {
      subSys = data.subSystems;
      for (var i = 0; i < subSys.length; i++) {
        var menuCode = subSys[i].id;
        var menuName = subSys[i].resourceName;

        subSysMenu.add({
              id : 'menu-' + menuCode,
              code : menuCode,
              text : menuName,
              handler : loadModuleMenu
            });
      }
    }
  }

  /**
   * Called whenever a NO_ACTIVE_SESSION error is returned by the server
   */

  function onBackendSessionExpired() {
    // alert(1);
    eraseCookie(AUTH_COOKIE_NAME);
    logInWindow.show();
  }

  /**
   * Load a UI url into a content tab. Params: ifrID - Id of the iframe in
   * tabpanel tab where the UI is loaded guiID - UI code params - extra params
   */
  function loadMenuLink(ifrID, guiID, params) {
    if (guiID.substr(0, 3) == "REP") {
      document.getElementById(ifrID).src = CFG_BACKENDSERVER_URL
          + "?_p_report_id=" + guiID;
    } else {
      var htmlString = buildHtml(session.get("language"), guiID, uiDcInc
              .get(guiID));
      // alert(htmlString);
      window.frames[ifrID].document.open("text/html", "replace");
      window.frames[ifrID].document.write(htmlString);
      window.frames[ifrID].document.close();
    }
  }

  /**
   * Open a menu-link. It is called by the menu tree onclick event handler. If
   * UI is already loaded but tab is not active then activate tab. If UI is
   * already loaded and tab is active then reload UI. If UI is not loaded,
   * create a new tab load UI into it and activate it.
   */
  function openMenuLink(guiID, guiText, params) {
    var tabID = "tabPanel_" + params.menuid;
    var ifrID = "iframe_" + params.menuid;
    _openMenuLinkImpl(tabID, ifrID, guiID, guiText, params);
  }

  /**
   * Open a menu-link in a new tab. Creates a new tab and load the UI regardless
   * of how many tabs are opened with the same UI
   *
   */
  function openMenuLinkInNewTab(guiID, guiText, params) {
    var tabID = "tabPanel_" + params.menuid + "_" + (new Date()).getTime();
    var ifrID = "iframe_" + params.menuid + "_" + (new Date()).getTime();
    _openMenuLinkImpl(tabID, ifrID, guiID, guiText, params);
  }

  /**
   * Implements the UI loading mechanism.
   */
  function _openMenuLinkImpl(tabID, ifrID, guiID, guiText, params) {
    //alert(ifrID+' => window.frames='+window.frames[ifrID]);
    //alert(ifrID+' => document.getElementById'+document.getElementById(ifrID));
    if (Ext.isEmpty(document.getElementById(ifrID))
        && !Ext.isEmpty(window.frames[ifrID])) {
      // alert('am frames no docgetElemById');
      delete window.frames[ifrID];
    }
    // alert(ifrID+' => window.frames='+window.frames[ifrID]);
    if (!Ext.isEmpty(document.getElementById(ifrID))) {
      if (contentPane.getActiveTab().getId() == tabID) {
        loadMenuLink(ifrID, guiID, params);
      } else {
        contentPane.activate(tabID);
      }
    } else {
      contentPane.add(new Ext.Panel({
        title : guiText,
        id : tabID,
        autoScroll : true,
        layout : 'fit',
        closable : true,
        html : '<div style="width:100%;height:100%;overflow: hidden;" id="div_'
            + params.menuid
            + '" ><iframe id="'
            + ifrID
            + '" name="'
            + ifrID
            + '" src="about:blank" style="border:0;width:100%;height:100%;overflow: hidden" FRAMEBORDER="no"></iframe></div>'
      }));
      contentPane.activate(tabID);
      loadMenuLink(ifrID, guiID, params);
      // alert("olkk");
    }
  }

  function getBlank() {
    return "<html></html>";
  }

  function loadModuleMenu(item) {
    menuTree.getLoader().baseParams.module = item.code;
    menuTree.getRootNode().setText(item.text);
    menuTree.getLoader().load(menuTree.getRootNode(), function(l, n, r) {
          l.expand();
        });
  }

  function doLockSession() {
    Ext.Ajax.request({
          url : CFG_BACKENDSERVER_URL
              + "?_p_form=DC_MAIN&_p_action=custom&_p_custom_action=lockSession",
          scope : this,
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            if (respText.success && respText.message == "OK") {
              // Ext.getCmp("menu-login").enable();
              // Ext.getCmp("menu-logout").disable();
              logInWindow.fields.get("username").disable();
              logInWindow.show();
            } else {
              if (!Ext.isEmpty(respText.message)) {
                Ext.Msg.alert('Error', urldecode(respText.message));
              } else {
                Ext.Msg.alert('Error',
                    'Cannot lock session. Connection to server lost.');
              }
            }
          }
        });
  }

  function doLogout() {
    eraseCookie(AUTH_COOKIE_NAME);
    Ext.Ajax.request({
          url : "./j_spring_security_logout",
          //scope : this,
          success : function(response) {
            document.location.href = './';
          },
          failure : function(response) {
            Ext.Msg.alert('错误', '无法访问服务器。');
          }
        });
  }

  // build title and menu
  function createToolbar() {
    subSysMenu = new Ext.menu.Menu({
          id : 'sys-menu',
          items : []
        });
    var sessionMenu = new Ext.menu.Menu({
          id : 'session-menu',
          style : {
            overflow : 'visible'
          },
          items : [{
                id : "menu-logout",
                text : L('/SessionMenu/LogOut'),
                handler : function() {
                  doLogout();
                },
                scope : this,
                disabled : false
              }, "-", {
                id : "menu-lock",
                text : L('/SessionMenu/LockScrenn'),
                handler : function() {
                  doLockSession();
                },
                scope : this,
                disabled : false
              }]
        });

    var aboutMenu = new Ext.menu.Menu({
      id : 'about-menu',
      style : {
        overflow : 'visible'
      },
      items : [{
            text : '关于',
            handler : function() {
              document.getElementById("content_iframe").src = "help/showAbout"
            },
            scope : this
          }, {
            text : '在线帮助',
            handler : function() {
              document.getElementById("content_iframe").src = "help/howto"
            },
            scope : this
            // disabled : true
        }]
    });

    menuBar = new Ext.Toolbar({
          id : 'menu-toolbar',
          items : [new Ext.Toolbar.Separator(), {
                text : '子系统',
                icon : CFG_PATH_ICONS + '/subsys.gif',
                menu : subSysMenu
              }, {
                text : '会话',
                icon : CFG_PATH_ICONS + '/user.gif',
                menu : sessionMenu
              }, {
                text : '帮助',
                icon : CFG_PATH_ICONS + '/help.gif',
                menu : aboutMenu
              }, '->', '<span id="cur-user-name"></span>']
        });

    mTlb = new Ext.Panel({
      region : 'north',
      id : 'north-panel',
      split : false,
      height : 76,
      border : false,
      collapsible : false,
      margins : '0 0 0 0',
      layout : 'border',
      items : [{
        region : 'north',
        html : "<table style='width:100%;background-color:#006EC7;border:0;' cellspacing='0'><tr><td align='left' width='120'><img src='resources/img/eaton.gif'/></td><td class='product-name'>"
            + L('/Application/Name')
            + "</td><td align='right'><img src='resources/img/eaton-ipm.gif'/></td></tr></table>",
        border : false,
        height : 48
      }, {
        region : 'center',
        border : false,
        height : 28,
        items : [menuBar]
      }]
    });
  }

  // 获取当前用户的信息
  function getCurUserInfos() {
    var curUser = getCurUser();
    if (curUser) {
      Ext.get('cur-user-name').dom.innerHTML = "当前用户:&nbsp;&nbsp;" + curUser;
    } else {
      Ext.get('cur-user-name').dom.innerHTML = "当前用户:&nbsp;&nbsp;";
    }
  }
});