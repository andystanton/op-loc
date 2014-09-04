angular.module('optLocApp').controller("optionsController", function($scope, optLocService) {
    $scope.rangeTypes = {
        NONE: undefined,
        CIRCLE: 0,
        DONUT: 1
    };

    $scope.donutRangeConfig = {
        range: true,
        disabled: false
    };

    $scope.circleRangeConfig = {
        disabled: true
    };

    $scope.options = {
        rangeType: $scope.rangeTypes.DONUT,
        donutRange: [4000, 10000],
        circleRange: 10000,
        population: 1
    };

    var populationRange = [
        [0, 9999],
        [10000, 49999],
        [50000, undefined]
    ];

    var updateDonut = function() {
        optLocService.options['range-min'] = $scope.options.donutRange[0];
        optLocService.options['range-max'] = $scope.options.donutRange[1];
        optLocService.refresh();
    }

    var updateCircle = function() {
        optLocService.options['range-min'] = 0;
        optLocService.options['range-max'] = $scope.options.circleRange;
        optLocService.refresh();
    }

    var updatePopulation = function() {
        optLocService.options['population-min'] = populationRange[$scope.options.population][0];
        optLocService.options['population-max'] = populationRange[$scope.options.population][1];
        optLocService.refresh();
    }

    $scope.$watch("options.donutRange", updateDonut);
    $scope.$watch("options.circleRange", updateCircle);
    $scope.$watch("options.population", updatePopulation);

    $scope.$watch("options.rangeType", function() {
        if ($scope.options.rangeType == $scope.rangeTypes.CIRCLE) {
            $scope.circleRangeConfig.disabled = false;
            $scope.donutRangeConfig.disabled = true;
            updateCircle();
        } else if ($scope.options.rangeType == $scope.rangeTypes.DONUT) {
            $scope.donutRangeConfig.disabled = false;
            $scope.circleRangeConfig.disabled = true;
            updateDonut();
        }
        console.log("range type changed to " + $scope.options.rangeType);
    });
});
