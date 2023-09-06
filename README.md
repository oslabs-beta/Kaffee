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

#### Exposing Kafka's JMX Endpoints

## Associated Links

- [The Official Kaffee Landing Page](http://firebase.here/)
- [Our Original Medium Article](http://medium.article.here/)
- [Apache Kafka](https://kafka.apache.org/)

## Contributor Information

<div>
  <div style="display: inline-block;">
    <h3>Duke Ahn</h3>
    <img src="profile.png" alt="Duke Ahn">
    <p>
    <a href="http://linkedin.com/"><img src="linkedinlogo.png" alt="Duke Ahn's LinkedIn" /></a>
    <a href="http://github.com/"><img src="githublogo.png" alt="Duke Ahn's GitHub" /></a>
    </p>
  </div>
  <div style="display: inline-block;">
    <h3>Vitaly Blotski</h3>
    <img src="profile.png" alt="Vitaly Blotski">
    <p>
    <a href="http://linkedin.com/"><img src="linkedinlogo.png" alt="Vitaly Blotski's LinkedIn" /></a>
    <a href="http://github.com/"><img src="githublogo.png" alt="Vitaly Blotski's GitHub" /></a>
    </p>
  </div>
</div>
<div>
  <div style="display: inline-block;">
    <h3>Clay Hilgert</h3>
    <img src="profile.png" alt="Clay Hilgert">
    <p>
    <a href="http://linkedin.com/"><img src="linkedinlogo.png" alt="Clay Hilgert's LinkedIn" /></a>
    <a href="http://github.com/"><img src="githublogo.png" alt="Clay Hilgert's GitHub" /></a>
    </p>
  </div>
  <div style="display: inline-block;">
    <h3>Darren Pavel</h3>
    <img src="profile.png" alt="Darren Pavel">
    <p>
    <a href="http://linkedin.com/"><img src="linkedinlogo.png" alt="Darren Pavel's LinkedIn" /></a>
    <a href="http://github.com/"><img src="githublogo.png" alt="Darren Pavel's GitHub" /></a>
    </p>
  </div>
</div>
---

### License Information

This project is licensed through MIT License. Please see the included LICENSE.md for more information.
