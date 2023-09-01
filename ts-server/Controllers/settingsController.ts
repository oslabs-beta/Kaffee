import express, { Express, Request, Response, NextFunction } from 'express';
import path from 'path';
import { fileURLToPath } from 'url';
import dataController from './dataController';
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const settingsController: object = {
  postJMXPort: (req:Request, res:Response, next:NextFunction) => {
    try {
      // const settingsFile = path.resolve(__dirname, "../UserSettings/settings.json")
      fetch("http://localhost:8080/setJMXPort")
      next();
    } catch (error) {
      next({err:500, errMsg: "An error occurred in settingsController"})
    }
  },

  postKafkaUrl: (req:Request, res: Response, next: NextFunction) => {
    try {
      fetch("http://localhost:8080/setKafkaUrl")
      next();
    } catch (error) {
      next({err:500, errMsg: "An error occurred in settingsController/setKafkaUrl"})
    }
  },

  postKafkaPort: (req:Request, res:Response, next:NextFunction) => {
    try {
      fetch("http://localhost:8080/postKafkaPort")
      next();
    } catch (error) {
      next({err:500, errMsg: "An error occurred in settingsController/postKafkaPort"})
    }
  }
  
}

export default settingsController;