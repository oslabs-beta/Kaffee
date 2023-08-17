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

      while (count > 0) {
        const val: number = Math.floor(Math.random() * 1000);
        returnObj.data.push(val);
        count--;
      }

      return returnObj;
    }

    res.locals.data = generateData(quantity);
    next();
  },
};

export default metricsController;
