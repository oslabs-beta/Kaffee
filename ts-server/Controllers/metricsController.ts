//for querying JMX and collecting metric data
import express, { Express, Request, Response, NextFunction } from 'express';
import path from 'path';
import { fileURLToPath } from 'url';
import dataController from './dataController.ts';
const __filename = fileURLToPath(import.meta.url);

const __dirname = path.dirname(__filename);
const metricsController: object = {
  //middleware to request, receive, and parse metric data from JMX
  getMetric: (req: Request, res: Response, next: NextFunction) => {
    console.log('entered getMetric');
    next();
  },
  //middleware to connect to current cluster and obtain live data stream
  getCluster: (req: Request, res: Response, next: NextFunction) => {
    console.log('entered getCluster');
    next();
  },
  //middleware to add a new cluster and connect to it
  addCluster: (req: Request, res: Response, next: NextFunction) => {
    console.log('entered addCluster');
    next();
  },
  //middleware to remove a cluster from the list
  deleteCluster: (req: Request, res: Response, next: NextFunction) => {
    console.log('entered deleteCluster');
    next();
  },

  dummy: (req: Request, res: Response, next: NextFunction) => {
    type DataObject = {
      name: string;
      data: Array<number>;
    };

    const quantity: number = Number(req.params.count);

    function generateData(count: number, name = 'Test Data') {
      const returnObj: DataObject = {
        name: name,
        data: [],
      };

      const MAX_VAL: number = 1000;

      let i: number = 0;
      while (i < count) {
        const val: number =
          MAX_VAL * 0.005 * i * i + MAX_VAL * 0.01 * i + MAX_VAL * 0.1;
        console.log(`base ${val}`);

        const noise: number = Math.floor(
          Math.random() * (MAX_VAL * 0.4) - MAX_VAL * 0.2
        );
        console.log(`noise ${noise}`);

        returnObj.data.push(val + noise);
        i++;
      }

      return returnObj;
    }

    res.locals.data = generateData(quantity);
    next();
  },
};

export default metricsController;
