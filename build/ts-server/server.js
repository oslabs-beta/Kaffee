import express from 'express';
import path from 'path';
import dataController from './Controllers/dataController.js';
import metricsController from './Controllers/metricsController.js';
import testController from './Controllers/testController.js';
import settingsController from './Controllers/settingsController.js';
import cors from 'cors';
const app = express();
const PORT = 3030;
app.use(cors());
import { fileURLToPath } from 'url';
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(express.static(path.resolve(__dirname, '../dist/')));
app.get('/', (req, res) => {
    res.status(200).sendFile(path.resolve(__dirname, '../src/template.html'));
});
app.use('/test', testController['runTest'], (req, res) => {
    res.status(200).json(res.locals.data);
});
app.use('/stopTest', testController['stopTest'], (req, res) => {
    res.status(200).json(res.locals.data);
});
// post requests to java server to update from settings.json
app.use('/setJMX', settingsController['postJMXPort'], (req, res) => {
    res.sendStatus(200);
});
app.use('/setKafkaUrl', settingsController['postKafkaUrl'], (req, res) => {
    res.sendStatus(200);
});
app.use('/setKafkaPort', settingsController['postKafkaPort'], (req, res) => {
    res.sendStatus(200);
});
app.use('/getBytes', metricsController['getBytes'], dataController['addData'], (req, res) => {
    console.log(res.locals.data);
    res.status(200).json(res.locals.data);
});
app.get('/dummy/:count', metricsController['dummy'], (req, res) => {
    res.status(200).json(res.locals.data);
});
app.use('/getCluster', metricsController['getCluster'], (req, res) => {
    res.sendStatus(200);
});
app.use('/addCluster', metricsController['addCluster'], (req, res) => {
    res.status(200).send('works');
});
app.use('/deleteCluster', metricsController['deleteCluster'], (req, res) => {
    res.status(200).send('works');
});
app.use('/getData/:filename', dataController['getData'], (req, res) => {
    res.json(res.locals.metrics);
});
app.use('/getLogFiles', dataController['getLogFiles'], (req, res) => {
    console.log(res.locals);
    res.json(res.locals.filenames);
});
app.use('/addData', dataController['addData'], (req, res) => {
    res.sendStatus(200);
});
app.use('/deleteData', dataController['deleteData'], (req, res) => {
    res.sendStatus(200);
});
app.use('/updateSettings', settingsController['updateSettings'], (req, res) => {
    res.setHeader('Access-Control-Allow-Origin', 'http://localhost:6060');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
    res.status(200);
});
app.use('/getSettings', settingsController['getSettings'], (req, res) => {
    res.setHeader('Access-Control-Allow-Origin', 'http://localhost:6060');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
    res.status(200).json(res.locals.settings);
});
app.use('/', (req, res) => {
    res.status(404).send('What are you doing here?');
});
app.use((err, req, res) => {
    const defaultErr = {
        status: 400,
        errMsg: 'An unknown error occured',
    };
    const error = Object.assign(defaultErr, err);
    res.status(error['status']).json(error['errMsg']);
});
app.listen(PORT, () => console.log(`Connected to ${PORT}`));
//# sourceMappingURL=server.js.map