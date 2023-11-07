import { createSlice } from '@reduxjs/toolkit';

// type SliceState = {
//   list: Array<ClusterObject>;
//   activeCluster: ClusterObject | undefined;
// };

// export type ClusterObject = {
//   id: number;
//   name: string;
// };

// const initialState: SliceState = {
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
    // setActiveCluster: (state, action: PayloadAction<number>) => {
    setActiveCluster: (state, action) => {
      state.activeCluster = state.list[action.payload];
    },
  },
});

export default clusterSlice.reducer;
export const { addCluster, removeCluster, setActiveCluster } =
  clusterSlice.actions;
