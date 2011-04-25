Ext.ns('app.base');

app.base.SubmitButton = Ext.extend(Ext.Button, {
      text : '确定',
      iconCls : 'icon-submit',
      initComponent : function() {
        app.base.SubmitButton.superclass.initComponent.apply(this, arguments);
      } // eo function initComponent
    }); // eo extend

Ext.reg('submitbutton', app.base.SubmitButton);

app.base.CancelButton = Ext.extend(Ext.Button, {
      text : '取消',
      iconCls : 'icon-undo',
      initComponent : function() {
        app.base.CancelButton.superclass.initComponent.apply(this, arguments);
      } // eo function initComponent
    }); // eo extend

Ext.reg('cancelbutton', app.base.CancelButton);

Ext.Toast = function() {

};
Ext.Toast.LongTime = 4000;
Ext.Toast.ShortTime = 2000;
Ext.Toast.show = function(msgText, time) {
  Ext.MessageBox.show({
        msg : msgText,
        closable : false
      });
  setTimeout(function() {
        Ext.MessageBox.hide();
      }, time);
};

//Ext.Toast.show("我要显示出来！",Ext.Toast.ShortTime);