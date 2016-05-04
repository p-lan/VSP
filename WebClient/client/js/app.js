var app = angular.module('RESTopolyClient', ['ngRoute', 'ngCookies', 'appControllers']);

app.config(['$routeProvider', function($routeProvider){

  $routeProvider.
  when('/play', {
    templateUrl: 'partials/play.html'
  }).
  otherwise({
    templateUrl: 'partials/start.html'
  });

}]);
