angular.module('optLocApp').factory('optLocService', function($rootScope, $http) {
    var optLocService = {};

    optLocService.location = {};
    optLocService.nearbyLocations = [];
    optLocService.options = {};

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

        if (!_.isUndefined(optLocService.options['population-max'])) {
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