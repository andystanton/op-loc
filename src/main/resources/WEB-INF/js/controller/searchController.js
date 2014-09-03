angular.module('optLocApp').controller("searchController", function($scope, $http, optLocService) {
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