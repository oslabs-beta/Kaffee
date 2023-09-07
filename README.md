![Kaffee Logo](/src/assets/readme/logo256.png)

# Kaffee

## Kafka Metrics and Diagnostics Tool

Kaffee provides real-time metric data read directly from Kafka's exposed JMX endpoints.
Using the displayed data, users will be able to diagnose problems within a Kafka broker.
As charts are displayed, data is saved within log files for historical analysis.

## Setup / Installation

You can use our Docker image to run Kaffee. Ensure you have a Docker service running on your system, and then run "docker-compose up" within the root directory to start Kaffee on your system.

## How To Use

After booting up Kaffee for the first time you will be presented with the following screen:

![Kafka Main Page](/src/assets/readme/Main.png)

Please click on the **Settings** button at the top and ensure that the your Kafka URL, Port, and JMX Ports are all set correcly. You can use the **Seconds of Data to Display** slider to change the number of seconds that will displayed on each chart in future sets.

![Settings Button Highlighted](/src/assets/readme/Settings.png)

Click the **Main** button at the top to transition to the metrics display area. In future launches you will begin here with the settings saved. Once you are in the Main area, you will see the **Choose Metrics** button.

Clicking on **Choose Metrics** will display a list of available metrics to display. Simply check one of the check boxes to begin tracking that metric.

![Data Displayed Example Opening/Closing](/src/assets/readme/add%20a%20chart.gif)

Once you have started tracking a metric, every one (1) second the data will be stored in a log file and can be accessed within the **History** area.

![History Page](/src/assets/readme/History.png)

If you would like to stress your Kafka broker a bit, you may click on the **Start Producers** button. This will begin the number of Kafka producers and consumers specified within **Settings** and have them generate dummy data. In order to stop producing this dummy data, click on the button it the same location, now with the text **Stop Producers**

![Start/Stop Producers and Consumers](</src/assets/readme/stop%20and%20start%20(1).gif>)

## How to contribute

#### Please reach out to one of our contributors for ways in which you can contribute to this project.

In order to develop Kaffee further please follow these steps:

1. Fork the repository.
2. Clone your fork into your development environment.
3. Run "npm install" in the root of the forked directory.
4. Ensure that you have installed Kafka Maven (see below for further instructions).
5. Start your Kafka broker, making sure to expose your JMX endpoints (see below for further instructions).
6. Once you have verified that Kafka is up and running, run "npm run dev" from the root directory.
7. You should now be running our Java Spring-Boot server with hot reloading, the client with hot reloading, and the Node.js server with hot reloading.

#### Installing Kafka Maven

You can find instructions for installing Maven from Apache [here](https://maven.apache.org/install.html).

#### Exposing Kafka's JMX Endpoints

Confluent has documentation about setting up JMX through their Docker image that can be read [here](https://docs.confluent.io/platform/current/installation/docker/operations/monitoring.html#use-jmx-monitor-docker-deployments).

Red Hat has documentation about setting up JMX that relates to the Kafka binaries distributed by Apache. These instructions can be read [here](https://access.redhat.com/documentation/en-us/red_hat_amq/7.2/html/using_amq_streams_on_red_hat_enterprise_linux_rhel/monitoring-str).

## Associated Links

- [The Official Kaffee Landing Page](http://kafka-kafka.com/)
- [Our Original Medium Article](https://medium.com/@darren.pavel/kaffee-an-apache-kafka-monitor-fe4fa4e997d1/)
- [Apache Kafka](https://kafka.apache.org/)

## Contributor Information

<!-- Not sure why this mess works and so many other attempts didn't -->

|                                                                                                                                                 Duke Ahn                                                                                                                                                 |                                                                                                                                                  Vitaly Blotski                                                                                                                                                  |
| :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: |
| [![Duke Ahn's LinkedIn](src/assets/readme/duke-ahn.png)](https://www.linkedin.com/in/duke-ahn-3886b9284/)<br/>[![Duke Ahn's Github](src/assets/readme/github.png)](https://github.com/AhnDuke) [![Duke Ahn's LinkedIn](src/assets/readme/linkedin.png)](https://www.linkedin.com/in/duke-ahn-3886b9284/) | [![Vitaly Blotski's LinkedIn](src/assets/readme/Blotski.png)](https://www.linkedin.com/in/vitaly-blotski/)<br/>[![Vitaly Blotski's Github](src/assets/readme/github.png)](https://github.com/Blotski)[![Vitaly Blotski's LinkedIn](src/assets/readme/linkedin.png)](https://www.linkedin.com/in/vitaly-blotski/) |

|                                                                                                                                           Clay Hilgert                                                                                                                                           |                                                                                                                                                           Darren Pavel                                                                                                                                                           |
| :----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: |
| [![Clay Hilgert](src/assets/readme/clhilgert.png)](https://www.linkedin.com/in/clay-hilgert/)<br/>[![Clay Hilgert's Github](src/assets/readme/github.png)](https://github.com/clhilgert) [![Clay Hilgert's LinkedIn](src/assets/readme/linkedin.png)](https://www.linkedin.com/in/clay-hilgert/) | [![Darren Pavel's LinkedIn](src/assets/readme/dpavel.png)](<[http://](https://www.linkedin.com/in/darren-pavel/)>)<br/>[![Darren Pavel's Github](src/assets/readme/github.png)](https://github.com/dcpavel) [![Darren Pavel's LinkedIn](src/assets/readme/linkedin.png)](<[http://](https://www.linkedin.com/in/darren-pavel/)>) |

---

### License Information

This project is licensed through MIT License. Please see the included LICENSE.md for more information.
