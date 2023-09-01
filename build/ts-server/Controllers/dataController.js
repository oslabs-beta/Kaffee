import path from 'path';
import { fileURLToPath } from 'url';
import * as fs from 'fs';
import os from 'os';
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const desktopPath = path.join(os.homedir(), 'Desktop');
let filePath = path.join(desktopPath, 'kaffee_log.json');
const content = JSON.stringify('test');
function formatDate() {
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
const dataController = {
    //middleware to fetch data from local json file
    getData: (req, res, next) => {
        fs.readFile(filePath, (err, data) => {
            if (err) {
                console.error('error', err);
                return;
            }
        });
        next();
    },
    //middleware to write data to the local json file
    addData: (req, res, next) => {
        try {
            console.log(req.body);
            const filename = formatDate() + '_log.json';
            console.log(filename);
            filePath = path.resolve(__dirname, `../History/${filename}`);
            console.log(filePath);
            fs.appendFile(filePath, JSON.stringify(req.body, null, 2), (err) => {
                if (err) {
                    console.log(err);
                }
                else {
                    console.log('file written');
                    next();
                }
            });
        }
        catch (error) {
            console.log(error);
            next({ errMsg: "err", err: 500 });
        }
    },
    //middleware to delete data from the local json file
    deleteData: (req, res, next) => {
        console.log('entered deleteData');
        next();
    },
    //middleware to update user settings in settings.json
    updateSettings: (req, res, next) => {
        const { settingName, newValue } = req.body;
        const settingsPath = path.join(__dirname, '..', 'UserSettings', 'settings.json');
        fs.readFile(settingsPath, 'utf-8', (readErr, data) => {
            if (readErr) {
                console.error('Error reading settings:', readErr);
                return res.status(500).json({ error: 'Error reading settings' });
            }
            const settings = JSON.parse(data);
            settings[settingName] = newValue;
            console.log(newValue);
            fs.writeFile(settingsPath, JSON.stringify(settings, null, 2) + ',', 'utf-8', (writeErr) => {
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
    getSettings: (req, res, next) => {
        const settingsPath = path.join(__dirname, '..', 'UserSettings', 'settings.json');
        fs.readFile(settingsPath, 'utf-8', (readErr, data) => {
            if (readErr) {
                console.error('Error reading settings:', readErr);
                return res.status(500).json({ error: 'Error reading settings' });
            }
            const settings = JSON.parse(data);
            res.locals.settings = settings;
            next();
        });
    }
};
export default dataController;
//# sourceMappingURL=dataController.js.map