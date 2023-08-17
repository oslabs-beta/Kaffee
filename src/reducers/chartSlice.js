import { createSlice, PayloadAction } from '@reduxjs/toolkit';

// type chartState = {
//   list: Array<chartObj>;
// };

// export type chartObj = {
// };

// const initialState: chartState = {
const initialState = {
  list: [],
};

const chartSlice = createSlice({
  name: 'chart',
  initialState,
  reducers: {
    addChart: (state) => {
      state.list.push();
    },
    // removeChart: (state, action: PayloadAction<number>) => {
    removeChart: (state, action) => {
      const index = action.payload;
      state.list = state.list.splice(index, 1);
    },
    filterCharts: (state, action) => {
      // how do we do this without blowing up all the charts from before?
    },
  },
});

export default chartSlice.reducer;
export const { addChart, removeChart, filterCharts } = chartSlice.actions;
