var app = angular.module('optLocApp', ['google-maps', 'ui.bootstrap', 'ui.slider']);

app.factory('optLocService', function($rootScope) {
    var optLocService = {};

    optLocService.location = '';

    optLocService.selectLocation = function(newLocation) {
        this.location = newLocation;
        this.broadcastItem();
    };

    optLocService.broadcastItem = function() {
        $rootScope.$broadcast('selectLocationEvent');
    };

    return optLocService;
});

app.controller("mapController", function($scope, optLocService) {
    $scope.map = {
        center: {
            latitude: 51.51279,
            longitude: -0.09184
        },
        zoom: 8,
        markers: []
    };

    $scope.$on('selectLocationEvent', function() {
        var location = optLocService.location;

        $scope.map.markers = [{
            id: location.id,
            latitude: location.latlong.latitude,
            longitude: location.latlong.longitude
        }];
        console.log($scope.map.markers);
    });
});

app.controller("searchController", function($scope, $http, optLocService) {
    $scope.selected = undefined;

    $scope.getOptLoc = function(searchString) {
        return $http.get('/find/name/' + searchString).then(function(response) {
            var locations = [];

            angular.forEach(response.data, function(location) {
                locations.push(location);
            });
            return locations;
        });
    }

    $scope.onSelect = function($item, $model, $label) {
        optLocService.selectLocation($item);
    };
});

app.controller("optionsController", function($scope, $http) {
    $scope.demoVals = {
        sliderExample9: [-0.52, 0.54]
    };
});
