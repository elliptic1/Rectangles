package com.tbse.rectangles;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.physics.box2d.Contact;

public class ContactsManager {
	private ArrayList<Contact> contacts = new ArrayList<Contact>(10);
	
	public ContactsManager() {
		super();
	}
	
	public boolean hasContacts() {
		if (contacts.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public void addContact(Contact c) {
		contacts.add(c);
	}
	
	public void removeContact(Contact c) {
		contacts.remove(c);
		
	}
	
	public void removeAllContacts() {
		Iterator<Contact> iterator = contacts.iterator();
		while (iterator.hasNext()) {
			removeContact(iterator.next());
		}
		
	}
	
	public int numContacts() {
		return contacts.size();
	}
	
	public ArrayList<Contact> getContacts() {
		return contacts;
	}

}
