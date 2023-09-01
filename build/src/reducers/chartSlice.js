import { createSlice, PayloadAction, createAsyncThunk } from '@reduxjs/toolkit';
// used when importing a test chart
export const newChart = createAsyncThunk('chart/newChart', async (_, thunkAPI) => {
    const state = thunkAPI.getState();
    const count = 50;
    const apiUrl = `http://localhost:8080/api/dummy/${count}`;
    const res = await fetch(apiUrl);
    const data = await res.json();
    console.log(data);
    return data;
});
// type chartState = {
//   list: Array<chartObj>;
// };
// export type chartObj = {
//  metric: string,
// };
// const initialState: chartState = {
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
        // removeChart: (state, action: PayloadAction<number>) => {
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
            let MAX_COUNT = 500;
            let desiredCount = action.payload;
            if (desiredCount < MIN_COUNT) {
                desiredCount = MIN_COUNT;
            }
            else if (desiredCount > MAX_COUNT) {
                desiredCount = MAX_COUNT;
            }
            state.metricCount = desiredCount;
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
export const { addChart, removeChart, filterCharts, changeMetricCount } = chartSlice.actions;
//# sourceMappingURL=chartSlice.js.map