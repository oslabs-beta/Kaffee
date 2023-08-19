import { configureStore, ThunkAction, Action } from '@reduxjs/toolkit';
import clusterSlice from '../reducers/clusterSlice.js';
import chartSlice from '../reducers/chartSlice.js';

export const store = configureStore({
  reducer: {
    clusters: clusterSlice,
    charts: chartSlice,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  Action<String>
>;

export default store;
