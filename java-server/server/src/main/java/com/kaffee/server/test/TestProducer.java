package com.kaffee.server.test;

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
        Properties producerProps = KafkaProducerConfig.getProducerProperties();
        try (Producer<String, String> producer = new KafkaProducer<>(producerProps)){
            String topic = "test";
            for (int i = 0; i < 10; i++) {
                System.out.println(i + "from" + name);
                String key = "key-" + i;
                String value = "value-" + i;
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
                producer.send(record);
                record = null;
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
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

