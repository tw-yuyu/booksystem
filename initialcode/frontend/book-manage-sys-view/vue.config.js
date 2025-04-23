/**
 * 所有請求（不管路徑）都會被轉發到後端 http://localhost:21090
 * 因此瀏覽器會以為是同源請求，不會觸發 CORS
 */
module.exports = {
    lintOnSave: false,
    devServer: {
        host: "localhost",
        port: 21091,
        https: false,
        proxy: "http://localhost:21090",
        overlay: {
            warning: false,
            errors: false
        },
    }
}