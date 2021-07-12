const debug = require('debug')('app');
const { port, serverUrl, serverDomain } = require('./config');
const { app } = require('./app');

app.listen(port, () => debug('server is up on ', serverDomain || `${serverUrl}:${port}`));
