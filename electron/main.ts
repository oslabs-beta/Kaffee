/*
 * Following the guide found at: https://medium.com/@sgstephans/creating-a-java-electron-react-typescript-desktop-app-414e7edceed2
 */

import { app, BrowserWindow } from 'electron';
import * as path from 'path';
import installExtension, {
  REACT_DEVELOPER_TOOLS,
} from 'electron-devtools-installer';
import { ChildProcessWithoutNullStreams, spawn } from 'child_process';

/**
 * Create the window for loading the electron app.
 */
function createWindow(): void {
  const win = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
    },
  });

  if (app.isPackaged) {
    win.loadURL(`file://${__dirname}/../index.html`);
  } else {
    win.loadURL('http://localhost:5173/');

    win.webContents.openDevTools();

    // Hot Reloading on 'node_modules/.bin/electronPath'
    // I would love to use ES6 import notation, but the module is giving me fits
    const reload = require('electron-reload');
    reload(__dirname, {
      electron: path.join(
        __dirname,
        '..',
        '..',
        'node_modules',
        '.bin',
        'electron',
      ),
      forceHardReset: true,
      hardResetMethod: 'exit',
    });
  }
}

/**
 * Start the server from the compiled jar or using the command line if in dev mode.
 *
 * @return pid if process starts, 0 otherwise
 */
function startServer(): Number {
  let child: ChildProcessWithoutNullStreams | null = null;

  if (app.isPackaged) {
    const jarPath = path.join(
      process.resourcesPath,
      'server',
      'target',
      'server-1.1.1-SNAPSHOT.jar',
    );
    child = spawn('java', ['-jar', jarPath]);
  } else {
    child = spawn('mvn', ['-f server/pom.xml', 'spring-boot:run']);
  }

  return child?.pid || 0;
}

/**
 * Loads the app when the electron's whenReady promise is fulfilled.
 */
app.whenReady().then(() => {
  // DevTools
  installExtension(REACT_DEVELOPER_TOOLS)
    .then((name) => {
      console.log(`Added Extension: ${name}`);
    })
    .catch((err) => {
      console.log('An error occurred: ', err);
    });

  const pid = startServer();
  console.log(pid);
  createWindow();

  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
      createWindow();
    }
  });

  app.on('window-all-closed', () => {
    if (pid !== -1) {
    }
    if (process.platform !== 'darwin') {
      app.quit();
    }
  });
});
