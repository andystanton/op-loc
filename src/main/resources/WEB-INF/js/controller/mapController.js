angular.module('optLocApp').controller("mapController", function($scope, $http, optLocService) {
    $scope.map = {
        center: { latitude: 0, longitude: 0 },
        zoom: 1,
        markers: [],
        control: {},
        bounds: {},
        range: { center: { latitude: 0, longitude: 0 } },
        options: {
            streetViewControl: false,
            mapTypeControl: false
        }
    };

    var donut = undefined;

    var apiLocationToMapLocation = function(apiLocation) {
        return {
            id: apiLocation.id,
            latitude: apiLocation.center.latitude,
            longitude: apiLocation.center.longitude,
            options: {
                icon: this.markerIcon
            }
        }
    }

    $scope.$on('updateLocationEvent', function() {
        var location = apiLocationToMapLocation.bind({ markerIcon: 'images/markers/symbol_inter.png' })(optLocService.location);
        var nearbyLocations = _.map(optLocService.nearbyLocations, apiLocationToMapLocation.bind({ markerIcon: 'images/markers/house.png' }));

        var bounds = drawDonut(optLocService.location.center, optLocService.options['range-max'], optLocService.options['range-min']);

        $scope.map.center = {
            latitude: location.latitude,
            longitude: location.longitude
        };

        $scope.map.bounds = {
            northeast: { latitude: bounds.getNorthEast().lat(), longitude: bounds.getNorthEast().lng() },
            southwest: { latitude: bounds.getSouthWest().lat(), longitude: bounds.getSouthWest().lng() }
        };

        $scope.map.markers = _.union([location], nearbyLocations);

    });

    var drawDonut = function(center, rangeMax, rangeMin) {
        if (!_.isUndefined(donut)) donut.setMap(null);

        var threshold = 1000;
        var bounds = new google.maps.LatLngBounds();

        donut = new google.maps.Polygon({
            paths: [
                drawCircle(new google.maps.LatLng(center.latitude, center.longitude), rangeMin - threshold, -1, bounds),
                drawCircle(new google.maps.LatLng(center.latitude, center.longitude), rangeMax + threshold, 1, bounds)
            ],
            strokeColor: "#08B21F",
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: "#08B21F",
            fillOpacity: 0.5
        });

        donut.setMap($scope.map.control.getGMap());

        return bounds;
    }

    var drawCircle = function(point, radius, dir, bounds) {
        var d2r = Math.PI / 180;        // degrees to radians
        var r2d = 180 / Math.PI;        // radians to degrees
        var earthsradius = 6377830;     // 3963 is the radius of the earth in miles

        var points = 32;

        // find the radius in lat/long
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