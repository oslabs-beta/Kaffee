import { describe, expect, test } from '@jest/globals';
import { createRequire } from 'node:module';
import dataController, {
  formatDate,
} from '../../../ts-server/Controllers/dataController';

describe('base tests', () => {
  test('formats date correctly', () => {
    const dateRegex = /^d{4}-\d{2}-\d{2}$/;
    const date = formatDate();
    expect(dateRegex.test(date)).toBe(true);
  });
});
