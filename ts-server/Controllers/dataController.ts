//for read/write from local JSON file
import express, { Express, Request, Response, NextFunction, response } from "express";
import path from 'path';
import { fileURLToPath } from 'url';
import * as fs from 'fs';
import os from 'os';

const __filename = fileURLToPath(import.meta.url);

const __dirname = path.dirname(__filename);

const desktopPath = path.join(os.homedir(), 'Desktop');
let filePath = path.join(desktopPath, 'kaffee_log.json');
const content = JSON.stringify('test');

function formatDate(): string {
  const date = new Date();

  // Extract year, month, and day
  const year = date.getFullYear().toString().slice(-2); // Extract last 2 digits of year
  const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-indexed, so add 1
  const day = String(date.getDate()).padStart(2, '0');

  // Format the date as YY-mm-dd
  return `${year}-${month}-${day}`; 
}

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
      console.log(req.body);
      const filename = formatDate() + '_log.json';
      console.log(filename);
      filePath = path.resolve(__dirname, `../History/${filename}`)
      console.log(filePath);
      fs.appendFile(filePath, JSON.stringify(req.body, null, 2), (err) => {
        if (err) {
          console.log(err)
        } else {
          console.log('file written')
          next()
        }
      })


    } catch (error) {
      console.log(error)
      next({errMsg: "err", err: 500})
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
    // if(process.env.NODE_ENV === 'dev'){
    // const settingsPath = path.resolve(__dirname, '../Settings/settings.json');
    // fs.readFile(settingsPath, 'utf-8', (readErr, data) => {
    //   if (readErr) {
    //     console.error('Error reading settings:', readErr);
    //     return res.status(500).json({ error: 'Error reading settings'});
    //   }
    //   const settings = JSON.parse(data);
    //   settings[settingName] = newValue;
    //   fs.writeFile(settingsPath, JSON.stringify(settings, null, 2), (err) => {console.log(err)})
    // });
    // fetch(`http://localhost:8080/set${settingName}`)
    // next();
    // }
    // else if(process.env.NODE_ENV === 'prod'){
      const settingsPath = path.resolve(__dirname, '../../target/classes/settings.json');
      fs.readFile(settingsPath, 'utf-8', (readErr, data) => {
        if (readErr) {
          console.error('Error reading settings:', readErr);
          return res.status(500).json({ error: 'Error reading settings'});
        }
        const settings = JSON.parse(data);
        settings[settingName] = newValue;
        fs.writeFile(settingsPath, JSON.stringify(settings, null, 2), (err) => {console.log(err)})
      });
      fetch(`http://localhost:8080/set${settingName}`)
      next();
      // }
  },

  //middleware to get user settings in settings.json
  getSettings: (req: Request, res: Response, next: NextFunction) => {
    const settingsPath = path.resolve(__dirname, '../../target/classes/settings.json');
    fs.readFile(settingsPath, 'utf-8', (readErr, data) => {
      if (readErr) {
        console.error('Error reading settings:', readErr);
        return res.status(500).json({error: 'Error reading settings'})
      }
      const settings = JSON.parse(data);
      res.locals.settings = settings;
      console.log(settings, 'these are the settings!!!!!!!!!')
      next();
    })
  }

}

export default dataController;
