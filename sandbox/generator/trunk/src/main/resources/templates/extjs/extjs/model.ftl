Ext.define('AM.model.User', {
	extend: 'Ext.data.Model',
	idProperty:'id',
	fields: [
      {name: 'id', type: 'int', useNull:true},
      {name: 'firstName', type: 'string'},
      {name: 'lastName', type: 'string'},
      {name: 'mail', type: 'string'}
    ]
});