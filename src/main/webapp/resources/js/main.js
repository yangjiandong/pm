/**
 * 系统主页面
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');
Ext.QuickTips.init();

var uiDcInc;

/* 菜单树面板 */
Divo.app.MenuPanel = function() {
  // region : 'west'
  Divo.app.MenuPanel.superclass.constructor.call(this, {
        autoScroll : true,
        animate : true,
        border : false,
        id : 'menu-tree',
        // no use root
        rootVisible : false,
        lines : false,
        layout : 'fit',
        loader : new Ext.tree.TreeLoader({
              dataUrl : 'system/loadMenu'
            }),

        // 虽然不显示, 还需定义
        root : new Ext.tree.AsyncTreeNode({
              id : 'root',
              resourceId : '0',
              text : '请先选择子系统',
              href : '',
              expanded : true
            })

      });

  this.getLoader().on("beforeload", function(treeLoader, node) {
        treeLoader.baseParams.resourceId = node.attributes.resourceId;
      }, this);
  this.getSelectionModel().on('beforeselect', function(sm, node) {
        return node.isLeaf();
      }, this);
  this.on('beforeexpandnode', function(node, deep, anim) {
        this.getLoader().load(node);
      }, this);
}

Ext.extend(Divo.app.MenuPanel, Ext.tree.TreePanel, {
      selectMenu : function(menuId) {
        if (menuId) {
          if (this.root.attributes.resourceId == menuId) {
            this.selectPath(this.root.getPath());
          } else {
            var curnode;
            this.root.cascade(function(n) {
                  if (n.isLeaf() && n.attributes.resourceId == menuId) {
                    curnode = n;
                  }
                });
            if (curnode) {
              this.selectPath(curnode.getPath());
            }
          }
        }
      }
    });

/* 操作界面主面板 */
Divo.app.MainPanel = function() {
  var html = [
      '<div id="welcome-div">',
      '<div style="float:left;"><img src="resources/img/layout-icon.gif" /></div>',
      '<div style="margin-left:100px;">', '<h2>欢迎使用！</h2>', '<p></p>', '</div>'];

  Divo.app.MainPanel.superclass.constructor.call(this, {
        id : 'menu-content-panel',
        region : 'center',
        margins : '3 3 3 0',
        resizeTabs : true,
        minTabWidth : 90,
        tabWidth : 150,
        enableTabScroll : true,
        activeTab : 0,
        deferredRender : false,
        items : [{
              id : 'welcome-panel',
              title : '欢迎',
              layout : 'fit',
              bodyStyle : 'padding:25px',
              html : html.join(''),
              autoScroll : true
            }]
      });
}

Ext.extend(Divo.app.MainPanel, Ext.TabPanel, {
  NEW_LINE : '\n',
  // CFG_DEPLOYMENT_TYPE : 'DEV',
  loadContent : function(href, menuId, title, iconCls) {
    var ifrId = 'frame-' + menuId;
    var tabId = 'contants-tab-' + menuId;
    var icls = iconCls;

    if (Ext.isEmpty(document.getElementById(ifrId))
        && !Ext.isEmpty(window.frames[ifrId])) {
      delete window.frames[ifrId];
    }

    if (!Ext.isEmpty(document.getElementById(ifrId))) {
      if (this.getActiveTab().getId() == tabId) {
        this.loadMenuLink(ifrId, href);
      } else {
        this.activate(tabId);
      }
    } else {
      this.add(new Ext.Panel({
        title : title,
        id : tabId,
        autoScroll : true,
        iconCls : icls,
        layout : 'fit',
        closable : true,
        html : '<iframe id="'
            + ifrId
            + '" name="'
            + ifrId
            + '" src="about:blank" style="border:0;width:100%;height:100%;overflow: hidden" FRAMEBORDER="no"></iframe>'
      }));
      this.activate(tabId);
      this.loadMenuLink(ifrId, href);
    }

    this.doLayout();
  },
  loadMenuLink : function(ifrId, href) {
    var curIframe = window.frames[ifrId];

    var htmlString = this.buildHtml(href);
    curIframe.document.open("text/html", "replace");
    curIframe.document.write(htmlString);
    curIframe.document.close();
  },
  buildHtml : function(href) {
    var out = '';
    out += '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">'
        + this.NEW_LINE;
    out += '<html>' + this.NEW_LINE;
    out += this.buildHtmlHead(href);
    out += this.buildHtmlBody();
    out += '</html>' + this.NEW_LINE;
    out += '';
    return out;
  },
  buildHtmlHead : function(href) {
    var out = '';
    out += '<head>' + this.NEW_LINE;
    out += '  <META HTTP-EQUIV="CONTENT-TYPE" CONTENT="text/html; charset=UTF-8"/>'
        + this.NEW_LINE;
    if (CFG_DEPLOYMENT_TYPE == 'DEV') { // 开发模式(DEV)
      out += '  <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE"/>'
          + this.NEW_LINE;
    } else { // 生产模式(PROD)
      out += '  <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="PRIVATE"/>'
          + this.NEW_LINE;
    }

    out += this.buildImportExtjs();
    out += this.buildImportEkingBase();
    out += this.buildImportDoc(href);
    out += '</head>' + this.NEW_LINE;
    out += '';
    return out;
  },
  buildHtmlBody : function() {
    var out = '';
    out += '<body>' + this.NEW_LINE;
    out += '</body>' + this.NEW_LINE;
    out += '';
    return out;
  },
  buildImportExtjs : function() {
    var out = '';
    out += '  <link rel="stylesheet" type="text/css" href="resources/ext/resources/css/ext-all.css"/>'
        + this.NEW_LINE;
    out += ' <link rel="stylesheet" type="text/css" href="resources/css/app.css"/>'
        + this.NEW_LINE;
    
    out += '  <script type="text/javascript" src="resources/ext/ext-base.js" ><\/script>'
        + this.NEW_LINE;
    out += '  <script type="text/javascript" src="resources/ext/ext-all.js" ><\/script>'
        + this.NEW_LINE;
    out += '  <script type="text/javascript" src="resources/ext/ext-basex.js" ><\/script>'
        + this.NEW_LINE;
    out += '  <script type="text/javascript" src="resources/ext/ext-lang-zh_CN.js"><\/script>'
        + this.NEW_LINE;
    if (CFG_DEPLOYMENT_TYPE == 'DEV') {
      out += '  <script type="text/javascript" src="resources/ext/debug.js" ><\/script>'
          + this.NEW_LINE;
    }

    if (Ext.isIE) {
      out += '<script id="ie-deferred-loader" defer="defer" src="//:"></script>'
          + this.NEW_LINE;
    }

    out += '';
    return out;
  },
  buildImportEkingBase : function() {
    var out = '';
    out += '  <script type="text/javascript" src="resources/js/lib/app.core.js'
        + '"><\/script>' + this.NEW_LINE;
    out += '  <script type="text/javascript" src="resources/js/lib/app.base.grid.js'
        + '"><\/script>' + this.NEW_LINE;
    out += '  <script type="text/javascript" src="resources/js/lib/app.base.edit.grid.js'
        + '"><\/script>' + this.NEW_LINE;
    out += '  <script type="text/javascript" src="resources/js/lib/app.base.form.js'
        + '"><\/script>' + this.NEW_LINE;
    out += '  <script type="text/javascript" src="resources/js/lib/utils.js'
        + '"><\/script>' + this.NEW_LINE;
    out += '  <script type="text/javascript" src="resources/js/lib/app.column.tree.js'
        + '"><\/script>' + this.NEW_LINE;
    out += '  <script type="text/javascript" src="resources/js/lib/app.tdgi.borderLayout.js'
        + '"><\/script>' + this.NEW_LINE;
    out += '  <script type="text/javascript" src="resources/js/lib/app.base.component.js'
        + '"><\/script>' + this.NEW_LINE;

    out += '';
    return out;
  },
  buildImportDoc : function(jsUI) {
    var basePath = 'resources/js/';
    var out = '';
    var ts = '';
    // if (this.CFG_DEPLOYMENT_TYPE == 'DEV') {
    // ts = '?_t_=' + (new Date()).getTime();
    // }

    var pDcArray = uiDcInc.get(jsUI)
    if (pDcArray != undefined) {
      for (var j = 0; j < pDcArray.length; j++) {
        out += '<script  type="text/javascript" src="' + basePath + pDcArray[j]
            + ts + '"><\/script>' + this.NEW_LINE;
      }
    }

    out += '';
    return out;
  }
});

Divo.app.Main = function() {
  /* -------------------- private属性 -------------------- */
  // var menuBar, menuTree, mainPanel, viewport;
  // var subSysMenu;
  // var maxPanelNum = 4;
  var aboutWin, menuBar, menuComponent, mainPanel, viewport, pwdWin;
  var sysConfig, subSysMenu, thumbTemplate;
  var maxPanelNum = 3;

  /* -------------------- private方法 -------------------- */
  // 创建菜单栏
  function createMenus() {
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
                icon : 'resources/img/icon/logoff.png',
                text : '注销',
                handler : function() {
                  doLogout();
                },
                scope : this,
                disabled : false
              }, '-', {
                id : "menu-change-pwd",
                icon : 'resources/img/icon/key.png',
                text : '修改密码',
                handler : function() {
                  pwdWin.show();
                },
                scope : this,
                disabled : false
              }, '-', {
                id : "menu-lock",
                text : '锁屏',
                handler : function() {
                  // doLockSession();
                },
                scope : this,
                disabled : true
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
                  aboutWin.show();
                },
                scope : this
              }, {
                text : '在线帮助',
                handler : function() {
                },
                scope : this,
                disabled : true
              }]
        });

    menuBar = new Ext.Toolbar({
          id : 'menu-toolbar',
          items : [
              new Ext.Toolbar.Separator(),
              {
                icon : 'resources/img/subsys.gif',
                text : '首页',
                handler : function() {
                  goToIndex();
                },
                scope : this,
                disabled : false
              },
              {
                text : '会话',
                icon : 'resources/img/session.gif',
                menu : sessionMenu
              },
              {
                text : '帮助',
                icon : 'resources/img/help.gif',
                menu : aboutMenu
              },
              {
                id : "menu-logout2",
                icon : 'resources/img/icon/logoff.png',
                text : '注销',
                handler : function() {
                  doLogout();
                },
                scope : this,
                disabled : false
              },
              '->',
              '当前用户:&nbsp;&nbsp;<span id="cur-user-name">'
                  + sysConfig.user_name + '</span>']
        });
  }

  /**
   * 加载子系统的菜单,并关闭mainPanel中已打开的面板
   */
  function loadModuleMenu(item) {
    onCloseMainPanelTabs();

    menuTree.getLoader().baseParams.module = item.code;
    menuTree.getRootNode().setText(item.text);
    // menuTree.getRootNode().attributes.resourceId = item.code;
    menuTree.getLoader().load(menuTree.getRootNode(), function(l, n, r) {
          l.expand();
        });

  }

  // 关闭全部打开的主面板Tab Panel
  function onCloseMainPanelTabs() {
    var ps = []
    var items = mainPanel.items.items;
    for (var i = 0; i < items.length; i++) {
      ps.push(items[i].id);
    }
    for (var i = 0; i < ps.length; i++) {
      mainPanel.remove(mainPanel.getItem(ps[i]), true);
    }
  }

  // 创建布局
  function createViewport() {

    aboutWin = new Divo.app.AboutWin({
          sysConfig : sysConfig
        });

    pwdWin = new Divo.app.ChangePwdFormWin({
          curUserName : sysConfig.user_name
        });

    var statusBar = new Ext.BoxComponent({
          region : 'south',
          height : 18,
          autoEl : {
            html : '<center>&nbsp;&copy;' + sysConfig.copyright + '</center>'
          }
        });

    menuTree = new Divo.app.MenuPanel();
    mainPanel = new Divo.app.MainPanel();

    mainPanel.on('tabchange', function(tp, tab) {
          if (tab) {
            menuTree.selectMenu(tab.menuId);
          }
        });

    // 限制最多能打开的面板,防止浏览器过载崩溃
    mainPanel.on('beforeadd', function(container, component, index) {
          if (index > maxPanelNum) {
            var items = container.items.items;
            var menuId = items[0].id;
            var tab = mainPanel.getItem(menuId)
            mainPanel.remove(tab, true);
          }
        });

    // 防止IFRAME销毁后仍然占用内存
    mainPanel.on('beforeremove', function(o, p) {
          var iFrame = p.getEl().dom;
          if (iFrame.src) {
            iFrame.src = "javascript:false";
          }
        });

    var menuComponent = new Ext.Panel({
          region : 'west',
          id : 'west-panel', // see Ext.getCmp() below
          title : '系统导航',
          split : true,
          autoScroll : true,
          width : 200,
          minSize : 175,
          maxSize : 400,
          layout : 'fit',
          collapsible : true,
          // collapsedTitle : true,
          collapseMode : 'mini',
          margins : '3 0 3 3',
          items : [menuTree]
        });

    menuTree.on('click', function(node, e) {
          if (node.isLeaf() && node.attributes.url.length > 0) {
            e.stopEvent();

            mainPanel.loadContent(node.attributes.url,
                node.attributes.resourceId, node.text, node.attributes.iconCls);
            viewport.doLayout();
          } else {
            e.stopEvent();
          }
        });

    var titleBar = new Ext.Panel({
      region : 'north',
      id : 'north-panel',
      split : false,
      height : 56,
      border : false,
      collapsible : false,
      margins : '0 0 0 0',
      layout : 'border',
      items : [{
        region : 'north',
        html : '<div id="titlebar"><input type=hidden value="" id="activeFrameId">'
            + '</input><h1>'
            + sysConfig.application_name
            + sysConfig.version
            + '</h1></div>'
        // + '</input><h1>' + sysConfig.application_name + '</h1></div>'
        ,
        border : false,
        height : 28
      }, {
        region : 'center',
        border : false,
        height : 26,
        items : [menuBar]
      }]
    });

    viewport = new Ext.Viewport({
          layout : 'tdgi_border',
          items : [titleBar, statusBar, menuComponent, mainPanel],
          listeners : {
            render : function() {
              Ext.Ajax.request({
                    url : 'system/loadSubSystem',
                    success : successFn
                  });
            }
          }
        });

    viewport.doLayout();
  }

  // viewport 渲染后，加载子系统菜单项
  function successFn(response) {
    var data = Ext.decode(response.responseText);

    if (data != null && data.success) {
      subSys = data.subSystems;
      for (var i = 0; i < subSys.length; i++) {
        var menuCode = subSys[i].resourceId;
        var menuName = subSys[i].text;

        subSysMenu.add({
              id : 'menu-' + subSys[i].resourceId,
              code : subSys[i].resourceId,
              text : subSys[i].text,
              handler : loadModuleMenu
            });
      }
    }

  }

  // 注销
  function doLogout() {
    Ext.Ajax.request({
          url : "common/logout",
          scope : this,
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            if (respText.success && respText.message == "OK") {
              viewport.destroy();
              window.location.href = 'common/index';
            } else {
              if (!Ext.isEmpty(respText.message)) {
                Ext.Msg.alert('错误', respText.message);
              } else {
                Ext.Msg.alert('错误', '因为不能取得服务端信息，不能正常注销。');
              }
            }
          }
        });
  }

  // 回到首页
  function goToIndex() {
    window.location.href = 'common/index';
  }

  function getSysConfig() {
    if (sysConfig == null) {
      Ext.Ajax.request({
            url : "common/get_sys_config",
            scope : this,
            async : false,
            callback : function(o, s, r) {
              var resp = Ext.decode(r.responseText);
              sysConfig = {
                user_name : resp.user_name,
                application_name : resp.application_name,
                vendor : resp.vendor,
                copyright : resp.copyright,
                run_mode : resp.run_mode,
                version : resp.version,
                website : resp.website
              }
            }
          });
    }
  }

  /* ----------------------- public方法 ----------------------- */
  return {
    init : function() {
      uiDcInc = new Ext.util.MixedCollection();
      initUiDcIncludes(uiDcInc);
      getSysConfig();
      createMenus();
      createViewport();
    }
  }
}();

Ext.onReady(Divo.app.Main.init, Divo.app.Main, true);
