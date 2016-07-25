/* eslint prefer-rest-params: ["off"] */

const webpack = require('webpack');
const path = require('path');

module.exports = function webpackConfig() {
  const environment = Object.assign.apply(null, arguments);
  const development = (environment.NODE_ENV !== 'production');

  return {
    devtool: development ? 'eval-source-map' : undefined,
    output: {
      filename: 'bundle.js',
      path: path.resolve(__dirname, '../src/main/resources/static/js'),
      publicPath: '/',
    },
    resolve:{
        alias:{
          "js-map-label": "../../../node_modules/js-map-label/src/maplabel-compiled.js"
        }
      },
    module: {
      loaders: [
        {
          test: /\.jsx?$/,
          loaders: ['babel-loader'],
          exclude: [/node_modules/, /web_modules/],
        },
      ],
    },
    entry: [
      path.resolve(__dirname, '../src/main/js/main.js'),
    ],
    plugins: [
      new webpack.DefinePlugin({
        'process.env': JSON.stringify(environment),
      }),
    ],
    externals: {
        'google': 'google',
    }
  };
};
