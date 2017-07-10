package com.tyh.chatroom;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Receiver implements Runnable
{  
    private final static String EXCHANGE_NAME = "chatroom";  
  
    

	public void run() {
		ConnectionFactory factory = new ConnectionFactory();  
        factory.setHost("localhost");  
        Connection connection;
		try {
			connection = factory.newConnection();
			Channel channel = connection.createChannel();  
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");  
	        // 创建一个非持久的、唯一的且自动删除的队列  
	        String queueName = channel.queueDeclare().getQueue();  
	        // 为转发器指定队列，设置binding  
	        channel.queueBind(queueName, EXCHANGE_NAME, "");  
	  
	  
	        QueueingConsumer consumer = new QueueingConsumer(channel);  
	        // 指定接收者，第二个参数为自动应答，无需手动应答  
	        channel.basicConsume(queueName, true, consumer);  
	  
	        while (true)  
	        {  
	            QueueingConsumer.Delivery delivery = consumer.nextDelivery();  
	            String message = new String(delivery.getBody());  
	            System.out.println(message);  
	        }  
	  
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ShutdownSignalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
	}  
}
