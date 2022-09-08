const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  useEslint: false,
  devServer: {
    // 'auto' | 'all' [string] here
    allowedHosts: "all"
  }
  
}

)


