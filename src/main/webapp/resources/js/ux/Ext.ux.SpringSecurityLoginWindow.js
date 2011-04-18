
Ext.namespace("Ext.co");

SpringSecurityLoginWindow = Ext.extend(Ext.Window, {
    title: '登陆',
    width: 265,
    height: 240,
    collapsible: true,
    closable: false,
    modal: true,
    defaults: {
        border: false
    },
    buttonAlign: 'center',
    listeners: {
        show: function() {
            Ext.getDom('captcha').src = 'captcha.jpg?' + new Date().getTime();
        }
    },
    createFormPanel: function() {
        return new Ext.form.FormPanel({
            bodyStyle: 'padding-top:6px',
            defaultType: 'textfield',
            labelAlign: 'right',
            labelWidth: 60,
            labelPad: 0,
            frame: true,
            defaults: {
                allowBlank: false,
                width: 158,
                selectOnFocus: true
            },
            items: [{
                cls: 'user',
                name: 'j_username',
                fieldLabel: '用户名',
                blankText: '用户名不能为空'
            },{
                cls: 'key',
                name: 'j_password',
                fieldLabel: '密  码',
                blankText: '密码不能为空',
                inputType: 'password'
            },{
                xtype: 'checkbox',
                name: '_spring_security_remember_me',
                fieldLabel: '记住信息'
            },{
                name: '_captcha_parameter',
                fieldLabel: '验证码'
            },{
                xtype: 'panel',
                anchor: '100%',
                bodyStyle: 'text-align:right;padding-right: 20px;',
                html: '<img id="captcha" src="captcha.jpg" title="点击刷新图片" alt="点击刷新图片" onclick="document.getElementById(\'captcha\').src=\'captcha.jpg?\' + new Date().getTime();this.blur();" style="cursor:pointer;border:0px;"><br><a onclick="document.getElementById(\'captcha\').src=\'captcha.jpg?\' + new Date().getTime();this.blur();" href="javascript:void(0);">刷新图片</a>'
            }]
        });
    },
    login: function() {
        if (this.formPanel.form.isValid()) {
            this.formPanel.form.submit({
                waitTitle: "请稍候",
                waitMsg : '正在登录......',
                url: this.url + '?' + new Date(),
                success: function(form, action) {
                    this.hide();
                    if (this.callback) {
                        this.callback.call(this, action.result);
                    }
                },
                failure: function(form, action) {
                    if (action.failureType == Ext.form.Action.SERVER_INVALID) {
                        Ext.MessageBox.alert('错误', action.result.errors.msg);
                    }
                    Ext.getDom('captcha').src = 'captcha.jpg?' + new Date().getTime();
                    form.findField("j_password").setRawValue("");
                    form.findField("j_username").focus(true);
                },
                scope: this
            });
        }
    },
    initComponent : function(){
        this.keys = {
            key: Ext.EventObject.ENTER,
            fn: this.login,
            scope: this
        };
        LoginWindow.superclass.initComponent.call(this);
        this.formPanel = this.createFormPanel();
        this.add(this.formPanel);
        this.addButton('登陆', this.login, this);
        this.addButton('重填', function() {
            this.formPanel.getForm().reset();
        }, this);
    }
});

