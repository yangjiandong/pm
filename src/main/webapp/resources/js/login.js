/**
 * 登录
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');
Ext.QuickTips.init();
//Ext.BLANK_IMAGE_URL = 'public/extjs/resources/images/default/s.gif';
Divo.app.Login = function() {
  /* -------------------- private属性 -------------------- */
  var win, okBtn, cancelBtn, tn = 1;
  var logoPanel, formPanel, fldLoginName, fldPassword, fldCheck;
  var cp, cpUsername, cpPassword, cpCheck;

  /* -------------------- private方法 -------------------- */
  // 创建cookie
  function createCookie() {
    cp = new Ext.state.CookieProvider({
      expires : new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * 1))
        // 1 当天有效
      });
    Ext.state.Manager.setProvider(cp);
    cpUsername = cp.get("username");
    cpPassword = cp.get("password");
    cpCheck = cp.get("check");
  }

  // 创建表单
  function createForm() {
    if (formPanel)
      return;

    logoPanel = new Ext.Panel({
          border : false,
          baseCls : 'x-plain',
          id : 'login-logo',
          region : 'center'
        });

    fldLoginName = new Ext.form.TextField({
          cls : 'user',
          fieldLabel : '登录名',
          id : 'login-name',
          tabIndex : tn++,
          value : cpUsername,
          allowBlank : false
        });
    fldLoginName.on('specialkey', onEnter);

    fldPassword = new Ext.form.TextField({
          cls : 'key',
          fieldLabel : '密&nbsp;&nbsp;&nbsp;码',
          id : 'password',
          inputType : 'password',
          value : cpPassword,
          tabIndex : tn++
        });
    fldPassword.on('specialkey', onEnter);

    fldCheck = new Ext.form.Checkbox({
          checked : false,
          boxLabel : '记住用户名和密码',
          checked : cpCheck,
          tabIndex : tn++
        })
    fldCheck.on('specialkey', onEnter);

    formPanel = new Ext.form.FormPanel({
          labelAlign : 'left',
          border : false,
          region : 'south',
          labelWidth : 70,
          height : 120,
          labelPad : 5,
          bodyStyle : 'padding: 20px 50px',
          frame : false,
          items : [fldLoginName, fldPassword, fldCheck]
        });

  }

  // 按回车键直接登录
  function onEnter(o, e) {
    if (e.getKey() === e.ENTER) {
      onSubmit();
      e.stopEvent();
    }
  }

  // 创建窗口
  function createWindow() {
    if (win)
      return;

    win = new Ext.Window({
          id : 'login-win',
          iconCls : 'icon-win',
          title : '登录',
          buttonAlign : 'right',
          closable : false,
          draggable : true,
          height : 280,
          width : 600,
          layout : 'border',
          plain : true,
          bodyStyle : 'padding:5px;',
          items : [logoPanel, formPanel]
        });

    okBtn = win.addButton({
          text : '确定',
          tabIndex : tn++
        }, onSubmit, this);

    canelBtn = win.addButton({
          text : '取消',
          tabIndex : tn++
        }, onCancel, this);
  }

  function onSubmit() {

    var form = formPanel.getForm();
    if (form.isValid()) {
      var username = fldLoginName.getValue();
      var password = fldPassword.getValue();

      if (fldCheck.getValue()) {
        cp.set("username", username);
        cp.set("password", password);
        cp.set("check", fldCheck.getValue());
      } else {
        cp.clear("username");
        cp.clear("password");
        cp.clear("check");
      }

      okBtn.disable();
      canelBtn.disable();
      win.body.mask('正在验证身份...', 'x-mask-loading');
      formPanel.getForm().submit({
            url : '../common/logon',
            params : {
              username : username,
              password : password
            },
            success : function(form, action) {
              win.body.unmask();
              win.hide();
              window.location.href = '../';
            },
            failure : function(form, action) {
              win.body.unmask();
              okBtn.enable();
              canelBtn.enable();
              if (action.result.message == undefined)
                Ext.Msg.alert('错误', '网络连接失败.');
              else
                Ext.Msg.alert('错误', action.result.message);
            }
          });
    }
  }

  function onCancel() {
    if (Ext.IsIE)
      window.opener = self;
    else
      window.open('', '_parent', '');

    window.close()
  }

  return {
    init : function() {
      createCookie();
      createForm();
      createWindow();

      win.show();
      fldLoginName.focus(true, 10);
    }
  }
}();

Ext.onReady(Divo.app.Login.init, Divo.app.Login, true);
