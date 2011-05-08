/**
 * 关于对话框
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');
Divo.app.AboutWin = Ext.extend(Ext.Window, {
  itemPanel : null,
  sysConfig : null,
  initComponent : function() {
    // 创建内容
    this.itemPanel = this.getItemPanel();

    Ext.apply(this, {
          iconCls : 'icon-win',
          autoHeight : false,
          title : this.sysConfig ? '关于&nbsp;&nbsp;'
              + this.sysConfig.application_name : '关于',
          width : 570,
          height : 320,
          layout : 'border',
          plain : false,
          buttonAlign : 'right',
          closable : true,
          closeAction : 'hide',
          modal : true,
          items : [this.itemPanel],
          buttons : [{
                text : '确定',
                handler : this.onWinHide.createDelegate(this)
              }]
        });

    Divo.app.AboutWin.superclass.initComponent.apply(this, arguments);
  },
  render : function() {
    Divo.app.AboutWin.superclass.render.apply(this, arguments);
  },
  initEvents : function() {
    Divo.app.AboutWin.superclass.initEvents.call(this);
  },
  /**
   * 关闭窗体
   */
  onWinHide : function() {
    this.hide();
  },
  getItemPanel : function() {
    if (this.itemPanel) {
      return this.itemPanel;

    } else {
      var item;
      if (this.sysConfig == null) {
        item = new Ext.Panel({
              region : 'center',
              html : '没有找到系统配置信息'
            });

      } else {
        var html = [
            '<table width="100%" border="0" cellspacing="0" cellpadding="0">',
            ' <tr>',
            '<td colspan="2" align="right"><img src="resources/img/about.gif"></td>',
            '</tr>',
            '<tr>',
            '<td width="7%">&nbsp;</td>',
            '<td width="93%"><table width="100%" border="0" cellspacing="0" cellpadding="0">',
            '<tr>',
            '<td height="35" class="moduleTitle" >'
                + this.sysConfig.application_name + '</td>',
            '</tr>',
            '<tr>',
            '<td><p>版本: <span>' + this.sysConfig.version + '</span></p>',
            '</tr>',
            '<tr>',
            '<td><p>构建标识: <span>' + this.sysConfig.run_mode + '</span></p>',
            '</tr>',
            '<tr>',
            '<td><p>版权所有&nbsp;&copy;' + this.sysConfig.copyright + '</p>',
            '</tr>',
            '<tr>',
            '<td><p>请访问: <a href="' + this.sysConfig.website
                + '"  target="_blank">' + this.sysConfig.website + '</a></p>',
            '</tr>', '</table></td>', '</tr>'];
        item = new Ext.Panel({
              region : 'center',
              html : html.join('')
            });
      }

      return item;
    }

  }
});
Ext.reg('Divo.app.AboutWin', Divo.app.AboutWin);