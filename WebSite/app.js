'use strict'

var express      = require('express'),
    app          = express(),
    server       = require('http').createServer(app),
    io           = require('socket.io')(server, { wsEngine: 'ws' }),
    port         = process.env.PORT || 3000,
    admin        = require("firebase-admin"),
    device_id    = null,
    device_name  = null,
    url          = null,
    motion       = null,
    numstreaming = 0,
    bodyParser = require('body-parser'),
    socket_io;


app.use(express.static(__dirname + '/public'));
app.use(bodyParser.urlencoded({ extended: false }))
app.set('view engine', 'jade')


server.listen(port, function () {
  console.log('Server listening at port %d', port);
});


app.get('/', (req, res) => {
    res.render('index');
})


// -- FIREBASE CONFIGURATION
// Fetch the service account key JSON file contents
var serviceAccount = require("./elegant-rock-208509-firebase-adminsdk-denf0-7dbbc2e37b.json"); //AndroidVideoStreamingUsers-e8b614f9dcca / AndroidVideoStreamingUsers-0104103df27a

// Initialize the app with a service account, granting admin privileges
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://elegant-rock-208509.firebaseio.com"
});

// As an admin, the app has access to read and write all data, regardless of Security Rules
var db      = admin.database();
var ref     = db.ref('num_streamings');
var refr    = db.ref('/');


// -- WEBSOCKET
io.on('connection', function (socket) {
    socket_io = socket;

    socket.on('send_mensaje', function(data){
        refr.update({mensajes: data.mensaje})
        console.log(data.mensaje);
    })

    socket.on('load_page', function(){
        console.log("Pagina recargada, mostrando streamings si los hubiera...")
        manageDatabase();
    })
});


manageDatabase();
function manageDatabase(){
    ref.on("value", function(snapshot) {
        var cont              = 0 ;
        numstreaming          = snapshot.val();
        var arr               = new Array();
        var array_dispostivos = new Array();

        refr.on("child_added", function(snapshot, prevChildKey) {
            cont ++;
            if(cont == 1){
                snapshot.forEach(function (android_id) {
                    device_id   = android_id.val().deviceId;
                    device_name = android_id.val().deviceName;
                    url         = android_id.val().url;
                    motion      = android_id.val().motion;
                    arr = [device_id, device_name, url, motion];
                    array_dispostivos.push(arr);
                });

                if(socket_io != undefined){
                    console.log("socket_io.emit 1")
                    socket_io.emit('data_streaming',{'num_streamings': numstreaming, 'dispositivos': array_dispostivos});
                }
            }
        });

        refr.on("child_removed", function(snapshot) {
            cont--;
            console.log("socket_io.emit 2")
            snapshot.forEach(function (android_id) {
                device_id   = android_id.val().deviceId;
                device_name = android_id.val().deviceName;
                url         = android_id.val().url;
                motion      = android_id.val().motion;
                arr = [device_id, device_name, url, motion];
                array_dispostivos.push(arr);
            });

            if(socket_io != undefined){
                console.log("socket_io.emit 2")
                socket_io.emit('data_streaming',{'num_streamings': numstreaming, 'dispositivos': array_dispostivos});
            }
        });
    });

}
