import { test, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import { setupServer } from 'msw/node';
import { HttpResponse, graphql, http } from 'msw';

import MainRoot from '../MainRoot';
import { metricListFriendly } from '../utils/metrics';
import { loader } from '../containers/NavBar';

// Test gives 8 errors on websockets and Java server if not connected to Java server/Apache
// const loaderMock = vi.fn((loader) => [{ 'bytes-in': 'Server Bytes In' }]);
// console.log(vi.isMockFunction(loaderMock));
// loader();
// console.log(loaderMock.mock.lastCall);

// loader = vi.fn().mockImplementation(() => [{ 'bytes-in': 'Server Bytes In' }]);

// Define handlers that catch the corresponding requests and returns the mock data.
const handlers = [
  http.get('http://localhost:8080/available-server-metrics', () => {
    return HttpResponse.json([{ 'bytes-in': 'Server Bytes In' }], {
      status: 200,
    });
  }),
];

test('"MainRoot" renders', async () => {
  const main = render(<MainRoot />);

  expect(screen.getByText('About')).toBeDefined();
  expect(screen.getByText('Home')).toBeDefined();

  main.unmount();
});
