Ext.define('AM.controller.Users',{
	extend: 'Ext.app.Controller',
	stores: ['Users'],
	models: ['User'],
	views: [ 'user.List', 'user.Edit'],
	activeRecord: null,	
	init: function() {
		this.control({
			'userlist': {
				itemdblclick: this.editUser,
				itemclick : this.setActiveRecord
			},
			'userlist button[action=add]': {
				click: this.addUser
			},
			'userlist button[action=delete]': {
				click: this.deleteUser
			},
			'useredit button[action=save]': {
				click: this.updateUser
			}
		});
	},

	 setActiveRecord: function(grid, record){	
        this.activeRecord = record;
        var button = Ext.getCmp('delete');
        if (record) {
        	button.enable();
        } else {
        	button.disable();
        }
    },
    addUser: function(button) {    	    	
		console.log('Clicked the Add button');		
		var user = Ext.create('AM.model.User');		
		
		var view = Ext.widget('useredit');
		view.down('form').loadRecord(user);
	},
	updateUser: function(button) {
		console.log('Clicked the Save button');
		var win = button.up('window'),
			form = win.down('form'),
			record = form.getRecord(),
			values = form.getValues();

		
		record.set(values);
		win.close();

		if(record.get('id') == null) {
			this.getUsersStore().add(record);
		}

		this.getUsersStore().sync({
			success:  function(batch, options) {
				console.log("success");
				console.log(batch.operations[0]);
				console.log(batch.operations[0].request.action);
				console.log(batch.operations[0].getError());
	        },
			 failure:  function(batch, options) {
				console.log("failure");
				console.log(batch.operations[0]);
				console.log(batch.operations[0].request.action);
				console.log(batch.operations[0].getError());
	        }
		});
	},
	deleteUser: function(button) {
		console.log('Clicked the Delete button');		
        if (this.activeRecord) {
        	this.getUsersStore().remove(this.activeRecord);
        	
        	this.getUsersStore().sync({
    			success:  function(batch, options) {
    				console.log("success");
    				console.log(batch.operations[0]);
    				console.log(batch.operations[0].request.action);
    				console.log(batch.operations[0].getError());
    	        },
    			 failure:  function(batch, options) {
    				console.log("failure");
    				console.log(batch.operations[0]);
    				console.log(batch.operations[0].request.action);
    				console.log(batch.operations[0].getError());
    	        }
    		});
        }
	},
	editUser: function(grid, record) {		
		console.log('Double clicked on ' + record.get('firstName')+ ' ' + record.get('lastName'));
		var view = Ext.widget('useredit');
		view.down('form').loadRecord(record);
	}
});