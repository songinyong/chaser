const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    // 'auto' | 'all' [string] here
    allowedHosts: "all"
  }
  
}

)


