import express, { Express, Request, Response } from "express";
import path from 'path';
import dataController from "./Controllers/dataController.ts";
import metricsController from "./Controllers/metricsController.ts";
const app: Express = express();
const PORT: number = 3030;
type dataKey = keyof typeof dataController;
type metricKey = keyof typeof metricsController;
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);

const __dirname = path.dirname(__filename);

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(express.static(path.resolve(__dirname, '../src')));

app.get('/', (req: Request, res: Response)  => {
  res.status(200).sendFile(path.resolve(__dirname, '../src/template.html'));
});

app.use('/getMetrics', metricsController['getMetric' as metricKey], (req:Request,res:Response) => {
  res.status(200).send('works')
})

app.use('/getCluster', metricsController['getCluster' as metricKey], (req:Request,res:Response) => {
  res.sendStatus(200);
})

app.use('/addCluster', metricsController['addCluster' as metricKey], (req: Request,res:Response) => {
  res.status(200).send('works');
})

app.use('/deleteCluster', metricsController['deleteCluster' as metricKey], (req:Request,res:Response) => {
  res.status(200).send('works')
})

app.use('/getData', dataController['getData' as dataKey], (req:Request,res:Response) => {
  res.sendStatus(200);
})

app.use('/addData', dataController['addData' as dataKey], (req:Request,res:Response) => {
  res.sendStatus(200);
})

app.use('/deleteData', dataController['deleteData' as dataKey], (req:Request,res:Response) => {
  res.sendStatus(200);
})

app.use((req:Request, res:Response) => {
  res.status(404).send('What are you doing here?')
})

app.use((err: object, req:Request, res: Response) => {
  const defaultErr: object = {
    status: 400,
    errMsg: 'An unknown error occured'
  }
  const error = Object.assign(defaultErr, err)
  type ObjectKey = keyof typeof error;
  res.status(error['status' as ObjectKey]).send(error['errMsg' as ObjectKey])
})

app.listen(PORT, () => console.log(`Connected to ${PORT}`));