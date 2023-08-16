import { createSlice, PayloadAction } from '@reduxjs/toolkit';

type SliceState = {
  clusters: [];
  activeCluster: number | undefined;
};

const initialState: SliceState = {
  clusters: [],
  activeCluster: undefined,
};

const clusterSlice = createSlice({
  name: 'cluster',
  initialState,
  reducers: {
    addCluster: (state, action: PayloadAction<Object>) => {},
    removeCluster: (state, action: PayloadAction<Object>) => {
      if (state.clusters.length) {
        state.clusters.pop();
      }
    },
    setActiveCluster: (state, action: PayloadAction<number>) => {
      state.activeCluster = state.clusters[action.payload];
    },
  },
});

export default clusterSlice;
export const { addCluster, removeCluster, setActiveCluster } =
  clusterSlice.actions;
