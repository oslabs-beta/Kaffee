 <p align="center">
  <img src="https://raw.githubusercontent.com/oslabs-beta/Kaffee/main/src/assets/readme/logoFixed.png" alt="" />
  </p>

<div align="center">

![Version: 1.1.1](https://img.shields.io/badge/version-1.1.1-black)
![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)

![pulls](https://img.shields.io/docker/pulls/teamkaffee/kaffee)


</div>

<div align="center">

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)
![Redux](https://img.shields.io/badge/redux-%23593d88.svg?style=for-the-badge&logo=redux&logoColor=white)
![Chart.js](https://img.shields.io/badge/chart.js-F5788D.svg?style=for-the-badge&logo=chart.js&logoColor=white)
![Vite](https://img.shields.io/badge/vite-%23646CFF.svg?style=for-the-badge&logo=vite&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)


</div>

#

## Kaffee

Kaffee provides real-time metric visualization for Apache Kafka. Using the displayed data, users will be able to monitor and diagnose issues within a Kafka broker. As charts are displayed, data is saved within log files for historical analysis.

## Setup / Installation

The easiest way to get Kaffee up and running is to use the provided docker-compose.yml provided in this repository and run ```docker compose up -d``` in a directory containing the file.

Ensure that a Kafka cluster with an exposed JMX port is up and running prior to starting Kaffee. This can be done by simply setting an environment variable ```JMX_PORT=9999``` in your Kafka instance.

Once the container is up and running, navigate to http://localhost:3030.

## How To Use

After booting up Kaffee for the first time you will be presented with the following screen:


Click on the **Settings** button to ensure that the your Kafka URL, Port, and JMX Ports are all set correctly. Use the **Chart Interval** slider to change the number of seconds displayed on each chart.


Click the **Home** button at the top to transition to the metrics display area and click on **Choose Metrics** will display a list of available metrics to display.


Once you have started tracking a metric, data will be stored in a log file and can be accessed within the **History** area.


To test a Kafka broker, click the **Start Producers** button. This will begin the number of Kafka producers and consumers specified within **Settings** and have them generate dummy data. In order to stop producing this dummy data, click on the button it the same location, now with the text **Stop Producers**


### To contribute, please open an issue or pull request.


#### Exposing Kafka's JMX Endpoints

Confluent has documentation about setting up JMX through their Docker image that can be read [here](https://docs.confluent.io/platform/current/installation/docker/operations/monitoring.html#use-jmx-monitor-docker-deployments).

Red Hat has documentation about setting up JMX that relates to the Kafka binaries distributed by Apache. These instructions can be read [here](https://access.redhat.com/documentation/en-us/red_hat_amq/7.2/html/using_amq_streams_on_red_hat_enterprise_linux_rhel/monitoring-str).

## Associated Links

- [Kaffee Landing Page](http://kafka-kaffee.com/)
- [Medium Article](https://medium.com/@darren.pavel/kaffee-an-apache-kafka-monitor-fe4fa4e997d1/)
## Release Notes

### 1.0.0 - Initial release of Kaffee

<details><summary>1.1.0</summary>
  <ul>
    <li>Converted split Node/Java backend to all Java</li>
    <li>Fixed bug where saved settings wouldn't apply until restart</li>
    <li>Updated styling</li>
  </ul>
</details>

<details><summary>1.1.1</summary>
  <ul>
    <li>General code cleanup and main branch update</li>
  </ul>
</details>

## Contributors

<table>
  <tr>
    <td align="center">
      <img src="assets/readme/clhilgert.png" width="140px;" alt="a photo of Clay Hilgert"/>
      <br />
      <sub><b>Clay Hilgert</b></sub>
      <br />
      <a href="https://www.linkedin.com/in/clay-hilgert/">Linkedin</a> |
      <a href="https://github.com/clhilgert">GitHub</a>
    </td>
     <td align="center">
      <img src="assets/readme/duke-ahn.png" width="140px;" alt="a photo of Duke Ahn"/>
      <br />
      <sub><b>Duke Ahn</b></sub>
      <br />
      <a href="https://www.linkedin.com/in/duke-ahn-3886b9284/">Linkedin</a> |
      <a href="https://github.com/AhnDuke">GitHub</a>
    </td> <td align="center">
      <img src="assets/readme/dpavel.png" width="140px;" alt="a photo of Darren Pavel"/>
      <br />
      <sub><b>Darren Pavel</b></sub>
      <br />
      <a href="https://www.linkedin.com/in/darren-pavel/">Linkedin</a> |
      <a href="https://github.com/dcpavel">GitHub</a>
    </td> <td align="center">
      <img src="assets/readme/Blotski.png" width="140px;" alt="a photo of Vitaly Blotski"/>
      <br />
      <sub><b>Vitaly Blotski</b></sub>
      <br />
      <a href="https://www.linkedin.com/in/vitaly-blotski/">Linkedin</a> |
      <a href="https://github.com/Blotski">GitHub</a>
    </td>     
  </tr>
</table>
