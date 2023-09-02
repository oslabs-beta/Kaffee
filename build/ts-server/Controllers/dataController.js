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
const dataController = {
    //middleware to fetch data from local json file
    getLogFiles: (req, res, next) => {
        const folderPath = path.resolve(__dirname, '../History/');
        fs.readdir(folderPath, (err, files) => {
            if (err) {
                return next(err);
            }
            res.locals.filenames = files;
            return next();
        });
    },
    getData: (req, res, next) => {
        try {
            const { filename } = req.params;
            const filePath = path.resolve(__dirname, `../History/${filename}`);
            const data = fs.readFileSync(filePath, 'utf-8');
            res.locals.metrics = JSON.parse(data);
            return next();
        }
        catch (err) {
            return next(err);
        }
    },
    //middleware to write data to the local json file
    addData: (req, res, next) => {
        try {
            const filename = formatDate() + '_log.json';
            filePath = path.resolve(__dirname, `../History/${filename}`);
            const newData = req.body;
            let fileObj = {};
            try {
                const file = fs.readFileSync(filePath, 'utf-8');
                fileObj = JSON.parse(file);
                const metric = Object.keys(newData)[0];
                if (!Object.keys(fileObj).includes(metric)) {
                    fileObj[metric] = newData[metric];
                }
                else {
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
            }
            catch (err) {
                fileObj = newData;
            }
            fs.writeFileSync(filePath, JSON.stringify(fileObj));
            return next();
        }
        catch (error) {
            console.log(error);
            return next({ errMsg: 'err', err: 500 });
        }
    },
    //middleware to delete data from the local json file
    deleteData: (req, res, next) => {
        console.log('entered deleteData');
        next();
    },
};
export default dataController;
//# sourceMappingURL=dataController.js.map