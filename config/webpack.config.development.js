const configure = require('./webpack.config');

module.exports = configure(
  { NODE_ENV: 'development' }
);
