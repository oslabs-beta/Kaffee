//for read/write from local JSON file
import express, { Express, Request, Response, NextFunction } from "express";
import path from 'path';
import { fileURLToPath } from 'url';
import * as fs from 'fs';
import os from 'os';

const __filename = fileURLToPath(import.meta.url);

const __dirname = path.dirname(__filename);

const desktopPath = path.join(os.homedir(), 'Desktop');
const filePath = path.join(desktopPath, 'kaffee_log.json');
const content = JSON.stringify('fuck');

fs.writeFile(filePath, content, (err) => {
  if (err) {
    console.log(err)
  } else {
    console.log('file written')
  }
})

fs.readFile(filePath, (err: NodeJS.ErrnoException | null, data: Buffer) => {
  if (err) {
    console.error('error', err);
    return;
  } else {
    console.log(data.toString());
  }
})

const dataController: object = {
  //middleware to fetch data from local json file
  getData: (req: Request,res:Response, next: NextFunction) =>{
    fs.readFile(filePath, (err: NodeJS.ErrnoException | null, data: Buffer) => {
      if (err) {
        console.error('error', err);
        return;
      }
    })
    next()
  },
  //middleware to write data to the local json file
  addData: (req: Request,res:Response, next: NextFunction) => {
    try {
      fs.appendFile(filePath, res.locals.data.toString(), (err) => {
        if (err) {
          console.log(err)
        } else {
          console.log('file written')
          next()
        }
      })
    } catch (error) {
      console.log(error)
      next({errMsg: "Fucked", err: 500})
    }
  },
  //middleware to delete data from the local json file
  deleteData: (req:Request,res:Response,next:NextFunction) => {
    console.log('entered deleteData')
    next()
  }
}

export default dataController;