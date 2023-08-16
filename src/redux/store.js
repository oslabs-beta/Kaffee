import { configureStore } from '@reduxjs/toolkit';
import clusterSlice from '../reducers/clusterSlice.ts';
// const rootReducer = combineReducers({});
// export type RootState = ReturnType<typeof rootReducer>

export const store = configureStore({
  reducer: {
    cluster: clusterSlice.reducer,
  },
});

// export type RootState = ReturnType<typeof store.getState>

export default store;
