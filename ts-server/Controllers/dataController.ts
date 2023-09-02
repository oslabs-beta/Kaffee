//for read/write from local JSON file
import express, {
  Express,
  Request,
  Response,
  NextFunction,
  response,
} from 'express';
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
  const year = date.getFullYear().toString(); // Extract last 2 digits of year
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
  getLogFiles: (req: Request, res: Response, next: NextFunction) => {
    const folderPath = path.resolve(__dirname, '../History/');

    fs.readdir(folderPath, (err, files) => {
      if (err) {
        return next(err);
      }

      res.locals.filenames = files;
      return next();
    });
  },
  getData: (req: Request, res: Response, next: NextFunction) => {
    try {
      const { filename } = req.params;
      const filePath = path.resolve(__dirname, `../History/${filename}`);
      const data = fs.readFileSync(filePath, 'utf-8');
      res.locals.metrics = JSON.parse(data);
      return next();
    } catch (err) {
      return next(err);
    }
  },
  //middleware to write data to the local json file
  addData: (req: Request, res: Response, next: NextFunction) => {
    type SetObj = {
      [name: string]: Array<number>;
    };

    type HistoryObject = {
      [metric: string]: {
        labels: Array<number>;
        datasets: Array<SetObj>;
      };
    };

    try {
      const filename = formatDate() + '_log.json';
      filePath = path.resolve(__dirname, `../History/${filename}`);
      const newData = req.body;
      let fileObj: HistoryObject = {};

      try {
        const file = fs.readFileSync(filePath, 'utf-8');
        fileObj = JSON.parse(file);
        const metric: string = Object.keys(newData)[0];
        if (!Object.keys(fileObj).includes(metric)) {
          fileObj[metric] = newData[metric];
        } else {
          fileObj = JSON.parse(file);
          fileObj[metric].labels.push(...newData[metric].labels);
          for (const newMetrics of newData[metric].datasets) {
            for (const savedMetrics of fileObj[metric].datasets) {
              if (savedMetrics.label === newMetrics.label) {
                savedMetrics.data.push(...newMetrics.data);
              }
            }
          }
        }
      } catch (err) {
        fileObj = newData;
      }

      fs.writeFileSync(filePath, JSON.stringify(fileObj));

      return next();
    } catch (error) {
      console.log(error);
      return next({ errMsg: 'err', err: 500 });
    }
  },
  //middleware to delete data from the local json file
  deleteData: (req: Request, res: Response, next: NextFunction) => {
    console.log('entered deleteData');
    next();
  },

  //middleware to update user settings in settings.json
  updateSettings: (req: Request, res: Response, next: NextFunction) => {
    const { settingName, newValue } = req.body;
    let settingsPath: string;
    if (process.env.NODE_ENV === 'dev') {
      settingsPath = path.resolve(
        __dirname,
        '../../java-server/server/src/main/java/com/kaffee/server/settings.json'
      );
    } else if (process.env.NODE_ENV === 'prod') {
      settingsPath = path.resolve(
        __dirname,
        '../../build/target/classes/settings.json'
      );
    } else {
      const Error = 'no path to settings was set';
      return next({ message: Error });
    }

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
    const settingsPath = path.resolve(
      __dirname,
      '../../java-server/server/src/main/java/com/kaffee/server/settings.json'
    );
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

export default dataController;
