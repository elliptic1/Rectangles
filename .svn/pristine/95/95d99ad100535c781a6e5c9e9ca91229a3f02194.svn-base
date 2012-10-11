package com.tbse.rectangles;

import com.badlogic.gdx.physics.box2d.Contact;

public class Wall {

	private Object userData;
	private ContactsManager contactsManager = new ContactsManager();

	public Wall() {
		// TODO Auto-generated constructor stub
		this.setUserData(new Object());
	}

	public Object getUserData() {
		return userData;
	}

	public void setUserData(Object userData) {
		this.userData = userData;
	}

	public ContactsManager getContactsManager() {
		return this.contactsManager;
	}

	public void addContact(Contact c) {
		this.contactsManager.addContact(c);
	}

	public void removeContact(Contact c) {
		this.contactsManager.removeContact(c);
	}

}
