var yellowpagesCtrl = angular.module('yellowpagesCtrl', []);

app.controller('getServices', ['$scope', '$http', function ($scope, $http){

  $scope.games = ["http://123","http://1234","http://12345"];
  $scope.openGames = [];

  getServicesByName();

  $scope.joinGame = function (){
    console.log($scope.user);
    window.location.href="#/play"
  };

  //---------------------- TEST - Functions --------------------//
  // Fill the games-array with EVENTS!!!
  function getServicesByName(){
    var gameIds = [];
    // Get service-id by name and put them in gameIds-array
    $http({
      method: 'GET',
      url: ' https://141.22.34.15/cnt/172.18.0.5/4567/services/of/name/lmnp_events'
    }).then(function successCallback(response) {
      gameIds = response.data.services.slice();
    }, function errorCallback(response) {
      console.log("Something is going wrong");
    }).then(function gotData(){
      gameIds.forEach(getURI);
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
    }, function errorCallback(response) {
      console.log("Something is going wrong on gameServiceIds");
    }).then(function thenGetGameServiceUrl(){
      gameServiceIds.forEach(getGameServiceUrl); //--- 1
    }).then(function thenGetGameUrls(){
      console.log("gameServiceUrl: " + gameServiceUrl);
      gameServiceUrl.forEach(getGameUrls); //--- 2
    }).then(function thenSaveOpenGames(){
      gameUrls.forEach(saveOpenGames); //--- 3
    });

    //--- 1
    // Search for an service-id the right URL
    var getGameServiceUrl = function(id, i, array){
      $http({
        method: 'GET',
        url: 'https://141.22.34.15/cnt/172.18.0.5/4567' + id
      }).then(function successCallback(response) {
        gameServiceUrl.push(response.data.uri);
      }, function errorCallback(response) {
        console.log("Something is going wrong on gameServiceUrl");
      });
    };

    //--- 2
    // Search for Game URLs with gameServiceUrl
    var getGameUrls = function(serviceUrl, i, array){
      $http({
        method: 'GET',
        url: serviceUrl + "/games"
      }).then(function successCallback(response) {
        //gameUrls.concat(response.data.id);
        response.data.id.forEach(fillGameUrls);
        // Hilfsfunktion
        var fillGameUrls = function(url, i, array){
          gameUrls.push(serviceUrl + url);
          console.log(serviceUrl + url);
        }
      }, function errorCallback(response) {
        console.log("Something is going wrong on gameServiceUrl");
      });
    };

    //--- 3
    // Search for Open Games with Game URLs
    var saveOpenGames = function(gameUrl, i, array){
      $http({
        method: 'GET',
        url: gameUrl + "/status"
      }).then(function successCallback(response) {
        if (response.localeCompare("finished") != 0) {
          $scope.openGames.push(gameUrl);
          console.log("Open Games : " + gameUrl);
        }
      }, function errorCallback(response) {
        console.log("Something is going wrong on gameServiceUrl");
      });
    };

  };
  
}]);
