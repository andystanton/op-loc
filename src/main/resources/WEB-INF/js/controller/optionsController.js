angular.module('optLocApp').controller("optionsController", function($scope, optLocService) {
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
        [0, 9999],
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
