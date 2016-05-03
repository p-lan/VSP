var yellowpagesCtrl = angular.module('yellowpagesCtrl', []);

app.controller('getServices', function ($scope, $http){

  console.log("helloWorld");
  var uri;

  // Simple GET request example:
  $http({
    method: 'GET',
    url: ' https://141.22.34.15/cnt/172.18.0.5/4567/services/of/name/lmnp_events'
  }).then(function successCallback(response) {
    console.log(response.data.services[1]);
    uri = response.data.services[1];
    console.log(response);
    var events = 'https://141.22.34.15/cnt/172.18.0.5/4567' + uri + '/events/0';
    console.log(events);

  }, function errorCallback(response) {
    console.log("Something is going wrong");
  // }).then(function gotData(events){
  //   // Simple GET request example:
  //   $http({
  //     method: 'GET',
  //     url: events
  //   }).then(function successCallback(response) {
  //     console.log(response);
  //   }, function errorCallback(response) {
  //     console.log("Something is going wrong");
  //   });
  });



});
