import React, { useState, useEffect } from 'react';
import History from '../containers/History.jsx';


// TODO:
// import history folder
// parse names of json files by date
// create chartjs from data on expand






export default function ({data}) {

  const items = [];
  data.forEach(el => {
    const [expanded, setExpanded] = useState(false);
  
    const clickHandler = () => {
      setExpanded(!expanded);
    };
  
    items.push(
      <div className='history-item-container' key={el.id}>
        <div className="history-preview">
        <div className='history-date'>
          {el.Date}
        </div>
        
        <button className='expand-button' onClick={clickHandler}>
          {expanded ? 'Collapse' : 'Expand'}
        </button>

        </div>
       
        

        {expanded && <div className='expanded-metrics'>
          {JSON.stringify(el.Metrics)}

          </div>
          }
      </div>
    );
  });



  return (
    items
  )
}