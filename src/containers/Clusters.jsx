import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import {
  addCluster,
  removeCluster,
  setActiveCluster,
} from '../reducers/clusterSlice.ts';

// interface props = {}

export default function Clusters() {
  const clusters = useSelector((state) => state.clusters);

  const handleAddCluster = () => {
    useDispatch(addCluster());
  };

  const handleRemoveCluster = () => {
    useDispatch(removeCluster());
  };

  // const handleSetActive = (id: number) => {
  const handleSetActive = (id) => {
    useDispatch(setActiveCluster(id));
  };

  return (
    <nav id='clusters'>
      <header>Clusters</header>
      {clusters.map((cluster) => {
        return (
          <button onClick={handleSetActive(cluster.id)}>
            Cluster {cluster.id}
          </button>
        );
      })}
      <footer>
        <button onClick={handleAddCluster}>Add</button>
        <button onClick={handleRemoveCluster}>Remove</button>
      </footer>
    </nav>
  );
}
