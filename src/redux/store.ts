import { configureStore, ThunkAction, Action } from '@reduxjs/toolkit';
import clusterSlice from '../reducers/clusterSlice';
import chartSlice from '../reducers/chartSlice.js';
import socketSlice from '../reducers/socketSlice.js';
import settingsSlice from '../reducers/settingSlice.js';

export const store = configureStore({
  reducer: {
    clusters: clusterSlice,
    charts: chartSlice,
    sockets: socketSlice,
    settings: settingsSlice,
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
