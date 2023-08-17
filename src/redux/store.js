import { configureStore } from '@reduxjs/toolkit';
import clusterSlice from '../reducers/clusterSlice.js';
import chartSlice from '../reducers/chartSlice.js';

export const store = configureStore({
  reducer: {
    clusters: clusterSlice,
    charts: chartSlice,
  },
});

// export type RootState = ReturnType<typeof store.getState>;
// export type AppDispatch = typeof store.dispatch;

export default store;
