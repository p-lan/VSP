var express = require('express');
var bodyParser = require('body-parser');
var app = express();

// --------------------------- App use ----------------------------
app.use(express.static(__dirname + '/www'));
app.use(bodyParser.json());

app.get('/', function (req, res) {
  res.send('Hello World!');
});

app.listen(8080, function () {
  console.log('Example app listening on port 8080!');
});
