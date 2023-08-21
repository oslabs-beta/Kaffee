//for read/write from local JSON file
import express, { Express, Request, Response, NextFunction, response } from "express";
import path from 'path';
import { fileURLToPath } from 'url';
import * as fs from 'fs';
import os from 'os';

const __filename = fileURLToPath(import.meta.url);

const __dirname = path.dirname(__filename);

const desktopPath = path.join(os.homedir(), 'Desktop');
const filePath = path.join(desktopPath, 'kaffee_log.json');
const content = JSON.stringify('fuck');

// fs.writeFile(filePath, content, (err) => {
//   if (err) {
//     console.log(err)
//   } else {
//     console.log('file written')
//   }
// })

// fs.readFile(filePath, (err: NodeJS.ErrnoException | null, data: Buffer) => {
//   if (err) {
//     console.error('error', err);
//     return;
//   } else {
//     console.log(data.toString());
//   }
// })

const dataController: object = {
  //middleware to fetch data from local json file
  getData: (req: Request,res:Response, next: NextFunction) =>{
    fs.readFile(filePath, (err: NodeJS.ErrnoException | null, data: Buffer) => {
      if (err) {
        console.error('error', err);
        return;
      }
    })
    next()
  },
  //middleware to write data to the local json file
  addData: (req: Request,res:Response, next: NextFunction) => {
    try {
      fs.appendFile(filePath, JSON.stringify(res.locals.data), (err) => {
        if (err) {
          console.log(err)
        } else {
          console.log('file written')
          next()
        }
      })
    } catch (error) {
      console.log(error)
      next({errMsg: "Fucked", err: 500})
    }
  },
  //middleware to delete data from the local json file
  deleteData: (req:Request,res:Response,next:NextFunction) => {
    console.log('entered deleteData')
    next()
  },

  //middleware to update user settings in settings.json
  updateSettings: (req: Request, res: Response, next: NextFunction) => {
    const { settingName, newValue } = req.body;
    const settingsPath = path.join(__dirname, '..', 'UserSettings', 'settings.json');
    fs.readFile(settingsPath, 'utf-8', (readErr, data) => {
      if (readErr) {
        console.error('Error reading settings:', readErr);
        return res.status(500).json({ error: 'Error reading settings'});
      }
      const settings = JSON.parse(data);
      settings[settingName] = newValue;
      console.log(newValue)
      fs.writeFile(settingsPath, JSON.stringify(settings, null, 2), 'utf-8', (writeErr) => {
        if (writeErr) {
          console.error('Error writing settings:', writeErr);
          return res.status(500).json({ error: 'Error updating settings' });
        }
        console.log('Setting updated successfully');
        return res.status(200).json({ message: 'Setting updated successfully' });
      });
    });
  },

  //middleware to get user settings in settings.json
  getSettings: (req: Request, res: Response, next: NextFunction) => {
    const settingsPath = path.join(__dirname, '..', 'UserSettings', 'settings.json');
    fs.readFile(settingsPath, 'utf-8', (readErr, data) => {
      if (readErr) {
        console.error('Error reading settings:', readErr);
        return res.status(500).json({error: 'Error reading settings'})
      }
      const settings = JSON.parse(data);
      res.locals.settings = settings;
      next();
    })
  }

}

export default dataController;