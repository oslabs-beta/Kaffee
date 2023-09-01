import { configureStore } from '@reduxjs/toolkit';
import clusterSlice from '../reducers/clusterSlice.js';
import chartSlice from '../reducers/chartSlice.js';
import socketSlice from '../reducers/socketSlice.js';
export const store = configureStore({
    reducer: {
        clusters: clusterSlice,
        charts: chartSlice,
        sockets: socketSlice,
    },
});
export default store;
//# sourceMappingURL=store.js.map