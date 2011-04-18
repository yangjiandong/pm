

Ext.namespace('app.base');


app.base.Lov = Ext.extend(Ext.form.TriggerField, {
    defaultAutoCreate : {tag: "input", type: "text", size: "16",style:"cursor:default;", autocomplete: "off"}
    ,triggerClass: 'x-form-search-trigger'
    ,validateOnBlur: false

    ,dataComponentName: null

    ,queryFields: []
    ,queryFieldsMap: null
    ,defQueryField:''

    , params:{}
    ,callFromGrid: null
    ,callFromGridRow:null

    // LOV window width
    ,lovWidth: 300
    // LOV window height
    ,lovHeight: 300
    // LOV window title
    ,lovTitle: ''
    // Multiple selection is possible?
    ,multiSelect: false

    // If this option is true, data store reloads each time the LOV opens
    ,alwaysLoadStore: false
    
    // LOV data provider, intance of Ext.grid.GridPanel or Ext.DataView
   , view: {}

    ,displayValue: ''
   , hiddenValue: ''

   , fieldMapping:[]
    // example: fieldMapping:[{column:'ID',field:'DC0014_ID'},{column:'CODE',field:'DC0014_CODE'}]

   , paramMapping:[]
    // example: paramMapping:[{param:'p_client_id',field:'DC0014_CLIENT_ID'},{param:'p_bpartner_id',field:'DC0014_BPARTNER_FROM'}]
    ,queryArraySize:-1
    ,window:null



    // Which data store field will use for return
    ,valueField: 'id'
    // Which data store field will use for display
    ,displayField: 'id'





    // If multiple items are selected, they are joined with this character
    ,valueSeparator: ','
    ,displaySeparator: ','

    // LOV window configurations
    // autoScroll, layout, bbar and items configurations are not changed by this option
    ,windowConfig: {}

    ,showOnFocus : false

    ,minItem : 0
    ,minItemText : 'The minimum selected item number for this field is {0}'

    ,maxItem : Number.MAX_VALUE
    ,maxItemText : 'The maximum selected item number for this field is {0}'

    // Private
    ,isStoreLoaded: false
    // Private
   ,selections: []
    // Private
   , selectedRecords: []

    , initComponent: function(){

        Ext.apply(this,arguments);

        this.queryFieldsMap = [
           new Ext.form.ComboBox({xtype:'combo', value:this.defQueryField,selectOnFocus:true,   name: this.dataComponentName+'_QRY_FIELD',hiddenName:  this.dataComponentName+'_QRY_FIELD_ID', hiddenId:  this.dataComponentName+'_QRY_FIELD_ID',  width:120, store: this.queryFields /*getQueryFieldLabels() */   })
          ,new Ext.form.TextField({xtype:'textfield',selectOnFocus:true, name: this.dataComponentName+'_QRY_VALUE',  width:150  })
        ];
        //alert(this.queryFieldsMap[0]);

        if((this.view.xtype != 'grid' && this.view.xtype != 'dataview') &&
        (!(this.view instanceof Ext.grid.GridPanel) && !(this.view instanceof Ext.DataView))){
            throw "app.base.Lov.view option must be instance of Ext.grid.GridPanel or Ext.DataView!";
        }
        app.base.Lov.superclass.initComponent.call(this);

        this.viewType = this.view.getXType();
        if(this.viewType == 'grid' && !this.view.sm){
            this.view.sm = this.view.getSelectionModel();
        }

        if(this.viewType == 'grid'){
            this.view.sm.singleSelect = !this.multiSelect;
        }else{
            this.view.singleSelect = !this.multiSelect;
            this.view.multiSelect = this.multiSelect;
        }

    }



  ,initEvents: function() {
     app.base.Lov.superclass.initEvents.call(this);
    this.on('specialkey', this.onSpecialKeyClick, this);
    this.view.on('keypress', this.onViewKeypress , this);
    this.view.on('dblclick', this.onDoSelect, this);
    this.view.getStore().proxy.on('loadexception', this.onLoadException, this);
    this.view.getStore().proxy.on('load', this.onLoadProxy, this);
  }

 ,onLovClose: function() {
      this.focus();
   }

 ,onLoadException:function(proxy,options,response,err) {
    if (err=!null && err != undefined) {
      Ext.Msg.alert(err.name,err.message );
    } else {
       try{
        Ext.Msg.alert(response.statusText, response.responseText );
       } catch (e) {
          Ext.Msg.alert(e.name, e.message);
          throw e;
       }
    }
    return false;
  }
  

  ,onLoadProxy:function(proxy, obj, arg, result) {
    if (this.view.getStore().reader instanceof Ext.data.JsonReader ) {
      for (var i=0;i<result.records.length ; i++ )  {
        for (v in result.records[i].data) {
          if (Ext.type(result.records[i].get(v)) == 'string')
            result.records[i].set(v, this.urldecode(result.records[i].get(v) )) ;
        }
        result.records[i].commit();
      }
    }
  }
  
   ,urldecode: function ( str ) {
    try{
      var ret = str;
      ret = ret.replace(/\+/g, "%20");
      ret = decodeURIComponent(ret);
      ret = ret.toString();
      return ret;
    } catch(e) {
      return str;
    }
  }


  ,onViewKeypress: function( evnt) {  //  alert(evnt.getKey());
   if (evnt.getKey() == 13 ) { // arrow down => open lov
       this.onDoSelect();
    }
  }

  ,onSpecialKeyClick: function(field, evnt) {
   if (evnt.getKey() == Ext.EventObject.DOWN ) { // arrow down => open lov
       this.showLov();
    }
    return false;
  }


  ,showLov:function() {    
    this.renderWindow();
    if (!this.isStoreLoaded) {
      this.executeQuery();
      this.window.show();
    } else {
      this.window.show();
      if (this.view.store.getCount()>0) {
         this.view.getView().focusRow(0);
       }
    }

  }

    , onRender: function(ct, position){
    	if (this.isRendered) {
            return;
        }

       // this.readOnly = true;

        if(this.textarea){
            this.defaultAutoCreate = {tag: "textarea", style:"cursor:default;", autocomplete: "off", value: this.displayValue};
            this.value = this.displayValue;
            this.displaySeparator = '\n';
        }

        app.base.Lov.superclass.onRender.call(this, ct, position);

        if(this.showOnFocus){
            this.on('focus',this.onTriggerClick,this);
        }
        
        this.renderWindow();
        this.isRendered = true;
    }

   , validateValue : function(value){
        if( app.base.Lov.superclass.validateValue.call(this, value)){
            if(this.selectedRecords.length < this.minItem){
                this.markInvalid(String.format(this.minItemText, this.minItem));
                return false;
            }
            if(this.selectedRecords.length > this.maxItem){
                this.markInvalid(String.format(this.maxItemText, this.maxItem));
                return false;
            }
        }else{
            return false;
        }
        return true;
    }

    ,getSelectedRecords : function(){
        if(this.viewType == 'grid'){
            this.selections = this.selectedRecords = this.view.sm.getSelections();
        }else{
            this.selections = this.view.getSelectedIndexes();
            this.selectedRecords = this.view.getSelectedRecords();
        }

        return this.selectedRecords;
    }



    ,clearSelections : function(){
        return (this.viewType == 'grid')? this.view.sm.clearSelections() : this.view.clearSelections();
    }



   , select : function(selections){
        if(this.viewType == 'grid'){
            if(selections[0] instanceof Ext.data.Record){
                this.view.sm.selectRecords(selections);
            }else{
                this.view.sm.selectRows(selections);

            }
        }else{
            this.view.select(selections);
        }
    }

    ,onDoRefresh: function() {
      this.isStoreLoaded = false;
      this.executeQuery();
    }


    ,executeQuery:function() {
       var p = new Object();
       this.isStoreLoaded = false;
       var newParamVal;


    // ---------------------

    // if (this.callFromGrid) {
    //   newParamVal = (!Ext.isEmpty(this.paramMapping[i].field))? this.callFromGrid.store.getAt(this.callFromGridRow).get( this.paramMapping[i].field ):this.paramMapping[i].value;
    // } else {
       for(var i=0;i<this.paramMapping.length; i++ ) {
         if (this.paramMapping[i].field) {
           if (this.paramMapping[i].field.indexOf(".")<0) {
             if (!Ext.isEmpty(this.callFromGrid)) {
               newParamVal = this.callFromGrid.store.getAt(this.callFromGridRow).get(this.paramMapping[i].field);
             } else {
               newParamVal = Ext.getCmp(this.paramMapping[i].field).getValue();
             }
           }else {
            var dc = this.paramMapping[i].field.substring(0,this.paramMapping[i].field.indexOf("."));
            var fld = this.paramMapping[i].field.substring(this.paramMapping[i].field.indexOf(".")+1);
            newParamVal = Ext.getCmp(dc).getFieldValue(fld);
           }

         } else {
           newParamVal = this.paramMapping[i].value;
         }
         p[this.paramMapping[i].param] =  newParamVal;
       }

     // ---------------------
 
      p['_p_query_column'] = this.queryFieldsMap[0].getValue();
      //p['_p_query_column_code'] = Ext.getCmp(this.dataComponentName+'_QRY_FIELD_ID').getValue();
      p['_p_query_value'] = this.queryFieldsMap[1].getValue();


       this.view.store.baseParams = p;

       if (!this.isStoreLoaded) {
         this.view.store.load({callback:this.afterExecuteQuery,scope:this});
       }

    }


    
  ,afterExecuteQuery: function(r,options,success) {
    if (success) {
       this.isStoreLoaded = true;
       if (this.view.store.getCount()>0) {
         this.view.getSelectionModel().selectFirstRow();
         this.view.getView().focusRow(0);
       }
     }
  }
  
  

  ,onBlur:function () {
     if (!this.getValue()) {
        for (var i=0;i<this.fieldMapping.length;i++ ) {
          if (!Ext.isEmpty(this.callFromGrid)) {
             //this.callFromGrid.store.getAt(this.callFromGridRow).set(this.fieldMapping[i].field,"");
           } else {
             Ext.getCmp(this.fieldMapping[i].field).setValue("");
           }
        }
     }
  }


    ,onDoSelect: function(){

        //TODO:  fix for Tpl

         if (this.getSelectedRecords().length == 0 ) {
           Ext.Msg.alert('Warning', 'No record selected.');
           return false;
         }

        Ext.form.TriggerField.superclass.setValue.call(this, this.getSelectedRecords()[0].get(this.displayField));

        for (var i=0;i<this.fieldMapping.length;i++ ) {
          if (!Ext.isEmpty(this.callFromGrid)) {
            this.callFromGrid.store.getAt(this.callFromGridRow).set(this.fieldMapping[i].field, this.getSelectedRecords()[0].get(this.fieldMapping[i].column));
          }
          else {
            Ext.getCmp(this.fieldMapping[i].field).setValue(this.getSelectedRecords()[0].get(this.fieldMapping[i].column));
          }
        }

        if(Ext.QuickTips){ // fix for floating editors interacting with DND
            Ext.QuickTips.enable();
        }
        this.window.hide();
        this.focus();
    }

    ,onDoCancel: function() {
        this.select(this.selections);
        this.window.hide();
        this.focus();
    }

    ,prepareValue:function(sRec){
        this.el.dom.qtip = '';
        if (sRec.length > 0) {
            var vals = {"hv": [],"dv": []};
            Ext.each(sRec, function(i){
                vals.hv.push(i.get(this.valueField));
                if (this.displayFieldTpl) {
                    vals.dv.push(this.displayFieldTpl.apply(i.data));
                } else {
                    vals.dv.push(i.get(this.displayField));
                }
                if(this.qtipTpl){
                    this.el.dom.qtip += this.qtipTpl.apply(i.data);
                }
            }, this);
            return vals;
        }
        return false;
    }


   , onTriggerClick: function(e){
       if (!Ext.isEmpty(this.callFromGrid)) {
         this.callFromGridRow = this.callFromGrid.getSelectionModel().selection.cell[0];
       }
       this.showLov();
       return false;
    }

    ,getQueryFieldLabels: function() {
       var qfl = new Array();
       for (var i=0; i< this.queryFields.length; i++ ) {
        qfl[qfl.length] = [this.queryFields[i].code, this.queryFields[i].label ];
       }
       return qfl;
    }

 

   , renderWindow: function(){

        if(!this.window){

          //alert(this.dataComponentName);
          this.windowConfig = Ext.apply(this.windowConfig, {
             title: this.lovTitle
            ,width: this.lovWidth
            ,height: this.lovHeight
            ,modal: true
            ,autoScroll: true
            ,closable: true
            ,closeAction: 'hide'
            ,layout: 'fit'
            ,items: this.view
            ,tbar: new Ext.Panel({
                      autoScroll:true
                     ,border:true
                     ,bodyBorder :false
                     ,height:0
                     ,bbar:
                        [
                           {text:'Select', handler:this.onDoSelect, scope:this, xtype:"button", cls:"x-btn-text-icon", icon:"resource/icon/lov_select.gif"}
                          ,{text:'Cancel', handler:this.onDoCancel, scope:this, xtype:"button", cls:"x-btn-text-icon", icon:"resource/icon/lov_cancel.gif"}
                        //  ,'->'
                         // ,{text:'Refresh', handler:this.onDoRefresh, scope:this, xtype:"button", cls:"x-btn-text-icon", icon:"_static/icon/lov_refresh.gif"}
                        ]
                     ,frame:false
                     ,layout:'table'
                     ,layoutConfig: {columns: this.queryPanelColCount,cellCls:'vertical-align:top;'}
                     ,defaults:{labelWidth:90, labelAlign:'right'}
                     ,tbar:[
                              {xtype:'label', text :'Filter on:'}
                              ,this.queryFieldsMap[0]
                              ,this.queryFieldsMap[1]
                             ,{xtype:'button', text:'Ok', handler:this.executeQuery, scope:this }
                     ]
                   })

        },{shadow: true, frame: true});

       if (this.queryArraySize != null && this.queryArraySize != -1) {

          this.windowConfig.bbar = new Ext.PagingToolbar({
             store:this.view.store
            ,displayInfo:true
            ,pageSize:this.queryArraySize
            });
         }
            this.window = new Ext.Window(this.windowConfig);
            this.window.on('beforeclose', function(){
                this.window.hide();
                return false;
            }, this);

            this.window.on('hide', this.onLovClose, this);

        }


    }
});

Ext.reg('xlovfield', app.base.Lov);