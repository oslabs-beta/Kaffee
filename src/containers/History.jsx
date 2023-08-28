import React from 'react';
import HistoryItem from '../components/HistoryItem.jsx'
import dummyData from './dummyData.json';

const History = () => {
  return (
    <div className='history'>
      <HistoryItem data={dummyData.data}/>
    </div>
  )
};

export default History;


