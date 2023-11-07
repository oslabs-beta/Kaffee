import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  kafkaPort: 0,
  zookeeperPort: 0,
  jmxPort: 0,
  logFilepath: '',
};

const settingsSlice = createSlice({
  name: 'settings',
  initialState,
  reducers: {
    setKafkaPort: (state, action) => {
      state.kafkaPort = action.payload;
    },
    setZookeeperPort: (state, action) => {
      state.zookeeperPort = action.payload;
    },
    setJmxPort: (state, action) => {
      state.jmxPort = action.payload;
    },
    setLogFilepath: (state, action) => {
      state.logFilepath = action.payload;
    },
  },
});

export default settingsSlice.reducer;
export const { setKafkaPort, setZookeeperPort, setJmxPort, setLogFilepath } =
  settingsSlice.actions;
