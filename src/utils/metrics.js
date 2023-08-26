// Generalize this here, in case we want to use it elsewhere.
// This may be better moved to a common place where we can use it
// across the whole application
export function metricListFriendly(metricList) {
  const returnList = [];

  // here we loop through the metrics list that we get from the java server
  // if we have a friendly name for it, we add it to the list
  // this way we don't add anything to the list that isn't ready to be displayed
  // in a user friendly manner
  for (const metric of metricList) {
    if (Object.hasOwn(friendlyList, metric)) {
      returnList.push({
        name: metric,
        display: friendlyList[metric],
      });
    }
  }

  return returnList;
}

export const friendlyList = {
  'bytes-in': 'Server Bytes In',
  'isr-shrinks': 'Rate of In Sync Replica Shrinking',
  'offline-partitions-count': 'Number of Offline Partitions',
  'isr-expands': 'Rate of In Sync Replica Expanding',
  'leader-election-rate': 'Leader Election Rate',
  'bytes-out': 'Server Bytes Out',
  'active-controller-count': 'Number of Active Controllers',
  'under-replicated-partitions': 'Number of Under Replicated Partitions',
  'unclean-leader-selection': 'Number of Unclean Leader Elections per Second',
};

// the idea here is to create an object that pairs a preferred metric to track
// based upon the selected metric
export const preferredMetrics = {
  'bytes-in': 'OneMinuteRate',
  'isr-shrinks': 'Rate of In Sync Replica Shrinking',
  'offline-partitions-count': 'Number of Offline Partitions',
  'isr-expands': 'Rate of In Sync Replica Expanding',
  'leader-election-rate': 'Leader Election Rate',
  'bytes-out': 'Server Bytes Out',
  'active-controller-count': 'Number of Active Controllers',
  'under-replicated-partitions': 'Number of Under Replicated Partitions',
  'unclean-leader-selection': 'Number of Unclean Leader Elections per Second',
};

// These are RGB color strings, but without the wrapper
// We do this so we can use them in rgba() functions and adjust
// the alpha channel as wanted
export const metricColors = [
  '255, 0, 0',
  '0, 255, 0',
  '0, 0, 255',
  '255, 255, 0',
  '255, 0, 255',
  '0, 255, 255',
];
