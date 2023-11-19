import { test, expect } from 'vitest';
import { render } from '@testing-library/react';
import { Provider } from 'react-redux';
import { StaticRouter } from 'react-router-dom/server';
import userEvent from '@testing-library/user-event';

import NavBar from '../containers/NavBar';
import store from '../redux/store';

test('contains all buttons', () => {
  const buttonsToBeInNavBar = [
    'Home',
    'History',
    'Settings',
    'Choose Metrics',
    'About',
    'Start Test Producers',
  ];

  const navBar = render(
    <StaticRouter>
      <Provider store={store}>
        <NavBar />
      </Provider>
    </StaticRouter>,
  );

  const div = navBar.getAllByRole('button');
  const buttonsFromNavBar = div.map((button) => button.textContent);
  buttonsToBeInNavBar.forEach((button) => {
    expect(buttonsFromNavBar.includes(button)).toBeTruthy();
  });

  navBar.unmount();
});

test('Start Test Producers button changes from start to stop to start', async () => {
  const user = userEvent.setup();

  const navBar = render(
    <StaticRouter>
      <Provider store={store}>
        <NavBar />
      </Provider>
    </StaticRouter>,
  );

  let startStopProducers = navBar.getByTestId('startStopProducers');
  expect(startStopProducers.textContent).toContain('Start Test Producers');

  await user.click(startStopProducers);
  startStopProducers = navBar.getByTestId('startStopProducers');
  expect(startStopProducers.textContent).toContain('Stop Test Producers');
  expect(startStopProducers.textContent).not.toContain('Start Test Producers');

  await user.click(startStopProducers);
  startStopProducers = navBar.getByTestId('startStopProducers');
  expect(startStopProducers.textContent).toContain('Start Test Producers');
  expect(startStopProducers.textContent).not.toContain('Stop Test Producers');

  navBar.unmount();
});
