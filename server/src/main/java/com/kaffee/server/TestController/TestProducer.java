package com.kaffee.server.TestController;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class TestProducer extends Thread{
    private String name;
    public TestProducer(String name){
        this.name = name;
    }
    
    @Override
    public void run(){
        try{
            Properties producerProps = KafkaProducerConfig.getProducerProperties();
            Producer<String, String> producer = new KafkaProducer<>(producerProps);
            String topic = "test";
            Integer count = 0;
            while (RunTest.run == true) {
                count++;
                String key = "key-" + name;
                String value = "value-" + count;
                // System.out.println(value + " from " + name);
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
                producer.send(record);
                record = null;
                try {
                    Thread.sleep(1000);
                } 
                catch(Exception e) {
                    System.out.println(e);
                }
            }
            producer.flush();
            producer.close();
        } catch (Exception e) {

        }
    }
    // public void testProducer(){
    //     Properties producerProps = KafkaProducerConfig.getProducerProperties();
    //     System.out.println("Entered Third");

    //     try (Producer<String, String> producer = new KafkaProducer<>(producerProps)) {
    //         String topic = "test"; // Change to your desired topic name
    //         for (int i = 0; i < 10; i++) {
    //             String key = "key-" + i;
    //             String value = "value-" + i;
    //             ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
    //             producer.send(record);
    //             Thread.sleep(1000);
    //         }
    //         producer.close();
    //     }
    // }
}

