import path from 'path';
import { fileURLToPath } from 'url';
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const metricsController = {
    //middleware to request, receive, and parse metric data from JMX
    getBytes: (req, res, next) => {
        try {
            fetch('http://localhost:8080/bytes', { method: 'GET' }).then((response) => {
                response.json().then((result) => {
                    res.locals.data = result;
                    console.log(res.locals.data);
                    next();
                });
            });
        }
        catch (err) {
            next({ errMsg: 'An internal server error occured', err: 500 });
        }
    },
    //middleware to connect to current cluster and obtain live data stream
    getCluster: (req, res, next) => {
        console.log('entered getCluster');
        next();
    },
    //middleware to add a new cluster and connect to it
    addCluster: (req, res, next) => {
        console.log('entered addCluster');
        next();
    },
    //middleware to remove a cluster from the list
    deleteCluster: (req, res, next) => {
        console.log('entered deleteCluster');
        next();
    },
    dummy: (req, res, next) => {
        const quantity = Number(req.params.count);
        function generateData(count, name = 'Test This') {
            const returnObj = {
                label: name,
                data: [],
            };
            const MAX_VAL = 1000;
            let i = 0;
            while (i < count) {
                const val = MAX_VAL * 0.005 * i * i + MAX_VAL * 0.01 * i + MAX_VAL * 0.1;
                const noise = Math.floor(Math.random() * (MAX_VAL * 0.4) - MAX_VAL * 0.2);
                returnObj.data.push(val + noise);
                i++;
            }
            return returnObj;
        }
        res.locals.data = generateData(quantity);
        return next();
    },
};
export default metricsController;
//# sourceMappingURL=metricsController.js.map