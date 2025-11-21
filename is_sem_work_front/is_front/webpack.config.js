const path = require('path')
const webpack = require('webpack')
const HtmlWebpackPlugin = require('html-webpack-plugin')

module.exports = [
    {
        mode: "development",
        devServer: {
            liveReload: true,
            static: {
                directory: path.join(__dirname, 'public'),
            },
            compress: true,
            port: 3000,
            historyApiFallback: true,
            hot: true,
            allowedHosts: "all"
        },
        watch: true,
        watchOptions: {
            aggregateTimeout: 300,
            poll: 1000,
            ignored: /node_modules/,
        },
        name: 'dev-configuration',
        context: path.resolve(__dirname, 'src'),
        entry: './index.jsx',
        output: {
            path: path.resolve(__dirname, 'dist'),
            filename: 'static/js/bundle.js',
            assetModuleFilename: 'assets/[name][ext]',
            clean: true,
            publicPath: '/',
        },
        devtool: 'source-map',
        module: {
            rules: [
                {
                    test: /\.(jsx|js)$/,
                    exclude: /node_modules/,
                    use: 'babel-loader',
                },
                {
                    test: /\.(png|svg|jpg|gif|ico)$/,
                    type: 'asset',
                },
                {
                    test: /\.(s[ac]ss)|css$/i,
                    use: ['style-loader', 'css-loader', 'sass-loader'],
                },
            ],
        },
        plugins: [
            new HtmlWebpackPlugin({
                title: 'Курсовая работа',
                template: path.resolve(__dirname, 'public/index.html'),
            }),
        ]
    }
];