var app = angular.module('optLocApp', ['google-maps', 'ui.bootstrap', 'ui.slider']);

app.factory('optLocService', function($rootScope, $http) {
    var optLocService = {};

    optLocService.location = {};
    optLocService.nearbyLocations = [];
    optLocService.options = {
        range: 10000
    };

    optLocService.updateOptions = function(range) {
        this.options.range = range;
        if ('undefined' !== typeof this.location.id) {
            optLocService.selectLocation(this.location)
        }
    }

    optLocService.selectLocation = function(newLocation) {
        this.location = newLocation;
        $http({
            url: '/find/near/' + newLocation.id,
            method: "GET",
            params: {
                range: optLocService.options.range
            }
        }).then(function(response) {
            optLocService.nearbyLocations = response.data;
            optLocService.broadcastItem();
        });
    };

    optLocService.broadcastItem = function() {
        $rootScope.$broadcast('updateLocationEvent');
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

    $scope.$on('updateLocationEvent', function() {
        var location = optLocService.location;
        var options = optLocService.options;

        $scope.map.markers = [{
            id: location.id,
            latitude: location.latlong.latitude,
            longitude: location.latlong.longitude
        }];

        angular.forEach(optLocService.nearbyLocations, function(location) {
            $scope.map.markers.push({
                id: location.id,
                latitude: location.latlong.latitude,
                longitude: location.latlong.longitude
            });
        });

        $scope.map.range = {
            id: 1,
            center: {
                latitude: location.latlong.latitude,
                longitude: location.latlong.longitude
            },
            radius: options.range,
            stroke: {
                color: '#08B21F',
                weight: 2,
                opacity: 1
            },
            fill: {
                color: '#08B21F',
                opacity: 0.5
            },
            visible: (typeof location.id != undefined)
        };
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

app.controller("optionsController", function($scope, optLocService) {
    function rangeChange(event, ui) {
        optLocService.updateOptions($scope.options.range);
    }

    $scope.rangeOptions = {
        orientation: 'vertical',
        range: 'min',
        change: rangeChange,
        slide: rangeChange
    };

    $scope.options = {
        range: 10000
    };
});
