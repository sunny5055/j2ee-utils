Ext.define('AM.store.Users', {
	extend : 'Ext.data.Store',
	model : 'AM.model.User',
	autoLoad : true,
	proxy : {
		type : 'ajax',
		api : {
			read : 'user/view.action',
			create : 'user/create.action',
			update : 'user/update.action',
			destroy : 'user/delete.action'
		},
		reader : {
			type : 'json',
			root : 'data',
			idProperty : 'id',
			totalProperty : 'total',
			successProperty : 'success',
			messageProperty : 'errorMsg'
		}
	}
});