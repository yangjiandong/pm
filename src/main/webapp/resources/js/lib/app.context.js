/**
 * 应用程序上下文
 */
Ext.ns('Ext.app');

Ext.app.AppContext = function(config) {
  Ext.apply(this, config)
  Ext.app.AppContext.superclass.constructor.call(this)
}
Ext.extend(Ext.app.AppContext, Ext.util.Observable, {
      version : null,
      user : null,
      // private
      funcPermissions : [],
      // private
      globalPermissions : [],
      isDebug : false,
      baseUrl : "/media/",
      homeUrl : null,
      productName : null,
      copyRight : null,
      toDay : null,
      firstLogin : false,
      deptId : null,
      deptName : '',
      init : function() {
        this.homeUrl = window.location.href
        this.loadAppContext()
        this.loadPermissions()
      },
      isAdmin : function(userId) {
        if (userId !== undefined)
          return userId === 1;
        return false;
      },
      getDayMonthYearFormat : function() {
        return "Y.m.d";
      },
      getDatetimeFormat : function() {
        return "Y.m.d h:i a";
      },
      hasAppInstalled : function(app) {
        if (!this.apps)
          return false
        for (var i = 0; i < this.apps.length; i++) {
          if (this.apps[i].indexOf(app) >= 0) {
            return true
          }
        }
        return false
      },
      loadAppContext : function() {
        Ext.Ajax.request({
              scope : this,
              url : '/appcontext',
              async : false,
              method : 'GET',
              success : function(response, options) {
                var resp = Ext.decode(response.responseText);
                this.user = {
                  id : resp.userId,
                  name : resp.userName,
                  fullName : resp.userFullName,
                  email : resp.email
                }
                this.isDebug = resp.isDebug
                this.apps = resp.apps
                this.version = resp.version
                this.productName = resp.productName
                this.copyRight = resp.copyRight
                this.toDay = resp.toDay
                this.firstLogin = resp.firstLogin
                this.deptId = resp.deptId
                this.deptName = resp.deptName
              },
              failure : function(response, options) {
                top.window.location.href = "/"
              }
            })
      },
      // private
      loadPermissions : function() {
        Ext.Ajax.request({
              scope : this,
              url : '/users/' + this.user.id + '/permissions/functional',
              async : false,
              method : 'GET',
              success : function(response, options) {
                var resp = Ext.decode(response.responseText);
                this.funcPermissions = resp.rows
              },
              failure : function(response, options) {
                // 可能是token lost!
                top.window.location.href = "/"
              }
            })
        Ext.Ajax.request({
              scope : this,
              url : '/users/' + this.user.id + '/permissions/global',
              async : false,
              method : 'GET',
              success : function(response, options) {
                var resp = Ext.decode(response.responseText);
                this.globalPermissions = resp.rows
              },
              failure : function(response, options) {
                top.window.location.href = "/"
              }
            })
      }
    })

app.appContext = new Ext.app.AppContext()

// EOP
