Ext.application({
	requires : [ 'Ext.container.Viewport' ],
	name : 'AM',
	appFoldder: 'app',
	controllers: [ 'Users' ],
	launch : function() {
		Ext.create('Ext.container.Viewport', {
			layout : 'fit',
			items : [ {
				xtype : 'userlist',
				title : 'Users',
				html : 'List of users will go here'
			} ]
		});
	}
});