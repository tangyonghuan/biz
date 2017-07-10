package com.tyh.chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Sender implements Runnable {

	private final static String EXCHANGE_NAME = "chatroom";

	public void run() {
		//用户名
    	String nickname = null;
    	String sendMessage = "";
        // 创建连接和频道  
        ConnectionFactory factory = new ConnectionFactory();  
        factory.setHost("localhost");  
        Connection connection;
		try {
			connection = factory.newConnection();
			Channel channel = connection.createChannel();  
		    // 声明转发器和类型  
		    channel.exchangeDeclare(EXCHANGE_NAME, "fanout" );  
		    // 创建一个非持久的、唯一的且自动删除的队列   
		    String queueName = channel.queueDeclare().getQueue();        
		    // 为转发器指定队列，设置binding         
		    channel.queueBind(queueName, EXCHANGE_NAME, "");  	        
		    QueueingConsumer consumer = new QueueingConsumer(channel);        
		    //指定接收者        
		    channel.basicConsume(queueName, true, consumer);	        
		    System.out.println("Welcome to RabbitMQ ChatRoom!");	        
		    System.out.println("Type q to exit...");		       
		    System.out.println("Input your nickname first");		        
		    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));     
		    nickname = br.readLine();
		    
		    System.out.println("Hello "+nickname+",you can chat from now,enjoy it");
			sendMessage = br.readLine();
	        while(!sendMessage.equals("q")){
	        	// 往转发器上发送消息  
	           
	           	sendMessage = nickname+" said "+sendMessage;
	           	channel.basicPublish(EXCHANGE_NAME, "", null, sendMessage.getBytes()); 
	        	sendMessage = br.readLine();
	        }
	        channel.close();
	        connection.close();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
       
	}
}
