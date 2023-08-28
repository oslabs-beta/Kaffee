import express, { Express, Request, Response } from 'express';
import path from 'path';
import dataController from './Controllers/dataController.ts';
import metricsController from './Controllers/metricsController.ts';
import testController from './Controllers/testController.ts';
import settingsController from './Controllers/settingsController.ts';
import cors from 'cors';

const app: Express = express();
const PORT: number = 3030;
app.use(cors());

type dataKey = keyof typeof dataController;
type metricKey = keyof typeof metricsController;
type testKey = keyof typeof testController;
type settingKey = keyof typeof settingsController;

import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);

const __dirname = path.dirname(__filename);

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(express.static(path.resolve(__dirname, '../src/')));

app.get('/', (req: Request, res: Response) => {
  res.status(200).sendFile(path.resolve(__dirname, '../src/template.html'));
});

app.use('/test', testController['runTest' as testKey],(req:Request, res:Response) => {
  res.status(200).json(res.locals.data);
})

app.use('/stopTest', testController['stopTest' as testKey],(req:Request,res:Response) => {
  res.status(200).json(res.locals.data);
})


// post requests to java server to update from settings.json
app.use('/setJMX', settingsController['postJMXPort' as settingKey], (req:Request, res:Response) => {
  res.sendStatus(200);
})

app.use('/setKafkaUrl', settingsController['postKafkaUrl' as settingKey], (req:Request, res:Response) => {
  res.sendStatus(200);
})

app.use('/setKafkaPort', 
  settingsController['postKafkaPort' as settingKey], 
  (req:Request, res:Response) => {
    res.sendStatus(200);
})



app.use('/getBytes', metricsController['getBytes' as metricKey], dataController['addData' as metricKey], (req:Request,res:Response) => {
  console.log(res.locals.data)
  res.status(200).json(res.locals.data);
})

app.get(
  '/dummy/:count',
  metricsController['dummy' as metricKey],
  (req: Request, res: Response) => {
    res.status(200).json(res.locals.data);
  }
);


app.use(
  '/getCluster',
  metricsController['getCluster' as metricKey],
  (req: Request, res: Response) => {
    res.sendStatus(200);
  }
);

app.use(
  '/addCluster',
  metricsController['addCluster' as metricKey],
  (req: Request, res: Response) => {
    res.status(200).send('works');
  }
);

app.use(
  '/deleteCluster',
  metricsController['deleteCluster' as metricKey],
  (req: Request, res: Response) => {
    res.status(200).send('works');
  }
);

app.use(
  '/getData',
  dataController['getData' as dataKey],
  (req: Request, res: Response) => {
    res.sendStatus(200);
  }
);

app.use(
  '/addData',
  dataController['addData' as dataKey],
  (req: Request, res: Response) => {
    res.sendStatus(200);
  }
);

app.use(
  '/deleteData',
  dataController['deleteData' as dataKey],
  (req: Request, res: Response) => {
    res.sendStatus(200);
  }
);

app.use(
  '/updateSettings',
  dataController['updateSettings' as dataKey],
  (req: Request, res: Response) => {
    console.log('route hit')
    res.setHeader('Access-Control-Allow-Origin', 'http://localhost:6060');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
    res.status(200).json();
  }
);

app.use(
  '/getSettings',
  dataController['getSettings' as dataKey],
  (req: Request, res: Response) => {
    res.setHeader('Access-Control-Allow-Origin', 'http://localhost:6060');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
    res.status(200).json(res.locals.settings);
  }
)

app.use('/', (req: Request, res: Response) => {
  res.status(404).send('What are you doing here?');
});

app.use((err: object, req: Request, res: Response) => {
  const defaultErr: object = {
    status: 400,
    errMsg: 'An unknown error occured',
  };
  const error = Object.assign(defaultErr, err);
  type ObjectKey = keyof typeof error;
  res.status(error['status' as ObjectKey]).json(error['errMsg' as ObjectKey])
});


app.listen(PORT, () => console.log(`Connected to ${PORT}`));
