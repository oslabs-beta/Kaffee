// const path = require('path');
// const HTMLWebpackPlugin = require('html-webpack-plugin');
import HtmlWebpackPlugin from 'html-webpack-plugin';
import NodePolyfillPlugin from 'node-polyfill-webpack-plugin';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

export default {
  entry: './src/index.tsx',
  // entry: './src/index.jsx',

  output: {
    path: path.join(__dirname, '/dist'),
    filename: 'bundle.js',
  },
  mode: 'development',
  devtool: 'inline-source-map',
  devServer: {
    static: {
      directory: path.resolve(__dirname, 'dist'),
      publicPath: '/',
    },
    port: 6060,
    proxy: {
      '/api': {
        target: 'http://localhost:3030/',
        pathRewrite: { '^/api': '' },
      },
      // '/get-metrics': {
      //   target: 'http://localhost:8080/',
      //   pathRewrite: { '^/get-metrics': '' },
      // },
    },
    open: true,
    hot: true,
    historyApiFallback: true,
  },

  plugins: [
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, 'src/template.html'),
    }),
    new NodePolyfillPlugin(),
  ],

  resolve: {
    extensions: ['.ts', '.tsx', '.js', '.jsx'],
  },
  module: {
    rules: [
      {
        test: /\.[tj]sx?$/i,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
        },
      },
      // {
      //   test: /\.tsx?$/,
      //   exclude: [/node_modules/],
      //   use: {
      //     loader: 'ts-loader',
      //     options: {
      //       compilerOptions: {
      //         jsx: 'preserve',
      //       },
      //     },
      //   },
      // },
      {
        test: /\.s?[ca]ss$/,
        use: ['style-loader', 'css-loader', 'sass-loader'],
      },
    ],
  },
};
