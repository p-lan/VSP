var yellowpagesCtrl = angular.module('yellowpagesCtrl', []);

app.controller('getServices', ['$scope', '$http', function ($scope, $http){

  $scope.games = ["http://123","http://1234","http://12345"];
  $scope.openGames = [];
  var helpUrl = "";

  //getServicesByName();
  getGames();

  $scope.joinGame = function (){
    var request = {
      "name" : "/user/" + $scope.user.name,
      "ready" : false
    };
    console.log(request);
    $http({
      method: 'POST',
      url: $scope.user.game,
      dataType: 'json',
      data: request,
      headers: { 'Content-Type': 'application/json' }
    }).then(function successCallback(response) {

    }, function errorCallback(response) {
      console.log("Something is going wrong");
    });

    // TODO diesen block zu successCallback schieben
    window.localStorage['user'] = JSON.stringify($scope.user);
    console.log(window.localStorage['user']);
    window.location.href="#/play"
    
  };

  //---------------------- TEST - Functions --------------------//
  // Fill the games-array with EVENTS!!!
  function getServicesByName(){
    var gameIds = [];
    // Get service-id by name and put them in gameIds-array
    $http({
      method: 'GET',
      url: ' https://141.22.34.15/cnt/172.18.0.5/4567/services/of/name/lmnp_games'
    }).then(function successCallback(response) {
      console.log("ServiceIDs: " + resonse);
      gameIds = response.data.services.slice();
    }, function errorCallback(response) {
      console.log("Something is going wrong");
    }).then(function gotData(){
      gameIds.forEach(getURI);
        console.log("gameIds: " + gameIds);
    }).then(function test(){
      console.log("test");
    });

    // Search for an service-id the right URL
    var getURI = function(id, i, array){
      $http({
        method: 'GET',
        url: 'https://141.22.34.15/cnt/172.18.0.5/4567' + id
      }).then(function successCallback(response) {
        $scope.games.push(response.data.uri);
        console.log($scope.games);
      }, function errorCallback(response) {
        console.log("Something is going wrong");
      });
    };
  };

  //---------------------- Functions --------------------//
  // Fill the games-array with
  function getGames(){
    var gameServiceIds = [];
    var gameServiceUrl = [];
    var gameUrls = [];
    // Get service-id by name and put them in gameServiceIds-array
    $http({
      method: 'GET',
      url: ' https://141.22.34.15/cnt/172.18.0.5/4567/services/of/name/lmnp_games'
    }).then(function successCallback(response) {
      gameServiceIds = response.data.services.slice();
        console.log(gameServiceIds);
    }, function errorCallback(response) {
      console.log("Something is going wrong on gameServiceIds");
    }).then(function thenGetGameServiceUrl(){
      gameServiceIds.forEach(getGameServiceUrl); //--- 1
    });

    //--- 1
    // Search for an service-id the right URL
    var getGameServiceUrl = function(id, i, array){
      $http({
        method: 'GET',
        url: 'https://141.22.34.15/cnt/172.18.0.5/4567' + id
      }).then(function successCallback(response) {
        gameServiceUrl.push(response.data.uri);
        console.log("gameServiceUrl: " + gameServiceUrl);
      }, function errorCallback(response) {
        console.log("Something is going wrong on gameServiceUrl");
      }).then(function thenGetGameUrls(){
      console.log("gameServiceUrl: " + gameServiceUrl);
        gameServiceUrl.forEach(getGameUrls); //--- 2
      });
    };
    //--- 2
    // Search for Game URLs with gameServiceUrl
    var getGameUrls = function(serviceUrl, i, array){
      $http({
        method: 'GET',
        url: serviceUrl
      }).then(function successCallback(response) {
        //gameUrls.concat(response.data.id);
        console.log(serviceUrl);
        helpUrl = serviceUrl;
        var s = response.data[0];
        console.log("id: " + s.id);
        response.data.forEach(fillGameUrls);
        console.log("openGames: " + $scope.openGames);
      }, function errorCallback(response) {
        console.log("Something is going wrong on gameServiceUrl");
      });
    };
    // Hilfsfunktion
    var fillGameUrls = function(game, i, array){
      if (game.status.localeCompare("FINISHED") != 0) {
        $scope.openGames.push(helpUrl + "/" + game.id);
      }
    }

  };

}]);
