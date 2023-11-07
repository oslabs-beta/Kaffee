package com.kaffee.server.TestController;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public class TestConsumer extends Thread {
  //        Properties producerProps = KafkaProducerConfig.getProducerProperties();
  @Override
  public void run(){
        try{
            Properties consumerProps = KafkaConsumerConfig.getConsumerProp();
            Consumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
            String topic = "test";
            consumer.subscribe(Arrays.asList(topic));
            while (RunTest.run) {
                ConsumerRecords<String, String> records = consumer.poll(java.time.Duration.ofMillis(100));
                System.out.println(records);
                records = null;
                try {
                    Thread.sleep(250);
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
            consumer.close();
        } catch (Exception e) {
        System.out.println(e);
        }
  }
}
