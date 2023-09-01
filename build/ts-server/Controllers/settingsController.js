import path from 'path';
import { fileURLToPath } from 'url';
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const settingsController = {
    postJMXPort: (req, res, next) => {
        try {
            // const settingsFile = path.resolve(__dirname, "../UserSettings/settings.json")
            fetch("http://localhost:8080/setJMXPort");
            next();
        }
        catch (error) {
            next({ err: 500, errMsg: "An error occurred in settingsController" });
        }
    },
    postKafkaUrl: (req, res, next) => {
        try {
            fetch("http://localhost:8080/setKafkaUrl");
            next();
        }
        catch (error) {
            next({ err: 500, errMsg: "An error occurred in settingsController/setKafkaUrl" });
        }
    },
    postKafkaPort: (req, res, next) => {
        try {
            fetch("http://localhost:8080/postKafkaPort");
            next();
        }
        catch (error) {
            next({ err: 500, errMsg: "An error occurred in settingsController/postKafkaPort" });
        }
    }
};
export default settingsController;
//# sourceMappingURL=settingsController.js.map