var express = require("express");
var mongoose = require("mongoose");
var bodyParser= require('body-parser');
var cors = require('cors');
const fileUpload = require('express-fileupload');
var port = 5000;
var app= express();
mongoose.connect('mongodb://umar:Q!w2e3r4@localhost:27017/FUEGO?authSource=admin&maxPoolSize=200&minPoolSize=100').then(()=>{
    console.log("connection successful")

}).catch((err)=>{
    console.log(err)
})

app.use(cors())
app.use(bodyParser.urlencoded({limit: '50mb', extended: true }));
app.use(bodyParser.json({limit: '50mb'}));
app.use(fileUpload());

// app.listen(port);
var routes=require('./routes');
app.use('/api',routes);
app.listen(port, function() {
    console.log('Listening to port:  ' + port);
});
app.get("/",function(req,res){
    return res.send("mongo developer")
})
console.log("app is listening to:",port)        