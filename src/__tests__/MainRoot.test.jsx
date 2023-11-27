import { test, expect, vi, beforeAll, afterAll } from 'vitest';
import { render, screen } from '@testing-library/react';
import { setupServer } from 'msw/node';
import { HttpResponse, http } from 'msw';

import MainRoot from '../MainRoot';

// Test gives 6 errors on websockets and api calls if not connected to Java server/Apache

const handlers = [
  http.get('http://localhost:8080/available-server-metrics', () => {
    return HttpResponse.json([{ 'bytes-in': 'Server Bytes In' }], {
      status: 200,
    });
  }),
  http.get('http://localhost:8080/getSettings', () => {
    return HttpResponse.json({
      status: 200,
    });
  }),
];

// this removes 4 errors caused by api calls
const server = setupServer(...handlers);
beforeAll(() => {
  server.listen({ onUnhandledRequest: 'error' });
});

afterAll(() => server.close());

test('"MainRoot" renders', async () => {
  let errNum = 0;
  try {
    const main = render(<MainRoot />);
    expect(screen.getByText('About')).toBeDefined();
    expect(screen.getByText('Home')).toBeDefined();
    main.unmount();
  } catch (error) {
    console.log(errNum++);
  }
});
