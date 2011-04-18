
Ext.ns('app.base');

app.base.Combo = Ext.extend(Ext.form.ComboBox, {

   dataComponentName:null
  ,isStoreLoaded:false
  ,expCboTask : null
     /*column from store which is displayed in combo and dropdown */
  ,displayColumn: null

     /* specify which columns to be returned in other fields or grid columns.  */
  ,fieldMapping:[]
     /* specify where to get the parameter values from */
  ,paramMapping:[]

     /* if the combo is a cell editor in a grid, this is the grid */
  ,callFromGrid: null
     /* row number where the combo is editing */
  ,callFromGridRow:null
  ,maxHeight :150
  ,initComponent:function() {
     Ext.apply(this, arguments);
     this.displayField = this.displayColumn;
     app.base.Combo.superclass.initComponent.apply(this, arguments);

  }



  ,initEvents: function() {
    app.base.Combo.superclass.initEvents.call(this);
    this.on('select', this.onSelectValue, this);
    this.on('focus', this.onFocus, this);
    this.store.proxy.on('loadexception', this.onLoadException, this);
    this.store.proxy.on('load', this.onLoadProxy, this);
  }



  ,onLoadProxy:function(proxy, obj, arg, result) {
    if (this.store.reader instanceof Ext.data.JsonReader ) {
      for (var i=0;i<result.records.length ; i++ )  {
        for (v in result.records[i].data) {
          if (Ext.type(result.records[i].get(v)) == 'string')
            result.records[i].set(v, this.urldecode(result.records[i].get(v) )) ;
        }
        result.records[i].commit();
      }
    }
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

 ,onSelectValue: function (cbo, rec, idx ) {
    if (this.fieldMapping.length > 0  ) {
        for (var i=0;i<this.fieldMapping.length;i++ ) {
          if (this.callFromGrid) {
            this.callFromGrid.store.getAt(this.callFromGridRow).set( this.fieldMapping[i].field ,rec.get(this.fieldMapping[i].column) )  ;
          }
          else {
            Ext.getCmp(this.fieldMapping[i].field).setValue( rec.get(this.fieldMapping[i].column) );
            Ext.getCmp(this.fieldMapping[i].field).fireEvent( "change");
          }
        }
    }
  }


  ,onFocus: function () {
    if (!Ext.isEmpty(this.callFromGrid)) {
       this.callFromGridRow = this.callFromGrid.getCurrentRowIndex();
     }
    var newParamVal;
    var oldParamVal;
    for (var i=0;i<this.paramMapping.length;i++) {

      if (this.callFromGrid) {
        newParamVal = (!Ext.isEmpty(this.paramMapping[i].field))? this.callFromGrid.store.getAt(this.callFromGridRow).get( this.paramMapping[i].field ):this.paramMapping[i].value ;
      } else {
        if (Ext.isEmpty(this.paramMapping[i].field)) {
          newParamVal = this.paramMapping[i].value;
        } else {
          if (this.paramMapping[i].field.indexOf(".")<0) {
            newParamVal = Ext.getCmp(this.paramMapping[i].field).getValue();
          } else {
            var dc = this.paramMapping[i].field.substring(0,this.paramMapping[i].field.indexOf("."));
            var fld = this.paramMapping[i].field.substring(this.paramMapping[i].field.indexOf(".")+1);
            newParamVal = Ext.getCmp(dc).getFieldValue(fld);
          }
        }
      }
      oldParamVal = this.store.baseParams[this.paramMapping[i].param];
      if (newParamVal != oldParamVal) {
            this.store.baseParams[this.paramMapping[i].param] = newParamVal;
            this.isStoreLoaded = false;
         }
    }
    if (!this.isStoreLoaded) {
      this.store.load({callback:this.afterLoad,scope:this});
      //app.base.combo.superclass.onFocus.apply(this, arguments);
      //return false;
    } else {
      app.base.Combo.superclass.onFocus.apply(this, arguments);
    }
  }


  ,onBlur: function () {
    if (!this.getRawValue()) {
      if (this.fieldMapping.length > 0  ) {
        for (var i=0;i<this.fieldMapping.length;i++ ) {
          if (this.callFromGrid) {
            this.callFromGrid.store.getAt(this.callFromGridRow).set( this.fieldMapping[i].field ,"" )  ;
          } else {
            Ext.getCmp(this.fieldMapping[i].field).setValue( "" );
            Ext.getCmp(this.fieldMapping[i].field).fireEvent( "change");
          }
        }
      }
    }
    app.base.Combo.superclass.onBlur.apply(this, arguments);
  }

  ,onTriggerClick:function() {
     if (!this.isStoreLoaded ) {
        this.expCboTask = new Ext.util.DelayedTask(this.onTriggerClick, this);
     }
     app.base.combo.superclass.onTriggerClick.apply(this, arguments);
  }


  ,afterLoad: function(records, options, success) {

     this.isStoreLoaded = true;
     if (success) {
       this.isStoreLoaded = true;
       //for the onFocus event after loading the store show the list
        if (this.expCboTask  != null ) {
            this.expCboTask.delay(50);
        }
       // setTimeout("this.expand", 100) ; // var x = this.isExpanded();
        //this.onTriggerClick();
       // var xx = new Ext.util.DelayedTask(this.onTriggerClick, this); //this.onFocus();
      //  xx.delay(200);
     } else {
       return false;
     }

  }


  ,setParamValue:function(paramName, paramValue) {  //alert('in BaseCombo.setParamValue'+ paramName +'='+paramValue);
    this.store.baseParams[paramName] = paramValue;
    this.isStoreLoaded = false;
    this.store.removeAll()
  }


  ,urldecode: function ( str ) {
    var ret = str;
    ret = ret.replace(/\+/g, "%20");
    ret = decodeURIComponent(ret);
    ret = ret.toString();
    return ret;
  }


});
Ext.reg('N21Combo', app.base.Combo);