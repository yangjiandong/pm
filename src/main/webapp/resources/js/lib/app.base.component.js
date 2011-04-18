Ext.ns('app.base');

app.base.SubmitButton = Ext.extend(Ext.Button, {
     text:'确定'
    ,iconCls:'icon-submit'
    ,initComponent:function() {
        app.base.SubmitButton.superclass.initComponent.apply(this, arguments);
    } // eo function initComponent
}); // eo extend

Ext.reg('submitbutton', app.base.SubmitButton);

app.base.CancelButton = Ext.extend(Ext.Button, {
     text:'取消'
    ,iconCls:'icon-undo'
    ,initComponent:function() {
        app.base.CancelButton.superclass.initComponent.apply(this, arguments);
    } // eo function initComponent
}); // eo extend

Ext.reg('cancelbutton', app.base.CancelButton);