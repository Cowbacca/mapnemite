const configure = require('./webpack.config');

module.exports = configure(
  require('./development.private.json'),
  { NODE_ENV: 'development' }
);
