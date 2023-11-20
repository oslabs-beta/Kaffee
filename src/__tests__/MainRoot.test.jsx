import { test, expect, vi, beforeAll } from 'vitest';
import { render, screen } from '@testing-library/react';
import { setupServer } from 'msw/node';
import { HttpResponse, graphql, http } from 'msw';

import MainRoot from '../MainRoot';
import { metricListFriendly } from '../utils/metrics';
import { loader } from '../containers/NavBar';
import { Client } from '@stomp/stompjs';

// Test gives 8 errors on websockets and Java server if not connected to Java server/Apache

const handlers = [
  http.get('http://localhost:8080/available-server-metrics', () => {
    return HttpResponse.json([{ 'bytes-in': 'Server Bytes In' }], {
      status: 200,
    });
  }),
  http.get('http://localhost:8080/getSettings', () => {
    return HttpResponse.json([{ settings: null }], {
      status: 200,
    });
  }),
];

const server = setupServer(...handlers);

beforeAll(() => {
  server.listen({ onUnhandledRequest: 'error' }); // this removes 4 errors caused by api calls

  // all of this doesn't work
  // vi.mock('@stomp/stompjs', async (importOriginal) => {
  //   const mod = await importOriginal();
  //   return {
  //     ...mod,
  //     // replace some exports
  //     Client: { activate: vi.fn(() => {}) },
  //   };
  // });
  // const loaderMock = vi.fn((loader) => [{ 'bytes-in': 'Server Bytes In' }]);
  // const socketConnectMock = vi.fn((socketConnect) => [
  //   { 'bytes-in': 'Server Bytes In' },
  // ]);
});

test('"MainRoot" renders', async () => {
  const main = render(<MainRoot />);
  expect(screen.getByText('About')).toBeDefined();
  expect(screen.getByText('Home')).toBeDefined();
  main.unmount();
});
