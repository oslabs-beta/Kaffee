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

![Image Here](image.link)

Please click on the **Settings** button at the top and ensure that the your Kafka URL, Port, and JMX Ports are all set correcly. You can use the **Seconds of Data to Display** slider to change the number of seconds that will displayed on each chart in future sets.

![Settings Button Highlighted](image.link)

Click the **Main** button at the top to transition to the metrics display area. In future launches you will begin here with the settings saved. Once you are in the Main area, you will see the **Choose Metrics** button.

![Choose Metrics Button Highlighted](image.link)

Clicking on **Choose Metrics** will display a list of available metrics to display. Simply check one of the check boxes to begin tracking that metric.

![Data Displayed Example Opening/Closing](image.link)

Once you have started tracking a metric, every one (1) second the data will be stored in a log file and can be accessed within the **History** area.

![History Page](image.link)

If you would like to stress your Kafka broker a bit, you may click on the **Start Producers** button. This will begin the number of Kafka producers and consumers specified within **Settings** and have them generate dummy data. In order to stop producing this dummy data, click on the button it the same location, now with the text **Stop Producers**

![Start/Stop Producers and Consumers](image.link)

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

- [The Official Kaffee Landing Page](http://firebase.here/)
- [Our Original Medium Article](http://medium.article.here/)
- [Apache Kafka](https://kafka.apache.org/)

## Contributor Information

<table style="border: none">
  <tr>
  <td>
    <h3>Duke Ahn</h3>
    <a href="https://www.linkedin.com/in/duke-ahn-3886b9284/"><img src="src/assets/readme/duke-ahn.png" alt="Duke Ahn"></a>
    <p>
    <a href="https://www.linkedin.com/in/duke-ahn-3886b9284/"><img src="/src/assets/readme/linkedin.png" alt="Duke Ahn's LinkedIn" /></a>
    <a href="http://github.com/AhnDuke"><img src="/src/assets/readme/github.png" alt="Duke Ahn's GitHub" /></a>
    </p>
  </td>
  <td>
    <h3>Vitaly Blotski</h3>
    <a href="https://www.linkedin.com/in/vitaly-blotski/"><img src="src/assets/readme/Blotski.png" alt="Vitaly Blotski"></a>
    <p>
    <a href="https://www.linkedin.com/in/vitaly-blotski/"><img src="/src/assets/readme/linkedin.png" alt="Vitaly Blotski's LinkedIn" /></a>
    <a href="http://github.com/Blotski"><img src="/src/assets/readme/github.png" alt="Vitaly Blotski's GitHub" /></a>
    </p>
  </td>
  </tr>
  <tr>
  <td>
    <h3>Clay Hilgert</h3>
    <a href="https://www.linkedin.com/in/clay-hilgert/"><img src="src/assets/readme/clhilgert.png" alt="Clay Hilgert"></a>
    <p>
    <a href="https://www.linkedin.com/in/clay-hilgert/"><img src="/src/assets/readme/linkedin.png" alt="Clay Hilgert's LinkedIn" /></a>
    <a href="http://github.com/clhilgert"><img src="/src/assets/readme/github.png" alt="Clay Hilgert's GitHub" /></a>
    </p>
  </td>
  <td>
    <h3>Darren Pavel</h3>
    <a href="https://www.linkedin.com/in/darren-pavel/"><img src="src/assets/readme/dpavel.png" alt="Darren Pavel"></a>
    <p>
    <a href="https://www.linkedin.com/in/darren-pavel/"><img src="/src/assets/readme/linkedin.png" alt="Darren Pavel's LinkedIn" /></a>
    <a href="https://github.com/dcpavel"><img src="/src/assets/readme/github.png" alt="Darren Pavel's GitHub" /></a>
    </p>
  </td>
  </tr>
</table>

---

### License Information

This project is licensed through MIT License. Please see the included LICENSE.md for more information.
