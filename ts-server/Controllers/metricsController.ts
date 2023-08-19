//for querying JMX and collecting metric data
import express, { Express, Request, Response, NextFunction } from "express";
import path from 'path';
import { fileURLToPath } from 'url';
const __filename = fileURLToPath(import.meta.url);

const __dirname = path.dirname(__filename);
const metricsController: object = {
  //middleware to request, receive, and parse metric data from JMX
  getBytes: (req:Request,res:Response,next:NextFunction) => {
    try {
      fetch('http://localhost:8080/get-metric/bytes', {method: "GET"})
      .then((response) => {
        response.json()
        .then((result) => {
          res.locals.data = result
          console.log(res.locals.data)
          next()
        }
        )
      })
    } catch (err) {
      next({errMsg: "An internal server error occured", err: 500})
    }
  },
  //middleware to connect to current cluster and obtain live data stream
  getCluster: (req:Request,res:Response,next:NextFunction) => {
    console.log('entered getCluster')
    next()
  },
  //middleware to add a new cluster and connect to it
  addCluster: (req:Request,res:Response,next:NextFunction) => {
    console.log('entered addCluster')
    next()
  },
  //middleware to remove a cluster from the list
  deleteCluster: (req:Request,res:Response,next:NextFunction) => {
    console.log('entered deleteCluster')
    next()
  }
}

export default metricsController;