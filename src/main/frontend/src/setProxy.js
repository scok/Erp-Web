const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/productionForm',
    createProxyMiddleware({
      target: 'http://localhost:8877',
      changeOrigin: true,
    })
  );
};