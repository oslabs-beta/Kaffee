import { test, expect } from 'vitest';
import { render } from '@testing-library/react';

import MainRoot from '../MainRoot';

// Test gives 8 errors on websockets and Java server if not connected to Java server/Apache

test('if "MainRoot" renders', () => {
  render(<MainRoot />);
});
