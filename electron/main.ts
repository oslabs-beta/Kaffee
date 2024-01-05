import { spawn } from 'child_process';
import { app, BrowserWindow } from 'electron/main';
import * as path from 'path';

const createWindow = () => {
  const win = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: path.join(__dirname, electron, 'preload.js'),
    },
  });

  win.loadFile('index.html');
};

app.whenReady().then(() => {
  startServer();
  createWindow();

  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
      createWindow();
    }
  });
});

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit();
  }
});

function startServer() {
  // let server = `${path.join(app.getAppPath(), 'server/target/server-1.1.1-SNAPSHOT.jar')}`;
  const server = `${path.join(app.getAppPath(), 'server/mvn spring-boot:run')}`;
  console.log(`Launching server with jar ${server}...`);
  // const serverProcess = spawn('java', ['-jar', server]);
  const serverProcess = spawn(server);
  if (serverProcess.pid) {
    console.log('Server PID: ' + serverProcess.pid);
  } else {
    console.log('Failed to launch server process');
  }
}
