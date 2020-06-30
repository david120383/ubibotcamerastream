var exec = require('cordova/exec');

exports.start = function (arg0, success, error) {
    exec(success, error, 'ubibotcamerastream', 'start', [arg0]);
};

exports.stop = function (arg0, success, error) {
    exec(success, error, 'ubibotcamerastream', 'stop', [arg0]);
};

exports.getversion = function (arg0, success, error) {
    exec(success, error, 'ubibotcamerastream', 'getversion', [arg0]);
};
