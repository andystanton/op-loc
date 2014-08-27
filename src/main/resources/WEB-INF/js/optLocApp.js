var app = angular.module('optLocApp', ['google-maps', 'ui.bootstrap', 'ui.slider']);

app.controller("mapController", function($scope) {
    $scope.map = {
        center: {
            latitude: 51.51279,
            longitude: -0.09184
        },
        zoom: 8,
        markers: []
    };

    $scope.$on('someEvent', function(event, args) {
        $scope.map.markers = [];
        $scope.map.markers = [{
            id: args.id,
            latitude: args.latlong.latitude,
            longitude: args.latlong.longitude
        }];
        console.log($scope.map.markers);
    });
});

app.controller("searchController", function($scope, $http) {
  $scope.selected = undefined;

  $scope.getOptLoc = function(val) {
    return $http.get('/find/name/' + val).then(function(res) {
      var addresses = [];
      angular.forEach(res.data, function(item) {
        addresses.push(item);
      });
      return addresses;
    });
  }

  $scope.onSelect = function($item, $model, $label) {
    console.log("emitting event");
    $scope.$emit('someEvent', $item);
  };
});

app.controller("optionsController", function($scope, $http) {
    $scope.demoVals = {
        sliderExample9:     [-0.52, 0.54]
    };
});

