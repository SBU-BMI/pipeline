const http = require('http');
const url = require('url');
const exec = require('child_process').exec;
const PORT = 5001;

function handleRequest(request, response) {

    var url_obj = url.parse(request.url);
    var str = '', cmd = '', key = '', val = '';

    if (url_obj.query) {

        // Split it by & and you've got key/value pair.
        url_obj.query.split('&').forEach(function (pp) {

            // Split it by = and you've got the key and the value.
            pp = pp.split('=');
            key = pp[0];
            val = pp[1];

            // Build a string to pass to our script.
            str += (' -' + key + ' "' + val + '"');

        })
    }

    cmd = (__dirname + "/kumquat/run_chunk_wsi.sh " + str);

    console.log("cmd:", cmd);

    const child = exec(cmd,
        (error, stdout, stderr) => {
            console.log(`stdout: ${stdout}`);
            console.log(`stderr: ${stderr}`);
            if (error !== null) {
                console.log(`exec error: ${error}`);
            }
        });

    response.end('OK. Job is running...\nCheck caMicroscope in about an hour for results.\n');
}

const server = http.createServer(handleRequest);

server.listen(PORT, function () {
    console.log("Server listening on: http://localhost:%s", PORT);
});

