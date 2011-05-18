/**
 * 修改用户密码的表单窗口
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');
Divo.app.ChangePwdFormWin = Ext.extend(Ext.Window, {
      formPanel : null,
      fldPassword : null,
      fldPassword2 : null,
      firstly : false,
      tn : 1,
      curUserName : null,
      initComponent : function() {
        // 创建内容
        this.createFormPanel();

        this.submitBtn = new Ext.Button({
              text : '确定',
              tabIndex : this.tn++,
              handler : this.onSubmit.createDelegate(this)
            });

        this.cancelBtn = new Ext.Button({
              text : '取消',
              tabIndex : this.tn++,
              handler : this.onWinHide.createDelegate(this)
            });

        Ext.apply(this, {
              title : '修改登录密码',
              iconCls : 'icon-win',
              width : 400,
              layout : 'form',
              buttonAlign : 'right',
              plain : false,
              autoHeight : true,
              closable : false,
              resizable : false,
              draggable : false,
              modal : true,
              items : [this.formPanel],
              buttons : [this.submitBtn, this.cancelBtn]
            });

        Divo.app.ChangePwdFormWin.superclass.initComponent.apply(this,
            arguments);
      },
      render : function() {
        Divo.app.ChangePwdFormWin.superclass.render.apply(this, arguments);
      },
      initEvents : function() {
        Divo.app.ChangePwdFormWin.superclass.initEvents.call(this);
        this.on("show", this.onWinShow, this);
      },
      /**
       * 提交
       */
      onSubmit : function() {
        var newPassword = this.fldPassword.getValue().trim();
        var newPassword2 = this.fldPassword2.getValue().trim();

        if (newPassword == "12345" || Ext.isEmpty(newPassword)) {
          this.fldPassword.markInvalid('密码不能为空或者12345,请重新输入');
          return;
        }

        if (newPassword != newPassword2) {
          this.fldPassword2.markInvalid('两次密码不一致,请重新输入');
          return;
        }

        this.body.mask('正在处理数据，请稍等...', 'x-mask-loading');
        this.submitBtn.disable();
        this.cancelBtn.disable();

        Ext.Ajax.request({
              url : "system/savepassword",
              method : "POST",
              scope : this,
              params : {
                userName : this.curUserName,
                newPassword : newPassword
              },
              callback : function(o, s, r) {
                this.body.unmask();
                this.hide();
                var respText = Ext.decode(r.responseText);
                if (respText.success && respText.message == "OK") {
                  //window.location.href = 'common/index';
                } else {
                  if (!Ext.isEmpty(respText.message)) {
                    Ext.Msg.alert('错误', respText.message);
                  } else {
                    Ext.Msg.alert('错误', '因为不能取得服务端信息，不能正常注销。');
                  }
                }
              }
            });
      },

      /**
       * 关闭窗口
       */
      onWinHide : function() {
        this.hide();
      },
      /**
       * 显示窗体时的初始化
       */
      onWinShow : function() {
        // this.curUser = Divo.util.Tools.getCurUser();

        this.formPanel.getForm().reset();

        // if (this.firstly) {
        // this.cancelBtn.hide();
        //
        // if (this.curUser) {
        // this.setTitle(this.curUser.userName + '，您是首次登录，请先设置密码');
        // }
        // } else {
        // this.cancelBtn.show();
        // }

        this.fldPassword.focus(true, 100);
      },
      /**
       * 创建表单
       */
      createFormPanel : function() {
        if (!this.formPanel) {
          this.fldPassword = new Ext.form.TextField({
                fieldLabel : '新密码',
                tabIndex : this.tn++
              });

          this.fldPassword2 = new Ext.form.TextField({
                fieldLabel : '再输入新密码',
                tabIndex : this.tn++
              });

          this.formPanel = new Ext.form.FormPanel({
                border : false,
                labelWidth : 120,
                labelPad : 5,
                bodyStyle : 'padding: 20px 50px',
                frame : false,
                defaults : {
                  inputType : 'password',
                  msgTarget : 'side',
                  labelSeparator : ":*"
                },
                items : [this.fldPassword, this.fldPassword2]
              });
        }
      }
    });

Ext.reg('Divo.app.ChangePwdFormWin', Divo.app.ChangePwdFormWin);
