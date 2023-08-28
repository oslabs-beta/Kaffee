import React, { useState, useEffect } from 'react';
import dummyData from './dummyData.json';


export default function () {

  const items = [];
  dummyData["data"].forEach(el => {
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