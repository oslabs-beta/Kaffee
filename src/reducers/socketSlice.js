import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

export const newChart = createAsyncThunk(
  'chart/newChart',
  async (_, thunkAPI) => {
    const state = thunkAPI.getState();
    const count = 50;
    const apiUrl = `http://localhost:8080/api/dummy/${count}`;

    const res = await fetch(apiUrl);
    const data = await res.json();

    return data;
  }
);

const initialState = {
  list: [],
  client: null,
  status: 'disconnected',
};

const socketSlice = createSlice({
  name: 'sockets',
  initialState,
  reducers: {
    connected: (state) => {
      state.status = 'connected';
    },

    disconnected: (state, action) => {
      state.status = 'disconnected';
    },
    setClient: (state, action) => {
      state.client = action.payload;
    },
    filterCharts: (state, action) => {

    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(newChart.pending, (state, action) => {

      })
      .addCase(newChart.fulfilled, (state, action) => {

      })
      .addCase(newChart.rejected, (state, action) => {

      })
  }
});

export default socketSlice.reducer;
export const { connected, disconnected, setClient } = socketSlice.actions;
