import { render, fireEvent } from '@testing-library/react';
import { it, expect } from 'vitest';
import { Provider } from 'react-redux';
import { StaticRouter } from 'react-router-dom/server';

import Settings from '../containers/Settings';
import store from '../redux/store';

it('tests if parameters and input fields exist and if right data can be entered', () => {
  const settings = render(
    <StaticRouter>
      <Provider store={store}>
        <Settings />
      </Provider>
    </StaticRouter>,
  );
  const label = [
    'Kafka Port',
    'Kafka URL',
    'JMX Port',
    'Chart Interval',
    '# Test Producers',
    '# Test Consumers',
  ];

  for (let lbl of label) {
    let settingsContainer = settings.getByLabelText(lbl);

    if (lbl === 'Chart Interval') {
      fireEvent.change(settingsContainer, { target: { value: '30' } });
      expect(settingsContainer.value).toBe('30');
    } else if (lbl === 'Kafka URL') {
      fireEvent.change(settingsContainer, { target: { value: 'sss' } });
      expect(settingsContainer.value).toBe('sss');
    } else {
      fireEvent.change(settingsContainer, { target: { value: '25' } });
      expect(settingsContainer.value).toBe('25');
    }
  }

  settings.unmount();
});
