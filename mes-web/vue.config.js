module.exports = {
    assetsDir: 'static',
    parallel: false,
    publicPath: './',
    presets: [
        '@vue/app',
        [
            '@babel/preset-env',
            {
                useBuiltIns: 'entry'
            }
        ]
    ],
    sourceType: 'unambiguous',
};