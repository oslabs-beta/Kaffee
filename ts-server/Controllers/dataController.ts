//for read/write from local JSON file
import express, { Express, Request, Response, NextFunction } from "express";
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);

const __dirname = path.dirname(__filename);

const dataController: object = {
  //middleware to fetch data from local json file
  getData: (req: Request,res:Response, next: NextFunction) =>{
    console.log('entered getData')
    next()
  },
  //middleware to write data to the local json file
  addData: (req: Request,res:Response, next: NextFunction) => {
    console.log('entered addData')
    next()
  },
  //middleware to delete data from the local json file
  deleteData: (req:Request,res:Response,next:NextFunction) => {
    console.log('entered deleteData')
    next()
  }
}

export default dataController;