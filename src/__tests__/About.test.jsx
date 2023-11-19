import { expect, test } from 'vitest';
import { render } from '@testing-library/react';

import About from '../containers/About.jsx';

const aboutPage = render(<About />);

test('has link to github repo', () => {
  const link = aboutPage.getByTestId('github-link');
  expect(link.href).toContain('https://github.com/oslabs-beta/Kaffee');
});

test('has kaffee logo', () => {
  const logoImg = aboutPage.getByTestId('kaffee-logo');
  expect(logoImg.src).toContain('logo.png');
  aboutPage.unmount();
});
