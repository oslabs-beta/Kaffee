import { useEffect } from 'react';
import { useSelector } from 'react-redux';
import Chart from '../components/Chart.jsx';
import client from '../utils/socket.js';

export default function () {
  const charts = useSelector((state) => state.charts.list);

  useEffect(() => {
    try {
      client.activate();
    } catch (error) {
      throw error;
    }

    return () => {
      client.deactivate();
    };
  }, []);

  return (
    <>
      <div id="metrics">
        <div id="charts">
          {charts?.map((chart, i) => {
            return (
              <Chart
                key={chart.metric}
                metric={chart.metric}
                id={chart.metric}
              />
            );
          })}
        </div>
      </div>
    </>
  );
}
