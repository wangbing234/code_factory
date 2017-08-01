package com.codefactory;

public class TestOther {
	 public static void getCurrentUser() {

	        String currentUser = System.getProperty("user.name");
	        System.out.println("Current user is " + currentUser);
	    }
	 public static void main(String[] args) {
		 getCurrentUser();
	}
}
