var app = angular.module('optLocApp', ['google-maps', 'ui.bootstrap', 'ui.slider']);

app.factory('optLocService', function($rootScope, $http) {
    var optLocService = {};

    optLocService.location = {};
    optLocService.nearbyLocations = [];
    optLocService.options = {
        range: 10000
    };

    optLocService.refresh = function() {
        if (typeof this.location.id !== 'undefined') {
            optLocService.selectLocation(this.location)
        }
    }

    optLocService.selectLocation = function(selectedLocation) {
        this.location = selectedLocation;
        $http({
            url: '/find/near/' + selectedLocation.id,
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

app.controller("mapController", function($scope, $http, optLocService) {
    $scope.map = {
        center: {
            latitude: 51.51279,
            longitude: -0.09184
        },
        zoom: 8,
        markers: [],
        control: {}
    };

    $scope.$on('updateLocationEvent', function() {
        var rawLocation = optLocService.location;

        var location = {
            id: rawLocation.id,
            latitude: rawLocation.latlong.latitude,
            longitude: rawLocation.latlong.longitude,
            options: {
                labelContent: rawLocation.name
            }
        };

        var nearbyLocations = _.map(optLocService.nearbyLocations, function(rawLocation) {
            return {
                id: rawLocation.id,
                latitude: rawLocation.latlong.latitude,
                longitude: rawLocation.latlong.longitude,
                options: {
                    labelContent: rawLocation.name
                }
            };
        });

        $scope.map.markers = _.union([location], nearbyLocations);

        $scope.map.center = {
            latitude: location.latitude,
            longitude: location.longitude
        };

        $scope.map.zoom = 11;

        $scope.map.range = {
            id: 1,
            center: {
                latitude: location.latitude,
                longitude: location.longitude
            },
            radius: optLocService.options.range,
            stroke: {
                color: '#08B21F',
                weight: 2,
                opacity: 1
            },
            fill: {
                color: '#08B21F',
                opacity: 0.5
            },
            visible: (typeof location.id !== 'undefined')
        };
    });

    var init = function () {
        var searchString = 'London';
        $http.get('/find/name/' + searchString).then(function(response) {
            optLocService.selectLocation(response.data[0]);
        });
    };

    init();
});

app.controller("searchController", function($scope, $http, optLocService) {
    $scope.selected = undefined;

    $scope.getOptLoc = function(searchString) {
        return $http.get('/find/name/' + searchString).then(function(response) {
            return response.data;
        });
    }

    $scope.onSelect = function($item, $model, $label) {
        optLocService.selectLocation($item);
    };
});

app.controller("optionsController", function($scope, optLocService) {
    $scope.rangeOptions = {
        orientation: 'vertical',
        range: 'min'
    };

    $scope.options = {
        range: 10000
    };

    $scope.$watch('options.range', function() {
        optLocService.options.range = $scope.options.range;
        optLocService.refresh();
    });
});
