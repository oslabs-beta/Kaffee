import { createSlice, PayloadAction, createAsyncThunk } from '@reduxjs/toolkit';

export const newChart = createAsyncThunk(
  'chart/newChart',
  async (_, thunkAPI) => {
    const state = thunkAPI.getState();
    const count = 50;
    const apiUrl = `http://localhost:8080/dummy/${count}`;

    const res = await fetch(apiUrl);
    const data = await res.json();

    return data;
  }
);

// type chartState = {
//   list: Array<chartObj>;
// };

// export type chartObj = {
// };

// const initialState: chartState = {
const initialState = {
  list: [],
  status: 'idle',
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
  extraReducers: {
    [newChart.pending]: (state, action) => {
      state.status = 'loading';
    },
    [newChart.fulfilled]: (state, action) => {
      state.status = 'succeeded';
      // if we are getting chunks of data repeatedly, we will need to handle this differently
      state.list.push(action.payload);
    },
    [newChart.rejected]: (state, action) => {
      state.status = 'failed';
    },
  },
});

export default chartSlice.reducer;
export const { addChart, removeChart, filterCharts } = chartSlice.actions;
