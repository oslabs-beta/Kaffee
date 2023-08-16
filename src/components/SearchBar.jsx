import React, { useState, MouseEvent } from 'react';
// import { useAppDispatch, useAppSelector } from '../hooks/hooks.js';
import { useSelector, useDispatch } from 'react-redux';
import { filterCharts } from '../reducers/chartSlice.js';

export default function () {
  const [search, setSearch] = useState('');

  // const dispatch = useAppDispatch();
  const dispatch = useDispatch();

  // const handleChangedText = (e: MouseEvent) => {
  const handleChangedText = (e) => {
    return setSearch(e.target.value);
  };

  const handleBrew = () => {
    alert(search);
    dispatch(filterCharts(search));
  };

  return (
    <footer>
      <form method='POST'>
        <input
          type='text'
          onchange={handleChangedText}
          placeholder='Input box: number of messages, size of messages, frequency'
        />
        <button onClick={handleBrew}>Brew</button>
      </form>
    </footer>
  );
}
