import { useDispatch, useSelector } from 'react-redux';
import {
  addCluster,
  removeCluster,
  setActiveCluster,
} from '../reducers/clusterSlice.js';

export default function Clusters() {
  const clusterList = useSelector((state) => state.clusters.list);

  const dispatch = useDispatch();

  const handleAddCluster = () => {
    dispatch(addCluster());
  };

  const handleRemoveCluster = () => {
    dispatch(removeCluster());
  };

  const handleSetActive = (clusterId) => {
    dispatch(setActiveCluster(clusterId));
  };

  return (
    <nav id="clusters">
      <header>Clusters</header>
      <ul>
        {clusterList.length ? (
          clusterList.map((cluster) => {
            return (
              <li>
                <button onClick={() => handleSetActive(cluster.id)}>
                  Cluster {cluster.name}
                </button>
              </li>
            );
          })
        ) : (
          <></>
        )}
      </ul>
      <footer>
        <button onClick={handleAddCluster}>Add</button>
        <button onClick={handleRemoveCluster}>Remove</button>
      </footer>
    </nav>
  );
}
