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

        var params = {
            'range-min': optLocService.options['range-min'],
            'range-max': optLocService.options['range-max'],
            'population-min': optLocService.options['population-min']
        }

        if (typeof optLocService.options['population-max'] !== 'undefined') {
            console.log("setting max pop to " + optLocService.options['population-max'])
            params['population-max'] = optLocService.options['population-max'];
        }

        $http({
            url: '/find/near/' + selectedLocation.id,
            method: "GET",
            params: params
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
        zoom: 1,
        markers: [],
        control: {},
        bounds: {}
    };

    $scope.map.bounds = {};

    var donut = undefined;

    $scope.$on('updateLocationEvent', function() {
        var rawLocation = optLocService.location;
        var center = rawLocation.center;

        if (typeof donut !== 'undefined') {
            donut.setMap(null);
        }
        donut = new google.maps.Polygon({
            paths: drawDonut(center, optLocService.options['range-max'], optLocService.options['range-min'], 500),
            strokeColor: "#08B21F",
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: "#08B21F",
            fillOpacity: 0.5
        });

        donut.setMap($scope.map.control.getGMap());

        $scope.map.bounds = {
            northeast: {
                latitude: bounds.getNorthEast().lat(),
                longitude: bounds.getNorthEast().lng()
            },
            southwest: {
                latitude: bounds.getSouthWest().lat(),
                longitude: bounds.getSouthWest().lng()
            }
        };

        var location = {
            id: rawLocation.id,
            latitude: rawLocation.center.latitude,
            longitude: rawLocation.center.longitude,
            options: {
                labelContent: rawLocation.name,
                icon: 'images/markers/symbol_inter.png'
            }
        };

        var nearbyLocations = _.map(optLocService.nearbyLocations, function(rawLocation) {
            return {
                id: rawLocation.id,
                latitude: rawLocation.center.latitude,
                longitude: rawLocation.center.longitude,
                options: {
                    labelContent: rawLocation.name,
                    icon: 'images/markers/house.png'
                }
            };
        });

        $scope.map.markers = _.union([location], nearbyLocations);

        $scope.map.center = {
            latitude: location.latitude,
            longitude: location.longitude
        };
    });

    var bounds;
    var drawDonut = function(center, rangeMax, rangeMin, threshold) {
        bounds = new google.maps.LatLngBounds();
        return [
            drawCircle(new google.maps.LatLng(center.latitude, center.longitude), rangeMin - threshold, -1),
            drawCircle(new google.maps.LatLng(center.latitude, center.longitude), rangeMax + threshold, 1)
        ];
    }

    var drawCircle = function(point, radius, dir) {
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

            bounds.extend(extp[extp.length - 1]);

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

    $scope.rangeConfig = {
        range: true,
        change: rangeChange,
        slide: rangeChange
    };

    $scope.options = {
        range: [4000, 10000],
        population: 1
    };

    var populationRange = [
        [1000, 9999],
        [10000, 49999],
        [50000, undefined]
    ];

    $scope.$watch("options.population", function() {
        optLocService.options['population-min'] = populationRange[$scope.options.population][0];
        optLocService.options['population-max'] = populationRange[$scope.options.population][1];
        optLocService.refresh();
    });

    $scope.$watch("options.range", function() {
        optLocService.options['range-min'] = $scope.options.range[0];
        optLocService.options['range-max'] = $scope.options.range[1];
        optLocService.refresh();
    });
});
