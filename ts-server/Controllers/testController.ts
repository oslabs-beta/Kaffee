import express, { Express, Request, Response, NextFunction } from 'express';
import path from 'path';
import { fileURLToPath } from 'url';
import dataController from './dataController.ts';
const __filename = fileURLToPath(import.meta.url);

const __dirname = path.dirname(__filename);

const testController: object = {
  runTest: (req:Request,res:Response,next:NextFunction) => {
    try {
      fetch("http://localhost:8080/test/runTest", {method:"GET"})
      .then((response) => {
        response.json()
        .then((result) => {
          res.locals.data = result
          next()
        })
      })
    } catch (error) {
      console.log('error!')
      next({err:500, errMsg: "Somethin fucky"})
    }
  },
  stopTest: (req:Request, res:Response, next:NextFunction) => {
    try {
      fetch("http://localhost:8080/test/stopTest", {method:"GET"})
      .then((response) => {
        response.json()
        .then((result) => {
          res.locals.data=result;
          next();
        })
      })
    } catch (error) {
      
    }
  }
}

export default testController;