Ext.define('AM.view.user.List', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.userlist',
	title: 'All Users',
	store: 'Users',
    initComponent: function(){
    	this.tbar = [
	    	          {xtype:'button', text: 'Add', action: 'add'},
	    	          {xtype:'button', id: 'delete', text: 'Delete', action: 'delete', disabled:true}
	    	          ];
		this.columns = [
	                	{header: 'Id', dataIndex: 'id', flex:1},
	                	{header: 'First name', dataIndex: 'firstName', flex:1},
		                {header: 'Last name', dataIndex: 'lastName', flex:1},
		                {header: 'Email', dataIndex: 'mail', flex:1}];

		this.callParent(arguments);
	}
});