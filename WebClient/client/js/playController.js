var playCtrl = angular.module('playCtrl', []);

app.controller('getGame', ['$scope', '$http', function ($scope, $http){



  $scope.joinGameasd = function (){
    var user = JSON.parse(window.localStorage['user'] || '{}');
    $scope.user = user;
    console.log($scope.user);
    // Get service-id by name and put them in gameServiceIds-array
    $http({
      method: 'GET',
      url: $scope.user.game
    }).then(function successCallback(response) {
      console.log(response);
    }, function errorCallback(response) {
      console.log("Something is going wrong on joinGameasd");
    });
  };

}]);
