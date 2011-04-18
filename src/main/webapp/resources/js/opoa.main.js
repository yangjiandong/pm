/*
 * OPOA Demo Author: 令狐葱 Email: microboat@gmail.com
 */

// 模块基类
Ext.namespace('demo');
demo.module = function(tab) {
  this.main = tab;
  this.init();
}

Ext.extend(demo.module, Ext.util.Observable, {
      init : Ext.emptyFn
    });

// 主程序类
demo.app = function() {
  this.init();
}
Ext.extend(demo.app, Ext.util.Observable, {
      init : function() {
        this.tree = new Ext.tree.TreePanel({
              region : 'west',
              margins : '0 0 0 5',
              width : 250,
              split : true,
              minSize : 200,
              maxSize : 300,
              autoScroll : true,
              bodyStyle : 'padding:10px',
              loader : new Ext.tree.TreeLoader({
                    url : 'book/getTree',
                    requestMethod : 'GET'
                  }),
              // 构造根节点
              root : new Ext.tree.AsyncTreeNode({
                    id : 'root',
                    text : '功能菜单',
                    expanded : true
                  })
            });
        this.tree.on('click', this.clickTree, this);

        var tab = new Ext.Panel({
              title : '说明',
              id : 'docs',
              bodyStyle : 'padding:10px',
              autoLoad : 'resources/js/docs.html',
              layout : 'fit'
            });

        this.body = new Ext.TabPanel({
              region : 'center',
              margins : '0 5 0 0',
              activeTab : 0,
              autoScroll : true,
              items : [tab]
            });
        this.body.on('tabchange', this.changeTab, this);

        var viewport = new Ext.Viewport({
              layout : 'border',
              items : [new Ext.BoxComponent({
                        region : 'north',
                        el : 'header',
                        height : 60
                      }), new Ext.BoxComponent({
                        region : 'south',
                        el : 'footer',
                        height : 50
                      }), this.tree, this.body]
            });

        this.loadMask = new Ext.LoadMask(this.body.body);
      },

      // 切换TabPanel标签方法
      changeTab : function(p, t) {
        // 如果存在相应树节点，就选中,否则就清空选择状态
        var id = t.id.replace('tab-', '');
        var node = this.tree.getNodeById(id);
        if (node) {
          this.tree.getSelectionModel().select(node);
        } else {
          this.tree.getSelectionModel().clearSelections();
        }
      },

      // 点击菜单方法(node:被点击的节点)
      clickTree : function(node) {
        // 如果节点不是叶子则返回
        if (!node.isLeaf()) {
          return false;
        }

        var id = 'tab-' + node.id;
        var tab = Ext.getCmp(id);
        if (!tab) {
          tab = this.body.add(new Ext.Panel({
                id : id,
                title : node.text,
                layout : 'fit',
                closable : true
              }));
          this.body.setActiveTab(tab);
          // 加载模块
          this.loadModel(node.id, tab);
        } else {
          this.body.setActiveTab(tab);
        }
      },

      // 加载模块方法(id:模块ID;tab:模块显示在哪里)
      loadModel : function(id, tab) {
        // 定义模块变量
        var model;

        if (this[id]) {
          // 如果模块类已存在，就直接实例化
          model = new this[id](tab);
        } else {
          this.loadMask.show();
          Ext.Ajax.request({
                method : 'GET',
                url : 'resources/js/modules/' + id + '.js',
                scope : this,
                success : function(response) {
                  // 获取模块类
                  this[id] = eval(response.responseText);
                  // 实例化模块类
                  model = new this[id](tab);
                  this.loadMask.hide();
                }
              });
        }
      }
    });

// 实例化主程序类
Ext.BLANK_IMAGE_URL = 'resources/img/s.gif';
Ext.form.Field.prototype.msgTarget = 'title';
Ext.onReady(function() {
      Ext.QuickTips.init();

      if (!Ext.isEmpty(Ext.get('app-loading'))) {
        Ext.get('app-loading').remove();
      }
      myApp = new demo.app();
    });