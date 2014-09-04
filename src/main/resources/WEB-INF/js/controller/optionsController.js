angular.module('optLocApp').controller("optionsController", function($scope, optLocService) {
    $scope.rangeTypes = {
        NONE: undefined,
        CIRCLE: 0,
        DONUT: 1
    };

    $scope.donutRangeConfig = {
        range: true
    };

    $scope.circleRangeConfig = { };

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

    $scope.$watch("options.population", function() {
        optLocService.options['population-min'] = populationRange[$scope.options.population][0];
        optLocService.options['population-max'] = populationRange[$scope.options.population][1];
        optLocService.refresh();
    });

    $scope.$watch("options.donutRange", function() {
        optLocService.options['range-min'] = $scope.options.donutRange[0];
        optLocService.options['range-max'] = $scope.options.donutRange[1];
        $scope.options.circleRange = $scope.options.donutRange[1];
        optLocService.refresh();
    });

    $scope.$watch("options.circleRange", function() {
        optLocService.options['range-max'] = $scope.options.circleRange;
        $scope.options.donutRange[1] = $scope.options.circleRange;
        optLocService.refresh();
    });

    $scope.$watch("options.rangeType", function() {
        if ($scope.options.rangeType == $scope.rangeTypes.CIRCLE) {
            optLocService.options['range-min'] = 0;
            $scope.options.donutRange[0] = 0;
        }
        console.log("range type changed to " + $scope.options.rangeType);
    });
});
