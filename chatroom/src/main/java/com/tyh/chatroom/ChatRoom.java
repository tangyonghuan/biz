package com.tyh.chatroom;

public class ChatRoom {
	public static void main(String[] args) {
		Sender sender = new Sender();
		Receiver receiver = new Receiver();
		Thread send = new Thread(sender);
		Thread receive = new Thread(receiver);
		receive.start();
		send.start();
	}
}
