const path = require('path');
const webpack = require('webpack');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const LodashModuleReplacementPlugin = require('lodash-webpack-plugin');

var config = {
  entry: "./customFormioComponents/index.js",
  output: {
    library: 'FormioCustom',
    libraryTarget: 'umd',
    libraryExport: 'default',
    path: path.resolve(__dirname, 'dist'),
    filename: 'customformio.js',
    sourceMapFilename: "customformio.js.map"
  },
  performance: { hints: false },
  mode: 'development',
  resolve: {
    extensions: [".tsx", ".ts", ".jsx", ".js", ".json"],
  },
  optimization: {
    minimizer: [new UglifyJsPlugin()],
  },
  module: {
    rules: [
      {
        test: /\.m?js/,
        type: "javascript/auto",
      },
      {
        test: /\.(css|s[ac]ss)$/i,
        use: ["style-loader", "css-loader", "postcss-loader"],
      },
      {
        test: /\.(ts|tsx|js|jsx)$/,
        exclude: /node_modules/,
        loader: 'babel-loader',
        options: { presets: ['@babel/env', '@babel/preset-react'] },
      },
    ],
  },
  plugins: [
    new LodashModuleReplacementPlugin(),
     // Ignore all locale files of moment.js
    new webpack.IgnorePlugin(/^\.\/locale$/, /moment$/),
  ]
};


module.exports = (env, argv) => {
  if (argv.mode === 'development') {
    config.devtool = 'source-map';
  }

  if (argv.mode === 'production') {
    //...
  }

  // Will take dependency from consumer project. Use --env exclude=react-formio
  if (env.exclude === 'react-formio') {
    config.externals = {
      'formiojs' : 'formiojs',
      'react-formio': 'react-formio',
    }
    config.output.filename = 'customformio-ex.js'
  }

  return config;
};
