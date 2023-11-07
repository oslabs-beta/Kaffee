import { configureStore} from '@reduxjs/toolkit';
import clusterSlice from '../reducers/clusterSlice.js';
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



export default store;
