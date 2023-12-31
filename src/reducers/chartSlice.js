import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

export const newChart = createAsyncThunk(
  'chart/newChart',
  async (_, thunkAPI) => {
    const count = 50;
    const apiUrl = `http://localhost:8080/api/dummy/${count}`;

    const res = await fetch(apiUrl);
    const data = await res.json();

    return data;
  }
);

const initialState = {
  list: [],
  status: 'idle',
  metricCount: 10,
};

const chartSlice = createSlice({
  name: 'chart',
  initialState,
  reducers: {
    addChart: (state, action) => {
      state.list.push({ metric: action.payload });
    },
    removeChart: (state, action) => {
      const newChartList = state.list.filter((chart) => {
        return chart.metric !== action.payload;
      });
      state.list = newChartList;
    },
    filterCharts: (state, action) => {
      // How do we do this without blowing up all the charts from before?
      // ANSWER: We don't do this in this state, we do it in the page!
    },
    changeMetricCount: (state, action) => {
      // set maximum and minimum counts here as well as on the front end
      // this serves as some way of ensuring that data is displayed reasonably
      let MIN_COUNT = 10;
      let MAX_COUNT = 600;

      let desiredCount = action.payload;
      if (desiredCount < MIN_COUNT) {
        desiredCount = MIN_COUNT;
      } else if (desiredCount > MAX_COUNT) {
        desiredCount = MAX_COUNT;
      }

      state.metricCount = desiredCount;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(newChart.pending, (state, action) => {
        state.status = 'loading';
      })
      .addCase(newChart.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.list.push(action.payload);
      })
      .addCase(newChart.rejected, (state, action) => {
        state.status = 'failed';
      })
  }
});

export default chartSlice.reducer;
export const { addChart, removeChart, filterCharts, changeMetricCount } =
  chartSlice.actions;
