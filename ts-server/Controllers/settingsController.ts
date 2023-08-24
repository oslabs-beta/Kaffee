import express, { Express, Request, Response, NextFunction } from 'express';
import path from 'path';
import { fileURLToPath } from 'url';
import dataController from './dataController.ts';
import settings from '../UserSettings/settings.json' assert {type: 'json'};
const __filename = fileURLToPath(import.meta.url);

const __dirname = path.dirname(__filename);

const settingsController: object = {
  postSettings: (req:Request, res:Response, next:NextFunction) => {
    try {
      // const settingsFile = path.resolve(__dirname, "../UserSettings/settings.json")
      fetch("http://localhost:8080/setJMXPort", {method:"POST", body: JSON.stringify(settings.JMX_PORT), headers:{
        "content-type":"application/json"
      }})
      .then((response) => response.json()
      .then((result) => {
        console.log(result);
        next();
      }))
    } catch (error) {
      next({err:500, errMsg: "An error occurred in settingsController"})
    }
  }
}

export default settingsController;