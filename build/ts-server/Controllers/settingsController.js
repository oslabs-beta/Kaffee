import path from 'path';
import { fileURLToPath } from 'url';
import settings from '../../java-server/server/src/main/java/com/kaffee/server/UserSettings/settings.json' assert { type: 'json' };
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const settingsController = {
    postJMXPort: (req, res, next) => {
        try {
            // const settingsFile = path.resolve(__dirname, "../UserSettings/settings.json")
            fetch("http://localhost:8080/setJMXPort", { method: "POST", body: JSON.stringify(settings.JMX_PORT), headers: {
                    "content-type": "application/json"
                } })
                .then((response) => response.json()
                .then((result) => {
                console.log(result);
                next();
            }));
        }
        catch (error) {
            next({ err: 500, errMsg: "An error occurred in settingsController" });
        }
    },
    postKafkaUrl: (req, res, next) => {
        try {
            fetch("http://localhost:8080/setKafkaUrl", { method: "POST", body: JSON.stringify(settings.KAFKA_URL), headers: {
                    "content-type": "application/json"
                } })
                .then((response) => response.json()
                .then((result) => {
                console.log(result);
                next();
            }));
        }
        catch (error) {
            next({ err: 500, errMsg: "An error occurred in settingsController/setKafkaUrl" });
        }
    },
    postKafkaPort: (req, res, next) => {
        try {
            fetch("http://localhost:8080/postKafkaPort", {
                method: "POST",
                body: JSON.stringify(settings.KAFKA_PORT),
                headers: { "content-type": "application/json" }
            })
                .then(response => response.json() // not needed and will be removed later
                .then((response) => {
                console.log(response);
                return next();
            }));
        }
        catch (error) {
            next({ err: 500, errMsg: "An error occurred in settingsController/postKafkaPort" });
        }
    }
};
export default settingsController;
//# sourceMappingURL=settingsController.js.map