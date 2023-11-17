import { test, expect } from 'vitest';
import { render } from '@testing-library/react';
import { StaticRouter } from 'react-router-dom/server';

import MainRoot from '../MainRoot';

// Test gives 8 errors on websockets and Java server if not connected to Java server/Apache
// test('all components rendered to the screen', () => {
//   // const main = render(
//   //   // <StaticRouter>
//   //   <MainRoot />,
//   //   // </StaticRouter>,
//   // );
//   const main = <MainRoot />;
//   expect(true).toBeTruthy();
// });
