import child_process from 'child_process';
import csv_parser from 'csv-parser';

function metricData (port: Number, metric: String): Object {
  // bin/kafka-run-class.sh org.apache.kafka.tools.JmxTool --jmx-url service:jmx:rmi:///jndi/rmi://localhost:${PORT}/jmxrmi --one-time true --object-name ${METRIC}
  // METRIC in the command line kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec
  const returnObj = {};

  return returnObj;
}