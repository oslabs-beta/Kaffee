import express from "express";
import path from 'path';
const app = express();
const PORT = 3030;
app.use('/', (req, res) => {
    res.status(200).sendFile(path.resolve(__dirname, 'src/index.tsx'));
});
app.listen(PORT, () => console.log(`Connected to ${PORT}`));
//# sourceMappingURL=server.js.map