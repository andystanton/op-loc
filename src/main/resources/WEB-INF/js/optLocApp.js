var app = angular.module('optLocApp', ['google-maps', 'ui.bootstrap', 'ui.slider']);

app.factory('optLocService', function($rootScope, $http) {
    var optLocService = {};

    optLocService.location = {};
    optLocService.nearbyLocations = [];
    optLocService.options = {
        'range-min': 1000,
        'range-max': 10000,
        'population-min': 5000,
        'population-max': 1000000
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
                'range-min': optLocService.options['range-min'],
                'range-max': optLocService.options['range-max'],
                'population-min': optLocService.options['population-min'],
                'population-max': optLocService.options['population-max']
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

    var donut;

    $scope.$on('updateLocationEvent', function() {
        var rawLocation = optLocService.location;
        var rawLatLong = rawLocation.latlong;

        if (typeof donut !== 'undefined') {
            donut.setMap(null);
        }
        donut = new google.maps.Polygon({
            paths: [
                drawCircle(new google.maps.LatLng(rawLatLong.latitude, rawLatLong.longitude), optLocService.options['range-max'] + 500, 1),
                drawCircle(new google.maps.LatLng(rawLatLong.latitude, rawLatLong.longitude), optLocService.options['range-min'] - 500, -1)
            ],
            strokeColor: "#08B21F",
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: "#08B21F",
            fillOpacity: 0.5
        });
        donut.setMap($scope.map.control.getGMap());

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
    });

    function drawCircle(point, radius, dir) {
        var d2r = Math.PI / 180;    // degrees to radians
        var r2d = 180 / Math.PI;    // radians to degrees
        var earthsradius = 6377830;    // 3963 is the radius of the earth in miles

        var points = 32;

        // find the radius in lat/lon
        var rlat = (radius / earthsradius) * r2d;
        var rlng = rlat / Math.cos(point.lat() * d2r);


        var extp = new Array();
        if (dir == 1) {
            var start = 0;
            var end = points + 1 // one extra here makes sure we connect the
        } else {
            var start = points + 1;
            var end = 0
        }
        for (var i = start; (dir==1 ? i < end : i > end); i = i + dir) {
            var theta = Math.PI * (i / (points/2));
            ey = point.lng() + (rlng * Math.cos(theta)); // center a + radius x * cos(theta)
            ex = point.lat() + (rlat * Math.sin(theta)); // center b + radius y * sin(theta)
            extp.push(new google.maps.LatLng(ex, ey));

        }
        return extp;
    }


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
    // leaving this in - might want to scale drawn circle in real
    // time but update markers only when slider stops.
    function rangeChange(event, ui) { }
    function populationChange(event, ui) { }

    $scope.rangeConfig = {
        range: true,
        change: rangeChange,
        slide: rangeChange
    };

    $scope.populationConfig = {
        range: true,
        change: populationChange,
        slide: populationChange
    };

    $scope.options = {
        range: [4000, 10000],
        population: [5000, 100000]
    };

    $scope.$watch("options.population", function() {
        optLocService.options['population-min'] = $scope.options.population[0];
        optLocService.options['population-max'] = $scope.options.population[1];
        optLocService.refresh();
    });

    $scope.$watch("options.range", function() {
        optLocService.options['range-min'] = $scope.options.range[0];
        optLocService.options['range-max'] = $scope.options.range[1];
        optLocService.refresh();
    });
});
