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

    var donut;

    $scope.$on('updateLocationEvent', function() {
        var rawLocation = optLocService.location;
        var rawLatLong = rawLocation.latlong;

        if (typeof donut !== 'undefined') {
            donut.setMap(null);
        }
        donut = new google.maps.Polygon({
            paths: [
                drawCircle(new google.maps.LatLng(rawLatLong.latitude, rawLatLong.longitude), optLocService.options.range, 1),
                drawCircle(new google.maps.LatLng(rawLatLong.latitude, rawLatLong.longitude), optLocService.options.range - 5000, -1)
            ],
            strokeColor: "#0000FF",
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: "#FF0000",
            fillOpacity: 0.35
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
    function rangeChange(event, ui) {
        // leaving this in - might want to scale drawn circle in real
        // time but update markers only when slider stops.
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

    $scope.$watch('options.range', function() {
        optLocService.options.range = $scope.options.range;
        optLocService.refresh();
    });
});
