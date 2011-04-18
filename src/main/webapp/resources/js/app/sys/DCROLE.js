Ext.ns("Sys.BaseData");
var ss = '';
if (CFG_DEPLOYMENT_TYPE == 'DEV') {
  ss = "<span class='dcName'>DCROLE</span>";
}

Sys.BaseData.DCROLE = Ext.extend(N21.Base.GridEdit, {
      dataRecordMeta : Ext.data.Record.create([{
            name : "_p_record_status",
            type : "string"
          }, {
            name : "id",
            type : "string"
          }, {
            name : "name",
            type : "string"
          }, {
            name : "desc",
            type : "string"
          }]),
      queryFields : new Ext.util.MixedCollection(),
      columnMap : new Ext.util.MixedCollection(),
      queryFieldsVisible : new Array(),
      queryPanelColCount : 1,
      recordPk : ["id"],
      layoutItems : new Ext.util.MixedCollection(),

      initComponent : function() {
        Ext.apply(this, {
              //对应后台产生的json格式
              store : new Ext.data.JsonStore({
                    id : "storeDCROLE",
                    totalProperty : "totalCount",
                    root : "rows",
                    url : "role/list",
                    remoteSort : true,
                    fields : this.dataRecordMeta
                  }),
              loadMask : true,
              tbar : new Array(new Ext.Toolbar.Button({
                        id : "tlb_FILTER",
                        xtype : "button",
                        cls : "x-btn-icon",
                        icon : CFG_PATH_ICONS + "/g_rec_src.png",
                        tooltip : "过滤",
                        handler : this.executeQuery,
                        scope : this
                      }), new Ext.Toolbar.Separator(), new Ext.Toolbar.Button({
                        id : "tlb_SAVE",
                        xtype : "button",
                        cls : "x-btn-icon",
                        icon : CFG_PATH_ICONS + "/g_rec_commit.png",
                        tooltip : "保存",
                        handler : this.commitForm,
                        scope : this
                      }), new Ext.Toolbar.Button({
                        id : "tlb_NEW",
                        xtype : "button",
                        cls : "x-btn-icon",
                        icon : CFG_PATH_ICONS + "/g_rec_new.png",
                        tooltip : "新增",
                        handler : this.createNewRecord,
                        scope : this
                      }), new Ext.Toolbar.Button({
                        id : "tlb_DELETE",
                        xtype : "button",
                        cls : "x-btn-icon",
                        icon : CFG_PATH_ICONS + "/g_rec_del.png",
                        tooltip : "删除",
                        handler : this.deleteRecord,
                        scope : this
                      }), new Ext.Toolbar.Separator(), new Ext.Toolbar.Button({
                        id : "tlb_PRINT",
                        xtype : "button",
                        cls : "x-btn-icon",
                        icon : CFG_PATH_ICONS + "/print.png",
                        tooltip : "打印",
                        handler : this.exportHtml,
                        scope : this
                      }), new Ext.Toolbar.Button({
                        id : "tlb_EXP_CSV",
                        xtype : "button",
                        cls : "x-btn-icon",
                        icon : CFG_PATH_ICONS + "/exp_excel.png",
                        tooltip : "导出",
                        handler : this.exportCsv,
                        scope : this
                      }), "->", ss),
              dataComponentName : "DCROLE",
              frame : true,
              queryArraySize : 20
            });

        this.columnMap.add("id", {
              id : 'id',
              header : "Id",
              width : 100,
              dataIndex : 'id',
              insert_allowed : true,
              update_allowed : true,
              hidden : true,
              sortable : true,
              editor : new Ext.form.TextField({
                    selectOnFocus : true,
                    allowBlank : false,
                    cls : "x-form-text-in-grid"
                  })
            });
        this.columnMap.add("name", {
              id : 'name',
              header : "角色",
              width : 150,
              dataIndex : 'name',
              insert_allowed : true,
              update_allowed : true,
              sortable : true,
              editor : new Ext.form.TextField({
                    selectOnFocus : true,
                    allowBlank : false,
                    //caseRestriction : "Upper",
                    //style : "text-transform:uppercase;",
                    cls : "x-form-text-in-grid"
                  })
            });
        this.columnMap.add("description", {
              id : 'desc',
              header : "备注说明",
              width : 200,
              dataIndex : 'description',
              insert_allowed : true,
              update_allowed : true,
              sortable : true,
              editor : new Ext.form.TextArea({
                    cls : "x-form-text-in-grid"
                  })
            });

        this.colModel = new Ext.grid.ColumnModel(this.columnMap.getRange());

        this.queryFields.add("id", new Ext.form.Hidden({
                  name : "QRY_id",
                  id : "DCROLEF_QRY_id",
                  fieldLabel : "Id",
                  allowBlank : true,
                  width : 100
                }));
        this.queryFields.add("name", new Ext.form.TextField({
                  name : "QRY_name",
                  id : "DCROLEF_QRY_name",
                  fieldLabel : "角色",
                  allowBlank : true,
                  //caseRestriction : "Upper",
                  //style : "text-transform:uppercase;",
                  width : 100
                }));

        this.queryFieldsVisible = ["name"];

        Sys.BaseData.DCROLE.superclass.initComponent.apply(this, arguments);
        //alert("ok");
      }

      ,
      onRender : function() {
        Sys.BaseData.DCROLE.superclass.onRender.apply(this, arguments);
      }

      ,
      newDataRecord : function() {
        return new this.dataRecordMeta({
              _p_record_status : "insert",
              id : "",
              name : "",
              description : ""
            });
      }

    });
Ext.reg("DCROLE", Sys.BaseData.DCROLE);
