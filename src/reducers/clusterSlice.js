import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  list: [],
  activeCluster: undefined,
};

const clusterSlice = createSlice({
  name: 'cluster',
  initialState,
  reducers: {
    addCluster: (state) => {
      state.list.push();
    },
    removeCluster: (state) => {
      if (state.list.length) {
        state.list.pop();
      }
    },
    setActiveCluster: (state, action) => {
      state.activeCluster = state.list[action.payload];
    },
  },
});

export default clusterSlice.reducer;
export const { addCluster, removeCluster, setActiveCluster } =
  clusterSlice.actions;
