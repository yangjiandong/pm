Ext.ns('app.base');

app.base.EditForm = Ext.extend(Ext.form.FormPanel, {
   dataRecord: null
  ,sysName:''//模块名
  ,dataComponentName:null
  ,firstFocusFieldName:null
  ,firstFocusFieldNameInsert:null
  ,fields: new Ext.util.MixedCollection()
  ,childDCs:new Array()   //Array

  ,initComponent:function() {

     Ext.apply(this, arguments); // eo apply
     this.header=false;
     this.hideBorders =true;
     this.trackResetOnLoad=true;
     app.base.EditForm.superclass.initComponent.apply(this, arguments);
     this.addEvents('commitFormSuccess', 'commitFormFailure', 'loadNextRecord', 'loadPrevRecord', 'deleteRecord');

     this.onInitForm();
   }
 ,onInitForm:function() {

 }

 ,initEvents: function() {
    app.base.EditForm.superclass.initEvents.call(this);

    var keyMap = new Ext.KeyMap( this.body, [
       { key: 83, fn: this.commitForm,   ctrl:true, scope:this }/* Ctrl+S */
    ]);
    keyMap.stopEvent = true;
  }

  /*
  ,fetchPrevFromDs: function() { // alert(-1);
     this.fireEvent("loadPrevRecord",this);
     // this.populateAllDetails();
  }

  ,fetchNextFromDs: function() {// alert(+1);
     this.fireEvent("loadNextRecord",this);
    // this.populateAllDetails();
  }
    */
  ,loadRecord: function (record) { //
     if (record!=null && record != undefined) {
       this.dataRecord =  record;
     }
     if (this.dataRecord != null) {
       this.getForm().loadRecord(this.dataRecord);
       if (this.dataRecord.get("_p_record_status") == "insert") {
          this.fields.eachKey( this.checkInsertAllowed, this );
       }else {
          this.fields.eachKey( this.checkUpdateAllowed, this );
       }

     }
  }
  ,disableAllFields: function() {
    this.fields.each( this.disable);
  }
  ,deleteRecord: function() {
     if (this.dataRecord.data._p_record_status == "insert") {
        this.fireEvent("deleteRecord",this.dataRecord.data._p_record_status);
     }else {
        this.fireEvent("deleteRecord",this.dataRecord.data._p_record_status);
     }
  }

  ,checkUpdateAllowed:function(key,item) {
     try {
       if (item.update_allowed) {
         this.getForm().findField(key).enable();
       } else {
         this.getForm().findField(key).disable();
       }
     }catch(e) {/* maybe is a hidden field .. */}
  }


  ,checkInsertAllowed:function(key,item) {
     try {
       if (item.insert_allowed) {
         this.getForm().findField(key).enable();
       } else {
         this.getForm().findField(key).disable();
       }
      }catch(e) {/* maybe is a hidden field .. */}
  }


  ,updateRecord: function () { //
       this.getForm().updateRecord(this.dataRecord);
  }


  ,getFieldValue: function(fieldName) {
      return this.fields.get(fieldName).getValue();
  }


  ,setFieldValue: function(fieldName,newVal) {
      var oldVal = this.getForm().findField(fieldName).getValue();
      this.getForm().findField(fieldName).setValue(newVal);
      this.getForm().findField(fieldName).fireEvent('change',this.getForm().findField(fieldName),newVal,oldVal);
      if ( this.fields.get(fieldName).xtype == 'numberfield' )
        this.getField(fieldName).beforeBlur();
  }

  ,getField: function(fieldName) {
      return this.getForm().findField(fieldName);
  }

  ,clearDetails: function() {
     for (var i=0;i<this.childDCs.length;i++) {
         this.clear_block_details(this.childDCs[i].name);
      }
  }
  ,clear_details: function() {
    this.clearDetails();
  }


  ,clearBlockDetails:function(detailBlock) {
       var blockRelationIndex = -1;
        for (var i=0;i<this.childDCs.length;i++) {
          if  (this.childDCs[i].name == detailBlock)  {
            blockRelationIndex = i;
            break;
          }
        }
       if (blockRelationIndex != -1 ) {
         try {
           Ext.getCmp(detailBlock).clear_records();
         } catch (e) {
            Ext.Msg.alert('Framework error', 'Cannot clear detail block: '+detailBlock);
         }
       }
  }
  ,clear_block_details:function(detailBlock) {
      this.clearBlockDetails(detailBlock);
  }

  ,synchronizeMasterDetail:function() {
      for (var i=0;i<this.childDCs.length;i++) {

         for (var x=0; x<this.childDCs[ i ].relation.length; x++ ) {
           Ext.getCmp(this.childDCs[ i ].name).setQueryFieldValue(this.childDCs[ i ].relation[x].child, Ext.getCmp(this.dataComponentName+"_"+ this.childDCs[ i ].relation[x].parent).getValue() );
         }

      }
  }
  ,synchronize_master_detail:function() {
     this.synchronizeMasterDetail();
  }
  ,setRecordStatus: function(s) {
     if (s=="insert"||s=="update") {
       this.getForm().findField("_p_record_status").setValue(s);
       this.dataRecord.set("_p_record_status",s);
     }
    // alert(this.getRecordStatus());
  }

  ,getRecordStatus: function() {
     if ( this.getForm().findField("_p_record_status").getValue() == "insert") return "insert";   else return "update";
  }

// --------------------------- commitForm ----------------------

   /*
    * Config object properties: see each function to see specific properties
    */
  ,executeTrigger: function(tn,config) {
    if (tn=="COMMIT_FORM") { this.commitForm(config); }
    if (tn=="CREATE_NEW_RECORD") { this.createNewRecord(config); }
  }


   /* Config object properties:
    *   onSuccess: callback for successfully execution
    *   onFailure: callback for execution failure
    */
  ,commitForm: function(config) {
    if (this.getForm().isValid()) {
      if (this.getForm().findField("_p_record_status").getValue() == "insert") {_p_action="insert"}  else {_p_action="update";}
      //url = CFG_BACKENDSERVER_URL+"?_p_action="+_p_action+"&_p_form="+this.dataComponentName.replace('F','');
      // 直接采用表单名
      url = this.dataComponentName.replace('F','') + "_save.htm";
      if (this.sysName != ''){
        url = this.sysName+"_"+url;
      }
      this.getForm().submit({ _p_config:config, url:url ,scope: this, success: this.afterCommitSuccess, failure: this.afterCommitFailure, waitMsg:'保存数据...'});
    }else {
      Ext.Msg.alert('Error', 'Form contains empty/invalid data');
    }
  }


  ,afterCommitSuccess: function(form,action) {
      var op = "";
      if (this.getForm().findField("_p_record_status").getValue() == "insert") {
        op = "insert";
        this.getForm().findField("_p_record_status").setValue("");
        //syncronize the local dataRecord with the values returned from server
        for (v in this.dataRecord.data) {
             try {
               this.getForm().findField(v).setValue(action.result.data[v]);
             } catch(e) {}
          }
      } else {
        op = "update";
        for (v in this.dataRecord.data) {
             try {
               this.getForm().findField(v).setValue(action.result.data[v]);
             } catch(e) {}
          }
      }
      this.updateRecord();
     // this.setDefaultFormFocus();

      this.fireEvent("commitFormSuccess", this, op );

      for (var i=0;i<this.childDCs.length;i++) {
         if (this.childDCs[ i ].commitChildWithParent) {
           Ext.getCmp(this.childDCs[ i ].name).commitForm();
         }
      }
      var p =  action.options._p_config.onSuccess;
      if( typeof p == "function" ) {
        p = p.call(this, action.result);
      }
      this.setDefaultFormFocus();
  }


  ,afterCommitFailure: function(form,action) {
     try{
        Ext.Msg.alert(action.response.statusText, action.response.responseText );
     } catch (e) {Ext.Msg.alert('Error', 'Browser internal error.');}
     this.fireEvent("commitFormFailure", this );
  }

  ,clearAllFieldsExcept:function(p){
    var f = this.fields.getRange();
    var et = "";  //excetion type
  //  alert(Ext.type( p));
    if( Ext.type( p ) == "array" ) {
      et = "array";
    }
  //  alert(et);       alert(p.length);     alert( ' EVENT_DATE-> ' +p.indexOf('EVENT_DATE'));
    for(var i=0;i<f.length;i++) { //  alert(f[i].name + ' -> ' +p.indexOf(f[i].name));
      if (!(et == "array" && p.indexOf(f[i].name) != -1)) {
        this.fields.get(f[i].name).setValue("");
      }
    }


  }

  ,setDefaultFormFocus: function() {
    if (this.getRecordStatus() == "insert") {
      if (this.firstFocusFieldNameInsert != null) {
         this.getForm().findField(this.firstFocusFieldNameInsert).focus();
       }else if (this.firstFocusFieldName != null) {
         this.getForm().findField(this.firstFocusFieldName).focus();
       }
    } else {
       if (this.firstFocusFieldName != null) {
         this.getForm().findField(this.firstFocusFieldName).focus();
       }
    }

  }


  ,populateAllDetails:function() {
      for (var i=0;i<this.childDCs.length;i++) {
          this.populate_details(this.childDCs[i].name);
        }
  }
  ,populate_all_details:function() {
      this.populateAllDetails();
  }

  ,populateDetails:function(detailBlock) {
        var blockRelationIndex = -1;
        for (var i=0;i<this.childDCs.length;i++) {
          if  (this.childDCs[i].name == detailBlock)  {
            blockRelationIndex = i;
            break;
          }
        }
         //alert(" alert(blockRelationIndex);="+blockRelationIndex);
       if (blockRelationIndex != -1 ) {
         Ext.getCmp(detailBlock).clear_records();
         for (var x=0; x<this.childDCs[ blockRelationIndex ].relation.length; x++ ) {
           Ext.getCmp(detailBlock).setQueryFieldValue(this.childDCs[ blockRelationIndex ].relation[x].child, Ext.getCmp(this.dataComponentName+"_"+ this.childDCs[ blockRelationIndex ].relation[x].parent).getValue() );
         }
         Ext.getCmp(detailBlock).executeQuery();
       }
  }

  ,populate_details:function(detailBlock) {
       this.populateDetails(detailBlock);
  }


  ,show_details:function(detailBlock) {
    this.windows.get(detailBlock).doLayout();
    this.windows.get(detailBlock).show();
    this.populate_details(detailBlock);
  }


  ,show_window:function(detailBlock) {
    this.layoutItems.get(detailBlock).show();
    this.populate_details(detailBlock);
  }

   /* Config object properties:
    *   remoteRecInit: call the remote record initialization procedure
    *   onSuccess: callback for successfully execution
    *   onFailure: callback for execution failure
    */
  ,createNewRecord: function(config) {

     this.dataRecord = this.newDataRecord();
     this.dataRecord.set("_p_record_status","insert");
     //this.updateRecord();
     this.loadRecord(this.dataRecord);
    // alert(this.dataRecord.get("_p_record_status"));
     //this.fields.each(this.initFields, this);
     var f= this.fields.getRange();
     for(var i=0;i<f.length; i++) {
       var item = f[i];
        if (!Ext.isEmpty(item.initialValue) ) {
            this.dataRecord.set(item.dataIndex, item.initialValue);
          } else if (!Ext.isEmpty(item.copyValueFrom) ) {
            this.dataRecord.set(item.dataIndex, Ext.getCmp(item.copyValueFrom).getValue());
          }
     }
    // this.fields.each(this.copyFieldValuesFrom, this);
    // alert(this.fields.get("_p_record_status").getValue());

    var doRemoteRecInit = (!Ext.isEmpty(config) && !config.remoteRecInit)?false:true;
     //alert(doRemoteRecInit);
     if (doRemoteRecInit) {
        this.getForm().load({ url:CFG_BACKENDSERVER_URL+"?_p_action=initRec&_p_form="+this.dataComponentName.replace('F',''),params:this.dataRecord.data
                          ,success: this.createNewRecordSuccess, failure: this.createNewRecordFailure, scope:this
                          ,waitMsg:'Initializing new record...'});

     }
  }


  ,createNewRecordSuccess: function(form, action) {
     //this.dataRecord = this.newDataRecord();
     //this.dataRecord.set("_p_record_status","insert");
     this.updateRecord();
     this.fields.each(this.copyFieldValuesFrom, this);
     this.loadRecord(this.dataRecord);
     this.clear_details();
     this.setDefaultFormFocus();  // set the focus

  }



  ,createNewRecordFailure: function(form, action) {
     try{
        Ext.Msg.alert(action.response.statusText, action.response.responseText );
     } catch (e) {Ext.Msg.alert('Error', 'Browser internal error.');}
  }



  ,copyFieldValuesFrom:function(item , idx, len) {
     //try {      // alert(idx+' '+item.dataIndex);
        if (!Ext.isEmpty(item.copyValueFrom) ) {
            this.dataRecord.set(item.dataIndex, Ext.getCmp(item.copyValueFrom).getValue());
          }
    // }catch(e) {}

  }

  ,fetchRecord:function(pk) {
     this.getForm().load({ url:CFG_BACKENDSERVER_URL+"?_p_action=fetchRecord&_p_data_format=json&_p_form="+this.dataComponentName.replace('F',''),params:pk //this.dataRecord.data
              ,success: this.fetchRecordSuccess, failure: this.fetchRecordFailure, scope:this
              ,waitMsg:'Loading...'});

  }

  ,fetchRecordSuccess: function(form, action) {
      for (v in this.dataRecord.data) {
           try {
             this.getForm().findField(v).setValue(action.result.data[v]);
           } catch(e) {}
        }
      this.updateRecord();
  }

  ,fetchRecordFailure: function(form, action) {
     try{
        Ext.Msg.alert(action.response.statusText, action.response.responseText );
     } catch (e) {Ext.Msg.alert('Error', 'Browser internal error.');}
  }


  ,callProcedure: function(procName, validateForm, fnSuccess, fnFailure ) {
     if (validateForm && !this.getForm().isValid()) {
        Ext.Msg.alert('Error', 'Form contains empty/invalid data');
        return false;
     }
     var f = this.fields;
     var fv = this.getForm().getValues(); //new Object();
    // for(var i = 0, len = f.keys.length; i < len; i++){
    //   fv[f.keys[i]] = f.items[i].getValue();
    // }
    Ext.Msg.wait("Working...");
     Ext.Ajax.request({
             params:fv
            ,method:"POST"
            ,callback:this.afterCallProcedure
            ,scope:this
            ,_p_callbackFnSuccess : fnSuccess
            ,_p_callbackFnFailure : fnFailure
            ,url:CFG_BACKENDSERVER_URL+"?_p_action=call_proc&_p_proc="+procName+"&_p_form="+this.dataComponentName.replace('F','')
        });
  }


  ,afterCallProcedure: function(options, success,response) {
      Ext.MessageBox.hide();
      if (!success) {
       try{

          Ext.Msg.alert(response.statusText, response.responseText );
       } catch (e) {Ext.Msg.alert('Error', 'Browser internal error.');}
      } else {
          var o = Ext.util.JSON.decode(response.responseText);
          if(!o.success) {
             Ext.Msg.alert('Error',this.urldecode(o.message) );
             var p =  options._p_callbackFnFailure;
             if( typeof p == "function" ) {
                p = p.call(this, response);
             }
          } else {
             if (!Ext.isEmpty(o.message)) {
               Ext.Msg.alert('Message',this.urldecode(o.message) );
             }
             var p =  options._p_callbackFnSuccess;
             if( typeof p == "function" ) {
                p = p.call(this, response);
             }
          }
      }
  }

  ,run_report:function(config,param_list) {
     var paramQS = "";
     config.showParameterForm = config.showParameterForm || 'N';
     for (var j=0; j<param_list.length; j++ ) {
       for (var kk in param_list[j] ) {
         //alert(kk + ' ' + param_list[j][kk]);
         //alert(this.getForm().findField(this.fields.get(param_list[j][kk]).id).getValue());
         param_list[j][kk] = this.getForm().findField(this.fields.get(param_list[j][kk]).id).getValue();
         paramQS = paramQS + '' +  Ext.urlEncode( param_list[j] );
       }
     }
    // var w = window.open('http://localhost:8088/jasperserver/flow.html?_flowId=viewReportFlow&reportUnit=/reports/MyReports/'+config.reportId+'&output=pdf&'+paramQS, config.reportId, 'width=680,height=500,menubar=yes,scrolling=yes,adress=yes');
     //var w = window.open('runReport.php?_p_show_parameter_form='+config.showParameterForm+'&_p_report_id='+config.reportId+'&'+paramQS, config.reportId, 'width=680,height=500,menubar=yes,scrolling=yes');
    var w = window.open('http://localhost/n21eBusinessSuite/ServerPhp/testjasper.php?_p_report_id='+config.reportId+'&output=pdf&'+paramQS, config.reportId, 'width=680,height=500,menubar=yes,scrolling=yes,adress=yes');
     w.focus;
  }


   ,urldecode: function ( str ) {
    var ret = str;
    ret = ret.replace(/\+/g, "%20");
    ret = decodeURIComponent(ret);
    ret = ret.toString();
    return ret;
  }


});

 Ext.reg('N21EditForm', app.base.EditForm);