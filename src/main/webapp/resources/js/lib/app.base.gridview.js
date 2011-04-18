Ext.ns('app.base');

app.base.GridView = Ext.extend(Ext.grid.GridPanel, {
  dataRecordMeta               : null,
  recordPk                     : new Array(),
  DATE_FORMAT                  : 'YYYY.mm.dd',
  queryFields                  : new Array(),
  queryWindow                  : null,
  toolbarConfig                : null,
  hasQuickFilter               : false,
  queryPanelColCount           : 2,
  queryFields                  : new Ext.util.MixedCollection(),
  queryFieldsVisible           : new Array(),

  // 重写父类的方法，构造函数
  initComponent                : function() {
    Ext.apply(this, arguments);

    if (this.queryArraySize != null && this.queryArraySize != -1) {
      this.bbar = new Ext.PagingToolbar({
            store       : this.store,
            displayInfo : true,
            pageSize    : 20
          });
    }

    if (this.queryPanelColCount > 0) {
      this.tbar = new Ext.Panel({
            autoScroll   : true,
            border       : false,
            bodyBorder   : false,
            // ,tbar:toolbar1
            frame        : true,
            layout       : 'table',
            layoutConfig : {
              columns : this.queryPanelColCount
            },
            defaults     : {
              labelWidth : 90,
              labelAlign : 'right'
            },
            bodyStyle    : 'padding-top:5px;padding-bottom:5px;'
          });
      // alert('this.queryPanelColCount='+this.queryPanelColCount);
      for (var i = 1; i <= this.queryPanelColCount; i++) {
        // this.tbar.items[this.tbar.items.length]
        this.tbar.add({
              layout     : 'form',
              labelAlign : 'right',
              bodyStyle  : 'border:0;',
              items      : this.getQueryFieldsForPanelCol(i)
            });
      }

    }
    // alert('this.tbar.items.length='+this.tbar.items.length);

    app.base.GridView.superclass.initComponent.apply(this, arguments);
    this.addEvents('executeQuerySuccess');
    this.addEvents('createNewRecord');
  },

  initEvents                   : function() {
    app.base.GridView.superclass.initEvents.call(this);
    this.getStore().proxy.on('loadexception', this.onLoadException, this);
    this.getStore().proxy.on('load', this.onLoadProxy, this);
    this.getStore().on('load', this.onLoadStore, this);
    var keyMap = new Ext.KeyMap(this.body, [{
      key   : Ext.EventObject.PAGE_DOWN,
      fn    : function() {
        if (this.getBottomToolbar().getPageData().activePage < this.getBottomToolbar()
            .getPageData().pages) {
          this.getBottomToolbar().changePage(this.getBottomToolbar().getPageData().activePage + 1);
        }
      },
      ctrl  : false,
      scope : this
    }, {
      key   : Ext.EventObject.PAGE_UP,
      fn    : function() {
        if (this.getBottomToolbar().getPageData().activePage > 1) {
          this.getBottomToolbar().changePage(this.getBottomToolbar().getPageData().activePage - 1);
        }
      },
      ctrl  : false,
      scope : this
    }
        // ,{ key: Ext.EventObject.F7 , fn: function() { this.enterQuery(); },
        // ctrl:false, scope:this }

        ]);
    keyMap.stopEvent = true;
  },

  getQueryFieldsForPanelCol    : function(colNr) {
    var idxStart, idxStop;
    var colArr = new Array();
    var vqfLen = this.queryFieldsVisible.length;
    var mod = vqfLen % this.queryPanelColCount;
    var colSize = Math.floor(vqfLen / this.queryPanelColCount);
    colSize = (mod > 0 && colNr <= mod) ? (colSize + 1) : colSize;

    idxStart = Math.floor(vqfLen / this.queryPanelColCount) * (colNr - 1);

    // alert('modulus='+mod+', colNr='+colNr+' colSize='+colSize);
    if (mod > 0) {
      idxStart += (colNr > mod) ? mod : (colNr - 1);
    }
    idxStop = idxStart + colSize;
    idxStop = (idxStop < vqfLen) ? idxStop : vqfLen;
    // alert('idxStart='+idxStart+' idxStop='+idxStop);
    // alert('idxStart='+idxStart+' idxStop='+idxStop);
    if (vqfLen % this.queryPanelColCount != 0) {
      if (i < vqfLen % this.queryPanelColCount) {
        // idxStart++;
        // idxStop++;
      }
    }
    for (var i = idxStart; i < idxStop; i++) { // alert('col='+colNr+'
      // qf='+this.queryFieldsVisible[i]);
      colArr[colArr.length] = this.queryFields.get(this.queryFieldsVisible[i]);
    }

    return colArr;
  },

  getSelectedRowPK             : function() {
    var pk = new Object();
    for (var i = 0; i < this.recordPk.length; i++) {
      pk[this.recordPk[i]] = this.getSelectionModel().getSelected().get(this.recordPk[i]);
    }
    return pk;
  },

  onLoadProxy                  : function(proxy, obj, arg, result) {
    if (result = !null && result != undefined ){
    if (this.getStore().reader instanceof Ext.data.JsonReader) {
      for (var i = 0; i < result.records.length; i++) {
        for (v in result.records[i].data) {
          if (Ext.type(result.records[i].get(v)) == 'string')
            result.records[i].set(v, this.urldecode(result.records[i].get(v)));
        }
        result.records[i].commit();
      }
    }
    }
  },

  onLoadStore                  : function() {
    this.getSelectionModel().selectFirstRow();
    this.getView().focusRow(0);
  },

  onLoadException              : function(proxy, options, response, err) {
    if (err = !null && err != undefined) {
      Ext.Msg.alert(err.name, err.message);
    } else {
      try {
        Ext.Msg.alert(response.statusText, response.responseText);
      } catch (e) {
        Ext.Msg.alert(e.name, e.message);
        throw e;
      }
    }
    return false;
  },

  createNewRecord              : function() {
    this.fireEvent('createNewRecord');
  },

  enterQuery                   : function() {
    if (this.queryFieldsVisible.length > 0) {
      this.queryFields.get(this.queryFieldsVisible[0]).focus();
    }
  },

  resetQuery                   : function() {
    for (var i = 0; i < this.queryFields.length; i++) {
      this.queryWindow.findById(this.queryFields[i].id).setValue(null);
    }
  },

  executeQuery                 : function() {
    var p = new Object();
    var qf = this.queryFields;
    for (var i = 0, len = qf.keys.length; i < len; i++) {
      if (qf.items[i] instanceof Ext.form.DateField) {
        if (!Ext.isEmpty(qf.items[i].getValue())) {
          p["QRY_" + qf.keys[i]] = qf.items[i].getValue().format(Ext.DATE_FORMAT);
        } else {
          p["QRY_" + qf.keys[i]] = "";
        }
      } else {
        p["QRY_" + qf.keys[i]] = qf.items[i].getValue();
      }
    }
    this.store.baseParams = p;
    this.store.load({
          callback : this.afterExecuteQuery,
          scope    : this
        });
  },

  afterExecuteQuery            : function(r, options, success) {
    if (success) {
      if (this.grid && this.store.getCount() > 0) {
        this.getSelectionModel().selectFirstRow();
        this.getView().focusRow(0);
        this.fireEvent("executeQuerySuccess", this, this.store.getCount(), this.getSelectionModel()
                .getSelected());
      } else {
        this.fireEvent("executeQuerySuccess", this, this.store.getCount(), null);
      }
    }
  },

  urldecode                    : function(str) {
    var ret = str;
    try {
      ret = str.replace(/\+/g, "%20");
      ret = decodeURIComponent(ret);
      ret = ret.toString();
      return ret;
    } catch (e) {
      return str;
    }
  },

  clear_records                : function() {
    this.getStore().removeAll();
  },

  setQueryFieldValue           : function(fieldName, fieldValue) {
    this.queryFields.get(fieldName).setValue(fieldValue);
  },

  export_data                  : function(p_format) {
    var qs = '';

    var qf = this.queryFields;
    for (var i = 0, len = qf.keys.length; i < len; i++) {
      if (qf.items[i].getValue() != undefined)
        qs = qs + '&QRY_' + qf.keys[i] + '=' + qf.items[i].getValue();
    }

    var cs = '&_p_disp_cols=';
    for (var i = 0; i < this.getColumnModel().getColumnCount(); i++) {
      if (!this.getColumnModel().isHidden(i)) {
        cs = cs + '' + this.getColumnModel().getDataIndex(i) + '|';
      }
    }
    var ss = ''; // sorting and grouping
    if (this.getStore().getSortState()) {
      ss = '&sort=' + this.getStore().getSortState().field + '&dir='
          + this.getStore().getSortState().direction;
    }
    if (this.getView() instanceof Ext.grid.GroupingView) {
      if (this.getStore() instanceof Ext.data.GroupingStore && this.getStore().groupField !== false) {
        ss += '&groupBy=' + this.getStore().getGroupState();
      }

    }

    var v = window.open(CFG_BACKENDSERVER_URL + "?_p_form="
            + this.dataComponentName.replace('G', '') + "&_p_action=export&_p_exp_format="
            + ((p_format) ? p_format : "csv") + qs + cs + ss, 'Export',
        'adress=yes,width=710,height=450,scrollbars=yes,resizable=yes,menubar=yes');
    v.focus();
  },

  deleteRecord                 : function() {
    if (this.getSelectionModel().getCount() < 1) {
      Ext.Msg.show({
            title   : '',
            msg     : 'No records selected. Nothing to delete.',
            buttons : Ext.Msg.OK,
            fn      : this.execute_delete,
            icon    : Ext.MessageBox.WARNING
          });
    } else {
      Ext.Msg.show({
            title   : 'Confirm delete',
            msg     : 'Are you sure you want to delete this record?',
            buttons : Ext.Msg.YESNO,
            fn      : this.execute_delete,
            scope   : this,
            icon    : Ext.MessageBox.QUESTION
          });
    }
  },

  execute_delete               : function(btn) {
    if (btn == 'yes') {
      Ext.Ajax.request({
            url     : CFG_BACKENDSERVER_URL + "?_p_form=" + this.dataComponentName.replace('G', '')
                + "&_p_action=delete",
            success : this.after_execute_delete_success,
            failure : this.after_execute_delete_failure,
            scope   : this,
            params  : this.getSelectedRowPK()
          });
    }
  },

  after_execute_delete_success : function(response, options) {
    var resp = Ext.decode(response.responseText);
    if (resp.success) {
      var removed = this.getSelectionModel().getSelected();
      var create = false;
      if (this.getSelectionModel().hasNext()) {
        this.getSelectionModel().selectNext();
      } else if (this.getSelectionModel().hasPrevious()) {
        this.getSelectionModel().selectPrevious();
      } else {
        create = true;
      }
      this.getStore().remove(removed);
      this.getStore().commitChanges();
      if (create) {
        this.createNewRecord();
      }
    } else {
      if (resp.message) {
        Ext.Msg.alert('Error', this.urldecode(resp.message));
      } else {
        Ext.Msg
            .alert('Error',
                'Error deleting this record with no message from server. Contact system administrator.');
      }
    }

  },

  after_execute_delete_failure : function(response, options) {
    try {
      Ext.Msg.alert(response.statusText, response.responseText);
    } catch (e) {
      Ext.Msg.alert('Error', 'Browser internal error.');
    }
  },

  reSelectCurrent              : function() {
    var crt = this.getSelectionModel().getSelected();
    this.getSelectionModel().clearSelections();
    this.getSelectionModel().selectRecords([crt]);
  }
});

Ext.reg('N21GridView', app.base.GridView);