Ext.ns('app.base');

app.base.GridEditForm = Ext.extend(Ext.Panel, {
   dataComponentName:null
  ,detailWindow:null
  ,masterName: null
  ,detailName: null
  ,parentDcRelation: null
  ,mdLayout:'card' // can be : row/column/tab/window/card
  ,detailTabRendered:false
  ,mainToolbar: null

  ,initComponent:function() {
     Ext.apply(this, arguments); // eo apply
     app.base.GridEditForm.superclass.initComponent.apply(this, arguments);
     if (this.parentDcRelation != null) {
        for (var j=0;j<this.parentDcRelation.relation.length; j++ ) {
          this.getRecordEditor().fields.get(this.parentDcRelation.relation[j].child).copyValueFrom =  this.parentDcRelation.name+'_'+ this.parentDcRelation.relation[j].parent;
        }
     }
   }

  ,initEvents: function() {
    app.base.GridEditForm.superclass.initEvents.call(this);
    this.getRecordList().getSelectionModel().on('rowselect', this.onRowSelect, this);

    this.getRecordList().on('keydown', this.onGridKeydown, this);
    this.getRecordList().on('rowdblclick', this.onRowDblClick, this);

    this.getRecordList().on('executeQuerySuccess',this.afterListExecuteQuerySuccess, this);
    this.getRecordList().on('createNewRecord',this.createNewRecord, this);

    this.getRecordEditor().on('commitFormSuccess',this.afterCommitFormSuccess, this);

    this.getRecordEditor().on('loadPrevRecord',this.goToPrevRecord, this);
    this.getRecordEditor().on('loadNextRecord',this.goToNextRecord, this);
    this.getRecordEditor().on('deleteRecord',this.deleteRecord, this);

    var keyMap = new Ext.KeyMap( this.body, [
        { key: Ext.EventObject.Q, fn: this.close_detail,  ctrl:true, scope:this }
       ,{ key: Ext.EventObject.F7, fn: this.enterQuery,   ctrl:false, scope:this }
       ,{ key: Ext.EventObject.F8, fn: this.executeQuery,   ctrl:false, scope:this }
       ,{ key: Ext.EventObject.N, fn: this.createNewRecord, ctrl:true, scope:this }
       ,{ key: Ext.EventObject.S, fn: this.commitForm, ctrl:true, scope:this }
    ]);
    keyMap.stopEvent = true;

  }

  // ----------- private properties
  ,reloadRecord: function () {
    this.getRecordEditor().fetchRecord(this.getRecordList().getSelectedRowPK());
  }

  ,goToPrevRecord: function() {  //  alert('in goToPrevRecord');
     if (this.getRecordList().getStore().getCount()==1) {
       this.reSelectCurrent();
     } else {
       this.getRecordList().getSelectionModel().selectPrevious(false);
       this.fetchAllChildRecords();
     }
  }

  ,goToNextRecord: function() {  //   alert('in goToNextRecord');
     if (this.getRecordList().getStore().getCount()==1) {
       this.reSelectCurrent();
     } else{
       this.getRecordList().getSelectionModel().selectNext(false);
       this.fetchAllChildRecords();
     }
  }

  ,reSelectCurrent:function () {
     this.getRecordList().reSelectCurrent();
  }

  ,afterListExecuteQuerySuccess:function(listCmp, fetchedRecCount, firstRec) {
      if (fetchedRecCount>0) {
         this.loadRecord(firstRec);
      }else {
          this.getRecordEditor().disableAllFields();
          //if (this.mdLayout == 'row' || this.mdLayout == 'column' ) {
          //   this.createNewRecord();
          //}
      }
  }

  ,toggleEditMode: function() {
     if (this.getTopToolbar().items.get("tlb_LIST_EDITOR_MODE").pressed) {
       this.close_detail();
       this.toggleEditorToolbarItem(false);
       this.enableDisableToolbarItems("list");
     }
     else {
       this.onRowDblClick();
       this.toggleEditorToolbarItem(true);
       this.enableDisableToolbarItems("editor");
     }
  }

  ,enableDisableToolbarItems: function(vMode) {
     if (this.mdLayout != "tab" && this.mdLayout != "card") {return ;}
     if(vMode == "list") {
       this.getTopToolbar().items.get("tlb_SAVE").disable();
       this.getTopToolbar().items.get("tlb_FILTER").enable();
       this.getTopToolbar().items.get("tlb_PRINT").enable();
       this.getTopToolbar().items.get("tlb_EXP_CSV").enable();
     } else {
       this.getTopToolbar().items.get("tlb_SAVE").enable();
       this.getTopToolbar().items.get("tlb_FILTER").disable();
       this.getTopToolbar().items.get("tlb_PRINT").disable();
       this.getTopToolbar().items.get("tlb_EXP_CSV").disable();
     }
  }

  ,toggleEditorToolbarItem: function(p) {
    this.getTopToolbar().items.get("tlb_LIST_EDITOR_MODE").toggle(p);
  }

  ,getRecordEditor:function() {
     return this.findById(this.detailName);
  }
  ,getRecordList:function() {
     return this.findById(this.masterName);
  }

  ,getDataRecord:function() {
     return this.getRecordEditor().dataRecord;
  }

  ,getFieldValue: function(fieldName) {
    return this.getRecordEditor().getFieldValue(fieldName);
  }

  ,close_detail: function() {
     if (this.mdLayout == "tab") {
       this.getComponent("MDTab").activate(0);
     }
     if (this.mdLayout == "card") {
       this.getComponent("MDTab").layout.setActiveItem(0);
     }
     if (this.mdLayout == "window") {
       this.detailWindow.hide();  // alert(this.masterName);
       //Ext.getCmp(this.masterName).focus(false,200);
     }
     this.toggleEditorToolbarItem(false);
     this.enableDisableToolbarItems("list");
     //this.getRecordList().getView().focusRow(this.getRecordList().getSelectionModel()....?);
     //TODO 有问题，会报错
     //this.getRecordList().getView().focusRow(0);
  }

  ,onRowSelect: function(sm, rowIdx, r) {
     this.loadRecord(r);
  }

  ,loadRecord:function(r) {
     this.getRecordEditor().loadRecord(r);
     this.clear_details();
     this.synchronize_master_detail();
   }

  ,synchronize_master_detail:function() {
      this.getRecordEditor().synchronize_master_detail();
  }


  ,setDefaultFormFocus: function() {
    if (this.getRecordEditor().firstFocusFieldName != null) {
       this.getRecordEditor().getForm().findField(this.getRecordEditor().firstFocusFieldName).focus();
    }
  }

  ,onRowDblClick: function(grid, rowIndex, evnt) { //return ;
     //if the layout of grid and form is tabs => show form
     // in this situation we're stucked as rowselect is not fired
     if (this.getRecordList().getStore().getCount()==1 && this.getRecordEditor().dataRecord.get("_p_record_status")=="insert" ) {
       this.reSelectCurrent();
     }
     if (this.mdLayout == "tab") {
       this.getComponent("MDTab").activate(1); // alert(this.detailTabRendered);
       if (!this.detailTabRendered) {  // workaround to show content of nested panels and fieldsets, necessary only on first activation of the detail tab
          this.getComponent("MDTab").doLayout();
          this.detailTabRendered = true;
       }
       this.getRecordList().getSelectionModel().fireEvent('rowselect');   // is necessary for the first activation of the form tab
       // *****************************************************************************************************
       //todo: !!! add a new parameter: fetchChildDataOn : masterSelection - when the master record is changed
       // check the paramter for each child DC and call this.getRecordEditor().populate_details(block_name);
       this.getRecordEditor().populate_all_details();
       // *****************************************************************************************************
     }else if (this.mdLayout == "window") {
       this.detailWindow.show();
       this.getRecordList().getSelectionModel().fireEvent('rowselect');  // is necessary for the first activation of the form tab
     }else if (this.mdLayout == "card") {
       this.getComponent("MDTab").layout.setActiveItem(1);
       //this.getComponent("MDTab").items.get(1).doLayout();
       this.getRecordList().getSelectionModel().fireEvent('rowselect');  // is necessary for the first activation of the form tab
       //TODO: see at tab layout
       this.getRecordEditor().populate_all_details();
     }  else {
         this.getRecordEditor().populate_all_details(); //form is visible with list, on record selection must load details
     }
     this.toggleEditorToolbarItem(true);
     this.enableDisableToolbarItems("editor");
     this.setDefaultFormFocus();
  }

  ,onGridKeydown: function(evnt) {
     //if the layout of grid and form is tabs => show form
     if (evnt.getKey() == Ext.EventObject.ENTER) {
       // in this situation we're stucked as rowselect is not fired
       if (this.getRecordList().getStore().getCount()==1 && this.getRecordEditor().dataRecord.get("_p_record_status")=="insert" ) {
         this.reSelectCurrent();
       }

       if (this.mdLayout == "tab") {
           this.getComponent("MDTab").activate(1);
           if (!this.detailTabRendered) {   // workaround to show content of nested panels and fieldsets, necessary only on first activation of the detail tab
              this.getComponent("MDTab").doLayout();
              this.detailTabRendered = true;
           }
           this.getRecordList().getSelectionModel().fireEvent('rowselect');  // is necessary for the first activation of the form tab
             // *****************************************************************************************************
             //todo: !!! add a new parameter: fetchChildDataOn : masterSelection - when the master record is changed
             // check the paramter for each child DC and call this.getRecordEditor().populate_details(block_name);
             this.getRecordEditor().populate_all_details();
             // *****************************************************************************************************

       }else if (this.mdLayout == "window") {
           this.detailWindow.show();
           // is necessary for the first activation of the form tab
           this.getRecordList().getSelectionModel().fireEvent('rowselect');
       }else if (this.mdLayout == "card") {
         this.getComponent("MDTab").layout.setActiveItem(1);
         this.getComponent("MDTab").doLayout();
         this.getRecordList().getSelectionModel().fireEvent('rowselect');  // is necessary for the first activation of the form tab
         //TODO: see at tab layout
         this.getRecordEditor().populate_all_details();
       } else {
            this.getRecordEditor().populate_all_details();
       }
       this.toggleEditorToolbarItem(true);
       this.enableDisableToolbarItems("editor");
       this.setDefaultFormFocus();
     }
  }

  ,fetchChildRecords: function(childName) {
     Ext.getCmp(childName).executeQuery();
  }

  ,fetchAllChildRecords: function() {
      for (var i=0;i<this.getRecordEditor().childDCs.length;i++) {
       Ext.getCmp(this.getRecordEditor().childDCs[i].name).executeQuery();
     }
  }

  ,enterQuery:function() {
      this.getRecordList().enterQuery();
  }

  ,commitForm: function() {
    this.getRecordEditor().commitForm();
  }

  ,afterCommitFormSuccess:function(recordEditor,operation) {
      if (operation=="insert") {
        this.getRecordList().getStore().add([this.getRecordEditor().dataRecord]);
        this.getRecordList().getSelectionModel().selectLastRow();
      }else {

      }
      this.getRecordList().getStore().commitChanges();
      this.getRecordEditor().setDefaultFormFocus();
  }

  ,createNewRecord: function() {
     if (this.mdLayout == "tab") {
       this.getComponent("MDTab").activate(1);
       if (!this.detailTabRendered) {   // workaround to show content of nested panels and fieldsets, necessary only on first activation of the detail tab
          this.getComponent("MDTab").doLayout();
          this.detailTabRendered = true;
       }
       this.toggleEditorToolbarItem(true);
       this.enableDisableToolbarItems("editor");
     } else if (this.mdLayout == "card") {
         this.getComponent("MDTab").layout.setActiveItem(1);
         //this.getComponent("MDTab").doLayout();
         this.toggleEditorToolbarItem(true);
         this.enableDisableToolbarItems("editor");
       }
     this.getRecordEditor().createNewRecord();
  }

  ,deleteRecord: function(recStatus) {
     if (recStatus == "insert") {
       this.reSelectCurrent();
     } else {
        this.getRecordList().deleteRecord();
     }
  }

  ,executeQuery: function() {
      this.getRecordList().executeQuery();
   }

  ,exportHtml: function() {
     this.exportList("html");
  }
  ,exportCsv: function() {
     this.exportList("csv");
  }
  ,exportList: function(pFormat) {
     this.getRecordList().export_data(pFormat);
  }

  ,urldecode: function ( str ) {
    var ret = str;
    ret = ret.replace(/\+/g, "%20");
    ret = decodeURIComponent(ret);
    ret = ret.toString();
    return ret;
  }

   ,clear_records:function() {
     this.getRecordList().getStore().removeAll();
     this.getRecordEditor().getForm().reset();
   }

   ,clear_details:function() {
     this.getRecordEditor().clear_details();
   }

   ,setQueryFieldValue:function(fieldName, fieldValue) {
     this.getRecordList().setQueryFieldValue(fieldName, fieldValue);
   }
   ,focus: function() {
      this.getRecordList().getSelectionModel().selectFirstRow();
      this.getRecordList().focusRow(0);
   }
});

Ext.reg('N21GridEditForm', app.base.GridEditForm);