angular.module('optLocApp').controller("mapController", function($scope, $http, optLocService) {
    $scope.map = {
        center: {
            latitude: 0,
            longitude: 0
        },
        zoom: 1,
        markers: [],
        control: {},
        bounds: {},
        range: {
            center: {
                latitude: 0,
                longitude: 0
            }
        }
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