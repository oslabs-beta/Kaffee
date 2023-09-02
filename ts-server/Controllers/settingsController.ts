import express, { Express, Request, Response, NextFunction } from 'express';
import path from 'path';
import fs from 'fs';
import { fileURLToPath } from 'url';
import dataController from './dataController';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
let settingsPath: string = '';

if (process.env.NODE_ENV === 'dev') {
  settingsPath = path.resolve(
    __dirname,
    '../../java-server/server/src/main/java/com/kaffee/server/settings.json'
  );
} else {
  settingsPath = path.resolve(
    __dirname,
    '../../build/target/classes/settings.json'
  );
}

const settingsController: object = {
  postJMXPort: (req: Request, res: Response, next: NextFunction) => {
    try {
      // const settingsFile = path.resolve(__dirname, "../UserSettings/settings.json")
      fetch('http://localhost:8080/setJMXPort');
      next();
    } catch (error) {
      next({ err: 500, errMsg: 'An error occurred in settingsController' });
    }
  },

  postKafkaUrl: (req: Request, res: Response, next: NextFunction) => {
    try {
      fetch('http://localhost:8080/setKafkaUrl');
      next();
    } catch (error) {
      next({
        err: 500,
        errMsg: 'An error occurred in settingsController/setKafkaUrl',
      });
    }
  },

  postKafkaPort: (req: Request, res: Response, next: NextFunction) => {
    try {
      fetch('http://localhost:8080/postKafkaPort');
      next();
    } catch (error) {
      next({
        err: 500,
        errMsg: 'An error occurred in settingsController/postKafkaPort',
      });
    }
  },

  updateSettings: (req: Request, res: Response, next: NextFunction) => {
    const { settingName, newValue } = req.body;

    fs.readFile(settingsPath, 'utf-8', (readErr, data) => {
      if (readErr) {
        console.error('Error reading settings:', readErr);
        return res.status(500).json({ error: 'Error reading settings' });
      }
      const settings = JSON.parse(data);
      settings[settingName] = newValue;
      console.log(newValue);
      fs.writeFile(
        settingsPath,
        JSON.stringify(settings, null, 2),
        'utf-8',
        (writeErr) => {
          if (writeErr) {
            console.error('Error writing settings:', writeErr);
            return res.status(500).json({ error: 'Error updating settings' });
          }
          console.log('Setting updated successfully');

          fetch(`http://localhost:8080/set${settingName}`);
          return next();
        }
      );
    });
  },

  //middleware to get user settings in settings.json
  getSettings: (req: Request, res: Response, next: NextFunction) => {
    fs.readFile(settingsPath, 'utf-8', (readErr, data) => {
      if (readErr) {
        console.error('Error reading settings:', readErr);
        return res.status(500).json({ error: 'Error reading settings' });
      }
      const settings = JSON.parse(data);
      res.locals.settings = settings;
      console.log(settings);
      next();
    });
  },
};

export default settingsController;
