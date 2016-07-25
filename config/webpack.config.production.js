const configure = require('./webpack.config');

module.exports = configure(
  process.env,
  { NODE_ENV: 'production' }
);
