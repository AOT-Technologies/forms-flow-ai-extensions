const webpack = require('webpack');

module.exports = {
  runtimeCompiler: true,
  devServer: {
    port: '3000',
    proxy: {
      '/engine-rest': {
        target: 'http://localhost:8080/',
        changeOrigin: true,
        pathRewrite: {
  
        }
      }
    }
  },
  configureWebpack: {
    plugins: [
      new webpack.IgnorePlugin(
        {
          resourceRegExp: /^\.\/locale$/,
          contextRegExp: /moment$/
        })
    ]
  }
};
